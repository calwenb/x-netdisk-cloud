package com.wen.releasedao.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.util.StringUtils;
import com.wen.releasedao.core.annotation.FieldName;
import com.wen.releasedao.core.annotation.IdField;
import com.wen.releasedao.core.annotation.TableName;
import com.wen.releasedao.core.enums.SelectTypeEnum;
import com.wen.releasedao.core.exception.MapperException;
import com.wen.releasedao.util.SqlUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Mapper工具类
 * 为BaseMapper提供支持
 *
 * @author calwen
 * @since 2022/7/9
 */
public class MapperUtil {
    /**
     * 解析字段
     * 过滤 @FieldName(exist = false)
     */
    public static <T> Field[] parseField(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        fields = Arrays.stream(fields).filter((f) -> {
            f.setAccessible(true);
            FieldName annotation = f.getDeclaredAnnotation(FieldName.class);
            if (annotation != null) {
                return annotation.exist();
            }
            return true;
        }).toArray(Field[]::new);
        return fields;
    }

    /**
     * 解析 主键id
     */
    public static <T> String parseId(Class<T> tClass) {
        String id = null;
        Field[] fields = tClass.getDeclaredFields();

        //找到属性上 @IdField 注解
        for (Field f : fields) {
            f.setAccessible(true);
            IdField idFieldAnn = f.getDeclaredAnnotation(IdField.class);
            if (idFieldAnn == null) {
                continue;
            }
            if (!StringUtils.isNullOrEmpty(idFieldAnn.value())) {
                id = idFieldAnn.value();
            } else {
                id = SqlUtil.camelToSnake(f.getName());
            }
        }
        //未指定，默认第一个
        if (id == null) {
            id = SqlUtil.camelToSnake(fields[0].getName());
        }
        return id;
    }


    /**
     * 解析表名
     * &#064;TableName("name")
     */
    public static <T> String parseTableName(Class<T> tClass) {
        //反射获取目标信息
        TableName tableNameAnno = tClass.getDeclaredAnnotation(TableName.class);
        String className = tClass.getSimpleName();

        //确定表名
        String tableName;
        if (tableNameAnno != null) {
            tableName = tableNameAnno.value();
        } else {
            //是否驼峰转蛇形
            tableName = SqlUtil.camelToSnake(className);
        }
        return tableName;
    }

    /**
     * 解析 对象字段与sql字段映射
     */
    public static <T> Map<String, String> resultMap(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        Map<String, String> map = new LinkedHashMap<>(fields.length);
        Arrays.stream(fields).filter((f) -> {
            f.setAccessible(true);
            FieldName annotation = f.getDeclaredAnnotation(FieldName.class);
            if (annotation != null) {
                return annotation.exist();
            }
            return true;
        }).forEach(f -> {
            String objectField = f.getName();
            String sqlField;
            FieldName anno = f.getDeclaredAnnotation(FieldName.class);
            if (anno != null && !StringUtils.isNullOrEmpty(anno.value())) {
                sqlField = anno.value();
            } else {
                sqlField = SqlUtil.camelToSnake(objectField);
            }
            map.put(objectField, sqlField);
        });
        return map;

    }

    /**
     * 解析生成对象
     */
    public static <T> T parseEntity(ResultSet rs, Map<String, String> resultMap, Field[] fields, Constructor<T> classCon) {
        try {
            Object[] fieldsVal = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                //从 字段映射中获取 sql字段
                fields[i].setAccessible(true);
                String fieldName = fields[i].getName();
                String sqlField = resultMap.get(fieldName);
                if (sqlField == null) {
                    continue;
                }
                fieldsVal[i] = rs.getObject(sqlField);
                //LocalDateTime转Date
                if (fieldsVal[i] != null && fieldsVal[i].getClass().equals(LocalDateTime.class)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    fieldsVal[i] = Date.from(objectMapper.convertValue(fieldsVal[i], LocalDateTime.class).atZone(ZoneId.systemDefault()).toInstant());
                }
            }
            return classCon.newInstance(fieldsVal);
        } catch (Exception e) {
            throw new MapperException("解析生成对象时异常", e);
        }
    }


    /**
     * 获得查询结果，解析成对象
     */
    public static <T> Object getEntity(ResultSet rs, Map<String, String> resultMap, Class<T> tClass, SelectTypeEnum type) {
        try {
            List<T> list = new ArrayList<>();
            //获得 对象属性数组，类构造器,字段映射
            Field[] fields = tClass.getDeclaredFields();
            Constructor<T> classCon = MapperUtil.getConstructor(tClass);

            //返回数据解析实体
            while (rs.next()) {
                // count(*)
                if (Objects.equals(type, SelectTypeEnum.COUNT)) {
                    return rs.getInt(1);
                }
                T entity = parseEntity(rs, resultMap, fields, classCon);
                if (type == SelectTypeEnum.ONE) {
                    return entity;
                }
                list.add(entity);
            }
            // 集合返回则不能返回null
            if (SelectTypeEnum.ALL.equals(type)) {
                return list;
            }
            return list.isEmpty() ? null : list;
        } catch (Exception e) {
            throw new MapperException("解析生成目标时异常", e);
        }
    }

    /**
     * 获取类构造器
     */
    public static <T> Constructor<T> getConstructor(Class<T> tClass) {
        try {
            Field[] fields = tClass.getDeclaredFields();
            //获取全部属性的类
            Class<?>[] classes = new Class[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                classes[i] = fields[i].getType();
            }
            return tClass.getDeclaredConstructor(classes);
        } catch (NoSuchMethodException e) {
            throw new MapperException("获取构建器时异常", e);
        }
    }
}
