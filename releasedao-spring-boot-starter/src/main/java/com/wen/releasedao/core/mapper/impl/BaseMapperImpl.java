package com.wen.releasedao.core.mapper.impl;

import com.mysql.cj.util.StringUtils;
import com.wen.releasedao.core.annotation.CacheQuery;
import com.wen.releasedao.core.annotation.CacheUpdate;
import com.wen.releasedao.core.annotation.CreateTime;
import com.wen.releasedao.core.annotation.UpdateTime;
import com.wen.releasedao.core.enums.CacheUpdateEnum;
import com.wen.releasedao.core.enums.SaveTypeEnum;
import com.wen.releasedao.core.enums.SelectTypeEnum;
import com.wen.releasedao.core.exception.MapperException;
import com.wen.releasedao.core.helper.MapperHelper;
import com.wen.releasedao.core.manager.LoggerManager;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import com.wen.releasedao.core.wrapper.SetWrapper;
import com.wen.releasedao.util.CastUtil;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * BaseMapper实现类
 *
 * @author calwen
 * @since 2022 /7/9
 */
public class BaseMapperImpl implements BaseMapper {
    /**
     * 数据库连接 AOP 自动管理连接
     */
    private Connection conn;


    @Override
    public <T> int getCount(Class<T> eClass, QueryWrapper wrapper) {
        return (int) baseSelect(eClass, wrapper, SelectTypeEnum.COUNT);
    }

    @Override
    public <T> int getCount(Class<T> eClass) {
        return (int) baseSelect(eClass, null, SelectTypeEnum.COUNT);
    }

    @Override

    public <T> List<T> getList(Class<T> eClass, QueryWrapper wrapper) {
        return (List<T>) baseSelect(eClass, wrapper, SelectTypeEnum.ALL);

    }

    @Override

    public <T> List<T> getList(Class<T> eClass) {
        return (List<T>) baseSelect(eClass, null, SelectTypeEnum.ALL);
    }

    @Override
    @CacheQuery

    public <T> T get(Class<T> eClass, QueryWrapper wrapper) {
        return (T) baseSelect(eClass, wrapper, SelectTypeEnum.ONE);
    }


    @Override
    @CacheQuery

    public <T> T getById(Class<T> eClass, Object id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(MapperHelper.parseId(eClass, false), id);
        return (T) baseSelect(eClass, wrapper, SelectTypeEnum.ONE);
    }

    @Override

    public <T> T get(Class<T> eClass) {
        return (T) baseSelect(eClass, null, SelectTypeEnum.ONE);
    }

    @Override
    public <T> boolean add(T entity) {
        return baseSave(entity, SaveTypeEnum.INSERT) > 0;
    }


    @Override
    public <T> boolean addBatch(List<T> entityList) {
        return baseBatchSave(entityList, SaveTypeEnum.INSERT) > 0;
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.ENTITY)
    public <T> boolean replace(T entity) {
        return baseSave(entity, SaveTypeEnum.REPLACE) > 0;
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.BATCH)
    public <T> boolean replaceBatch(List<T> entityList) {
        return baseBatchSave(entityList, SaveTypeEnum.REPLACE) > 0;
    }

    @CacheUpdate(CacheUpdateEnum.ENTITY)
    public <T> boolean save(T entity) {
        return replace(entity);
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.BATCH)
    public <T> boolean saveBatch(List<T> entityList) {
        return replaceBatch(entityList);
    }

    @Override
    public <T> List<T> selectSQL(Class<T> eClass, String sql, Object[] values) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            Map<String, String> resultMap = MapperHelper.resultMap(eClass);
            for (int i = 0; values != null && i < values.length; i++) {
                pst.setObject(i + 1, values[i]);
            }
            ResultSet rs = pst.executeQuery();
            return (List<T>) MapperHelper.getEntity(rs, resultMap, eClass, null);
        } catch (Exception e) {
            throw new MapperException("自定义查询SQL 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.WRAPPER)
    public <T> boolean delete(Class<T> eClass, QueryWrapper queryWrapper) {
        PreparedStatement pst = null;
        String sql = "";
        List<Object> values = new ArrayList<>();
        try {
            //删除必须指定条件，否则会全表删除
            if (queryWrapper == null) {
                throw new MapperException("删除必须查询条件，否则会全表删除!!!");
            }
            //条件查询，解析where sql
            HashMap<String, Object> wrapperResult = queryWrapper.getResult();
            String whereSQL = String.valueOf(wrapperResult.get("sql"));
            values = CastUtil.castList(wrapperResult.get("values"), Object.class);
            if (StringUtils.isNullOrEmpty(whereSQL)) {
                throw new MapperException("删除必须查询条件，否则会全表删除!!!");
            }

            String tableName = MapperHelper.parseTableName(eClass);

            sql = "DELETE FROM " + tableName + whereSQL;

            //执行查询
            pst = conn.prepareStatement(sql);

            // 占位符设值
            for (int i = 0; values != null && i < values.size(); i++) {
                pst.setObject(i + 1, values.get(i));
            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new MapperException("delete 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }


    @Override
    public <T> boolean deleteById(Class<T> eClass, Integer id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(MapperHelper.parseId(eClass, false), id);
        return delete(eClass, wrapper);
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.WRAPPER)
    public <T> boolean update(Class<T> eClass, SetWrapper setWrapper, QueryWrapper queryWrapper) {
        PreparedStatement pst = null;
        String sql = "";
        List<Object> values = new ArrayList<>();
        try {
            //更新必须指定条件
            if (setWrapper == null || queryWrapper == null) {
                System.out.println("更新必须指定set,where");
                return false;
            }
            HashMap<String, Object> wrapperResult;
            //条件查询，解析where sql
            wrapperResult = queryWrapper.getResult();
            String whereSQL = String.valueOf(wrapperResult.get("sql"));
            List<Object> whereValues = CastUtil.castList(wrapperResult.get("values"), Object.class);

            //条件查询，解析set sql
            wrapperResult = setWrapper.getResult();
            String setSql = String.valueOf(wrapperResult.get("sql"));
            List<Object> setValues = CastUtil.castList(wrapperResult.get("values"), Object.class);

            if (StringUtils.isNullOrEmpty(setSql) || StringUtils.isNullOrEmpty(whereSQL)) {
                throw new MapperException("更新必须指定set,where");
            }
            //解析表名
            String tableName = MapperHelper.parseTableName(eClass);

            //拼接sql
            sql = "UPDATE " + tableName + setSql + whereSQL;

            //执行查询
            pst = conn.prepareStatement(sql);

            // 占位符设值
            values = new ArrayList<>();
            values.addAll(setValues);
            values.addAll(whereValues);
            for (int i = 0; i < values.size(); i++) {
                pst.setObject(i + 1, values.get(i));
            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new MapperException("更新 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }

    @Override
    @CacheUpdate(CacheUpdateEnum.OTHER)
    public <T> boolean exeSQL(String sql, Object[] values) {
        PreparedStatement pst = null;
        try {
            //执行查询
            pst = conn.prepareStatement(sql);
            //设值
            for (int i = 0; values != null && i < values.length; i++) {
                pst.setObject(i + 1, values[i]);
            }
            return pst.execute();

        } catch (SQLException e) {
            throw new MapperException("自定义执行sql 时异常", e);
        } finally {
            LoggerManager.log(pst, sql, values);
        }
    }


    /**
     * 查询数据 base方法
     * 通过type 返回不同的结果类型
     *
     * @param eClass  目标class
     * @param wrapper 查询构造器
     * @param type    查询类型
     */
    private <T> Object baseSelect(Class<T> eClass, QueryWrapper wrapper, SelectTypeEnum type) {
        PreparedStatement pst = null;
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<>();
        try {
            String tableName = MapperHelper.parseTableName(eClass);
            Map<String, String> resultMap = MapperHelper.resultMap(eClass);
            //sql拼接
            sql = new StringBuilder();
            sql.append("SELECT ");
            if (Objects.equals(type, SelectTypeEnum.COUNT)) {
                sql.append(" COUNT(*) ");
            } else if (wrapper != null && !StringUtils.isNullOrEmpty(wrapper.getSelectField())) {
                //自定义 查询结果：剔除字段映射map 中 不查询的 字段
                String[] selectFields = wrapper.getSelectField().split(",");
                resultMap = resultMap.entrySet().stream().filter(entry -> {
                    String sqlField = entry.getValue();
                    return Arrays.asList(selectFields).contains(sqlField);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                sql.append(wrapper.getSelectField());
                System.out.println(resultMap);
            } else {
                sql.append(" * ");
            }

            sql.append(" FROM ").append(tableName).append(" ");

            //有Wrapper时
            if (wrapper != null) {
                //条件查询
                HashMap<String, Object> wrapperResult = wrapper.getResult();
                String whereSQL = String.valueOf(wrapperResult.get("sql"));
                values = CastUtil.castList(wrapperResult.get("values"), Object.class);
                if (!StringUtils.isNullOrEmpty(whereSQL)) {
                    sql.append(whereSQL);
                }
            }
            //执行查询
            pst = conn.prepareStatement(String.valueOf(sql));

            //需要设值时
            for (int i = 0; values != null && i < values.size(); i++) {
                pst.setObject(i + 1, values.get(i));
            }
            ResultSet rs = pst.executeQuery();
            // 将sql结果集解析 对象或对象集
            return MapperHelper.getEntity(rs, resultMap, eClass, type);
        } catch (Exception e) {
            throw new MapperException("查询 时异常", e);
        } finally {
            LoggerManager.log(pst, String.valueOf(sql), values);
        }
    }

    /**
     * 保存数据  base方法
     * 提供saveType执行INSERT、REPLACE操作
     *
     * @param entity-数据
     * @param saveType-保存类型
     */
    private <T> int baseSave(T entity, SaveTypeEnum saveType) {
        PreparedStatement pst = null;
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<>();
        try {
            Class<?> eClass = entity.getClass();
            Map<String, String> resultMap = MapperHelper.resultMap(eClass);
            baseSaveSqlPrefix(eClass, resultMap, saveType, sql);
            baseSaveSqlQuestion(resultMap, sql);
            sql.append(" ) ");
            pst = conn.prepareStatement(String.valueOf(sql), PreparedStatement.RETURN_GENERATED_KEYS);
            //填充Entity实现动态更新
            fillingEntity(eClass, entity, saveType);
            AtomicInteger i = new AtomicInteger(1);
            PreparedStatement finalPst = pst;
            resultMap.forEach((k, v) -> {
                try {
                    Field f = eClass.getDeclaredField(k);
                    f.setAccessible(true);
                    Object value;
                    if (SaveTypeEnum.INSERT.equals(saveType)
                            && (f.isAnnotationPresent(CreateTime.class) || f.isAnnotationPresent(UpdateTime.class))) {
                        value = new Date();
                    } else if (SaveTypeEnum.REPLACE.equals(saveType) && f.isAnnotationPresent(UpdateTime.class)) {
                        value = new Date();
                    } else {
                        value = f.get(entity);
                    }
                    finalPst.setObject(i.get(), value);
                    values.add(value);
                    i.getAndIncrement();
                } catch (Exception e) {
                    throw new MapperException("设置预编译 时异常", e);
                }
            });
            int rs = pst.executeUpdate();
            //更新原实体
            ResultSet GeneratedKeys = pst.getGeneratedKeys();
            if (GeneratedKeys.next()) {
                int id = GeneratedKeys.getInt(1);
                Object newEntity = getById(entity.getClass(), id);
                BeanUtils.copyProperties(newEntity, entity);
            }
            return rs;
        } catch (Exception e) {
            throw new MapperException(" 保存时发生sql异常 ", e);
        } finally {
            LoggerManager.log(pst, String.valueOf(sql), values);
        }
    }


    /**
     * 批量保存
     */
    private <T> int baseBatchSave(List<T> entityList, SaveTypeEnum saveType) {
        PreparedStatement pst = null;
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<>();
        try {
            Class<?> eClass = entityList.get(0).getClass();
            int listSize = entityList.size();
            Map<String, String> resultMap = MapperHelper.resultMap(eClass);

            baseSaveSqlPrefix(eClass, resultMap, saveType, sql);
            for (int i = 0; i < listSize; i++) {
                baseSaveSqlQuestion(resultMap, sql);
                sql.append(" ) ,");
            }
            sql.deleteCharAt(sql.length() - 1);

            pst = conn.prepareStatement(String.valueOf(sql));
            AtomicInteger i = new AtomicInteger(1);
            for (T entity : entityList) {
                PreparedStatement finalPst = pst;
                resultMap.forEach((k, v) -> {
                    try {
                        Field field = eClass.getDeclaredField(k);
                        field.setAccessible(true);
                        Object value;
                        if (field.isAnnotationPresent(CreateTime.class) && SaveTypeEnum.INSERT.equals(saveType)) {
                            value = new Date();
                        } else if (field.isAnnotationPresent(UpdateTime.class) && SaveTypeEnum.REPLACE.equals(saveType)) {
                            value = new Date();
                        } else {
                            value = field.get(entity);
                        }
                        finalPst.setObject(i.get(), value);
                        values.add(value);
                        i.getAndIncrement();
                    } catch (Exception e) {
                        throw new MapperException("批量保存 预编译 时异常", e);
                    }
                });
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            throw new MapperException("批量保存 时异常", e);
        } finally {
            LoggerManager.log(pst, String.valueOf(sql), values);
        }
    }

    /**
     * baseSave sql前缀拼接
     */
    private void baseSaveSqlPrefix(Class<?> eClass, Map<String, String> resultMap, SaveTypeEnum saveType, StringBuilder sql) {
        String tableName = MapperHelper.parseTableName(eClass);
        sql.append(saveType.name()).append(" INTO ").append(tableName).append(" ( ");
        resultMap.forEach((k, v) -> sql.append(v).append(" , "));
        sql.delete(sql.lastIndexOf(","), sql.length()).append(" ) ").append(" VALUES ");
    }

    /**
     * baseSaveSql 问号拼接
     */
    private void baseSaveSqlQuestion(Map<String, String> resultMap, StringBuilder sql) {
        sql.append(" ( ");
        for (int i = 0; i < resultMap.size(); i++) {
            if (i == resultMap.size() - 1) {
                sql.append(" ? ");
                break;
            }
            sql.append(" ?, ");
        }
    }

    /**
     * 更新操作时，通过主键将Entity中属性填充
     */
    private void fillingEntity(Class<?> eClass, Object entity, SaveTypeEnum saveType) throws IllegalAccessException, NoSuchFieldException {
        //插入不操作
        if (SaveTypeEnum.INSERT.equals(saveType)) {
            return;
        }
        String fid = MapperHelper.parseId(eClass, true);
        Field field = eClass.getDeclaredField(fid);
        field.setAccessible(true);
        Object oid = field.get(entity);
        //主键值等于null 不操作
        if (oid == null) {
            return;
        }
        Object data = getById(eClass, oid);
        if (data != null) {
            Field[] fields = eClass.getDeclaredFields();
            ArrayList<String> ignoreFields = new ArrayList<>();
            for (Field f : fields) {
                f.setAccessible(true);
                Object o = f.get(entity);
                if (o != null) {
                    ignoreFields.add(f.getName());
                }
            }
            BeanUtils.copyProperties(data, entity, ignoreFields.toArray(new String[0]));
        }
    }
}
