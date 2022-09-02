package com.wen.releasedao.core.aop;

import com.wen.releasedao.config.PropertyConfig;
import com.wen.releasedao.core.annotation.CacheUpdate;
import com.wen.releasedao.core.enums.CacheUpdateEnum;
import com.wen.releasedao.core.helper.MapperHelper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存AOP类 <br>
 * 遵循缓存旁路模式<br>
 * 执行 <b> Wrapper、或 id 条件</b>为 key 加入缓存 <br>
 * 引入 <b>cache_map</b>代表一条数据的所有Wrapper记录集（更新操作删除对应的缓存）：<br>
 * key= redao:cache:表名:主键名=106（一条行数据），value= 为该数据的所有Wrapper记录集（set）。
 * <p>
 * 查询：缓存 key= id 或 wrapper , 有则值返回，否则加入缓存中。cache_map 记录<br>
 * 更新：<br>
 * 1. id,entity: 删除主键级缓存。删除cache_map id=x，中的所以Wrapper记录集；<br>
 * 2. wrapper，batch：删除该表全部缓存。<br>
 * 3. other:删除全部缓存，如 exeSql。<br>
 *
 * @author calwen
 * @since 2022/7/16
 */
@Aspect
@Order(10)
public class CacheAop {
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    private final String CACHE_PREFIX = "redao:cache:";
    private final String CACHE_MAP_PREFIX = "redao:cache-map:";

    @Pointcut("@annotation(com.wen.releasedao.core.annotation.CacheQuery)")
    private void queryPointcut() {

    }

    @Pointcut("@annotation(com.wen.releasedao.core.annotation.CacheUpdate)")
    private void updatePointcut() {

    }


    @Around("queryPointcut()")
    public Object queryCache(ProceedingJoinPoint joinPoint) throws Throwable {
        log("++++ logger: start cache aop ++++");
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = (Class<?>) args[0];
        String tableName = MapperHelper.parseTableName(targetClass);
        Class<?> aClass = args[1].getClass();
        String keySuffix;
        String cacheId;
        String idValue;
        //缓存 1.QueryWrapper 格式化字符串 。2. id
        if (Objects.equals(QueryWrapper.class, aClass)) {
            QueryWrapper wrapper = (QueryWrapper) args[1];
            keySuffix = String.valueOf(wrapper.getResult());
        } else {
            cacheId = MapperHelper.parseId(targetClass,true);
            idValue = String.valueOf(args[1]);
            keySuffix = cacheId + "=" + idValue;
        }
        String key = CACHE_PREFIX + tableName + ":" + keySuffix;
        Object cache = redisTemplate.opsForValue().get(key);

        if (cache != null) {
            log("have cache ==> key: " + key);
            log("value      ==> " + cache);
            return cache;
        }

        Object rs = joinPoint.proceed();
        //有值放入缓存中
        if (rs != null) {
            if (rs instanceof Collection) {
                return rs;
            }
            redisTemplate.opsForValue().set(key, rs, PropertyConfig.getExpiredTime(), TimeUnit.SECONDS);
            String id = MapperHelper.parseId(rs.getClass(),true);
            Field field = rs.getClass().getDeclaredField(id);
            field.setAccessible(true);
            idValue = String.valueOf(field.get(rs));
            key = CACHE_MAP_PREFIX + tableName + ":id:" + idValue;
            redisTemplate.opsForSet().add(key, keySuffix);
            redisTemplate.expire(key, PropertyConfig.getExpiredTime(), TimeUnit.SECONDS);
            log("have cache ==> key: " + key);
        }
        return rs;

    }


    @Around("updatePointcut()")
    public <T> Object updateCache(ProceedingJoinPoint joinPoint) throws Throwable {
        log("++++ logger: start cache update ++++");

        Object rs = joinPoint.proceed();
        //成功更新操作
        if (rs != null) {
            if (rs instanceof Boolean && Objects.equals(Boolean.FALSE, rs)) {
                return rs;
            }
            if (rs instanceof Integer && (Integer) rs == 0) {
                return rs;
            }

            Object[] args = joinPoint.getArgs();

            Object target = args[0];
            Class<?> targetClass = target.getClass();
            if (target instanceof Collection) {
                List<T> list = (List<T>) target;
                targetClass = list.get(0).getClass();
            }

            String tableName = MapperHelper.parseTableName(targetClass);

            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method method = ms.getMethod();
            CacheUpdate declaredAnnotation = method.getDeclaredAnnotation(CacheUpdate.class);
            CacheUpdateEnum cacheUpdate = declaredAnnotation.value();

            switch (cacheUpdate) {
                //删除行缓存
                case ID:
                case ENTITY:
                    delRowCache(target, tableName);
                    break;
                //删除表缓存
                case WRAPPER:
                case BATCH:
                    delTableCache(tableName);
                    break;
                //删除全部缓存
                case OTHER:
                    delAllCache();
                    break;
                default:
                    throw new RuntimeException("非法操作");
            }

        }
        return rs;
    }

    /**
     * 删除该缓存映射的全部 缓存 <br>
     * 如:  redao:cache-map:client:id:106 <br>
     */
    private void delRowCache(Object target, String tableName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> targetClass = target.getClass();
        String cacheId = MapperHelper.parseId(targetClass,true);
        Field field = targetClass.getDeclaredField(cacheId);
        field.setAccessible(true);
        Object idValue = field.get(target);
        String topKey = CACHE_MAP_PREFIX + tableName + ":id:" + idValue;
        Set<Object> set = redisTemplate.opsForSet().members(topKey);
        set = Optional.ofNullable(set).orElse(Collections.emptySet());
        for (Object o : set) {
            String key = CACHE_PREFIX + tableName + ":" + o;
            redisTemplate.delete(key);
        }
        redisTemplate.delete(topKey);
        log("delete map caches ==> " + topKey);
    }

    /**
     * 删除表级 缓存
     */
    private void delTableCache(String tableName) {
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX + tableName + ":*");
        Set<String> MapKeys = redisTemplate.keys(CACHE_MAP_PREFIX + tableName + ":*");
        keys = Optional.ofNullable(keys).orElse(Collections.emptySet());
        MapKeys = Optional.ofNullable(MapKeys).orElse(Collections.emptySet());
        keys.addAll(MapKeys);
        redisTemplate.delete(keys);
        log("delete caches ==> " + keys);
    }

    /**
     * 删除全部缓存
     */
    private void delAllCache() {
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX + "*");
        Set<String> MapKeys = redisTemplate.keys(CACHE_MAP_PREFIX + "*");
        keys = Optional.ofNullable(keys).orElse(Collections.emptySet());
        MapKeys = Optional.ofNullable(MapKeys).orElse(Collections.emptySet());
        keys.addAll(MapKeys);
        redisTemplate.delete(keys);
        log("delete caches ==> " + keys);
    }

    private void log(String log) {
        if (PropertyConfig.isLogger()) {
            System.out.println(log);
        }
    }


}

