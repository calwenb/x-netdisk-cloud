package com.wen.baseorm.core.mapper;

import com.wen.baseorm.core.wrapper.QueryWrapper;
import com.wen.baseorm.core.wrapper.SetWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装基本CRUD
 *
 * @author calwen
 * @since 2022/7/9
 */
public interface BaseMapper {
    /**
     * 返回匹配指定条件的行数
     * count(*)
     *
     * @param targetClass  目标类型
     * @param queryWrapper 查询构造器
     * @return count(*)
     * @author calwen
     * @since 2022/7/14
     */
    <T> Integer selectCount(Class<T> targetClass, QueryWrapper queryWrapper);


    /**
     * 返回匹配目标表数据行数
     * count(*)
     *
     * @param targetClass 目标类型
     * @return count(*)
     * @author calwen
     * @since 2022/7/14
     */
    <T> Integer selectCount(Class<T> targetClass);


    /**
     * 执行 自定义sql查询
     *
     * @param sql         要执行的sql
     * @param setSqls     ?设置，预编译
     * @param targetClass 目标类型
     * @return 结果集
     * @author calwen
     * @since 2022/7/14
     */
    <T> ArrayList<T> selectSQL(String sql, Object[] setSqls, Class<T> targetClass);


    /**
     * 查询目标，通过 查询构造器
     *
     * @param targetClass  目标类型
     * @param queryWrapper 查询构造器
     * @return 结果集
     * @author calwen
     * @since 2022/7/14
     */
    <T> ArrayList<T> selectList(Class<T> targetClass, QueryWrapper queryWrapper);

    <T> ArrayList<T> selectList(Class<T> targetClass);

    <T> T selectTarget(Class<T> targetClass, QueryWrapper wrapper);

    <T> T selectTargetById(Class<T> targetClass, Object id);


    <T> T selectTarget(Class<T> targetClass);

    <T> int save(T target);

    <T> int insertTarget(T target);

    <T> int insertBatchTarget(List<T> targetList);

    <T> int replaceTarget(T target);

    <T> int replaceBatchTarget(List<T> targetList);

    <T> int batchSave(List<T> targetList);

    <T> int deleteTarget(Class<T> targetClass, QueryWrapper queryWrapper);

    <T> int deleteTargetById(Class<T> targetClass, Integer id);


    <T> int updateTarget(Class<T> targetClass, SetWrapper setWrapper, QueryWrapper queryWrapper);

    <T> boolean exeSQL(String sql, Object[] setSqls);


}
