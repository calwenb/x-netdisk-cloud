package com.wen.releasedao.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.util.StringUtils;
import com.wen.releasedao.core.annotation.FieldName;
import com.wen.releasedao.core.annotation.IdField;
import com.wen.releasedao.core.annotation.TableName;
import com.wen.releasedao.core.enums.SelectTypeEnum;
import com.wen.releasedao.util.SqlUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     *
     * @author calwen
     * @since 2022/7/14
     */

    public static <T> Field[] parseField(Class<T> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
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

    public static <T> String parseId(Class<T> targetClass) {
        String id = null;
        Field[] fields = targetClass.getDeclaredFields();

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
     *
     * @author calwen
     * @since 2022/7/9
     */
    public static <T> String parseTableName(Class<T> targetClass) {
        //反射获取目标信息
        TableName tableNameAnno = targetClass.getDeclaredAnnotation(TableName.class);
        String className = targetClass.getSimpleName();

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
     *
     * @author calwen
     * @since 2022/7/14
     */
    public static <T> Map<String, String> resultMap(Class<T> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
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
            String sqlField = null;
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


    public static <T> T parseTarget(ResultSet rs, Map<String, String> resultMap, Field[] fields, Constructor<T> classCon) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
    }


    /**
     * 获得查询结果，解析成对象
     *
     * @author calwen
     * @since 2022/7/9
     */
    public static <T> Object getTarget(ResultSet rs, Map<String, String> resultMap, Class<T> targetClass, SelectTypeEnum type) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ArrayList<T> targets = new ArrayList<>();
        //获得 对象属性数组，类构造器,字段映射
        Field[] fields = targetClass.getDeclaredFields();
        Constructor<T> classCon = MapperUtil.getConstructor(targetClass);

        //返回数据解析实体
        while (rs.next()) {
            // count(*)
            if (Objects.equals(type, SelectTypeEnum.COUNT)) {
                return rs.getInt(1);
            }
            T target = parseTarget(rs, resultMap, fields, classCon);
            if (type == SelectTypeEnum.ONE) {
                return target;
            }
            targets.add(target);
        }
        // 集合返回则不能返回null
        if (SelectTypeEnum.ALL.equals(type)) {
            return targets;
        }
        return targets.isEmpty() ? null : targets;
    }

    /**
     * 获取类构造器
     *
     * @author calwen
     * @since 2022/7/9
     */
    public static <T> Constructor<T> getConstructor(Class<T> targetClass) throws NoSuchMethodException {
        Field[] fields = targetClass.getDeclaredFields();
        //获取全部属性的类
        Class<?>[] classes = new Class[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            classes[i] = fields[i].getType();
        }
        return targetClass.getDeclaredConstructor(classes);
    }
}
