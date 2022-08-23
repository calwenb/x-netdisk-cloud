package com.wen.releasedao.core.mapper;

import com.wen.releasedao.core.wrapper.QueryWrapper;
import com.wen.releasedao.core.wrapper.SetWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装基本CRUD <br>
 * core
 *
 * @author calwen
 * @since 2022/7/9
 */
public interface BaseMapper {

    /**
     * 查询
     *
     * @param targetClass 目标类型
     */
    <T> T get(Class<T> targetClass);

    /**
     * 查询（通过构建器）
     *
     * @param targetClass 目标类型
     * @param wrapper     构建器
     */
    <T> T get(Class<T> targetClass, QueryWrapper wrapper);

    /**
     * 查询（通过id）
     *
     * @param targetClass 目标类型
     * @param id          主键
     */
    <T> T getById(Class<T> targetClass, Object id);

    /**
     * 查询列表
     *
     * @param targetClass 目标类型
     * @return 结果集
     */
    <T> ArrayList<T> getList(Class<T> targetClass);

    /**
     * 查询列表 （查询构造器）
     *
     * @param targetClass  目标类型
     * @param queryWrapper 查询构造器
     * @return 结果集
     */
    <T> ArrayList<T> getList(Class<T> targetClass, QueryWrapper queryWrapper);


    /**
     * 返回匹配目标表数据行数
     * count(*)
     *
     * @param targetClass 目标类型
     * @return count(*)
     */
    <T> Integer getCount(Class<T> targetClass);

    /**
     * 返回匹配指定条件的行数
     * count(*)
     *
     * @param targetClass  目标类型
     * @param queryWrapper 查询构造器
     * @return count(*)
     */
    <T> Integer getCount(Class<T> targetClass, QueryWrapper queryWrapper);

    /**
     * 保存 数据
     *
     * @param target 数据
     */
    <T> int save(T target);

    /**
     * 插入数据
     *
     * @param target 数据
     */
    <T> int add(T target);

    /**
     * 批量插入数据
     *
     * @param targetList 数据集
     */
    <T> int addBatch(List<T> targetList);

    /**
     * 替换数据
     *
     * @param target 数据
     */
    <T> int replace(T target);

    /**
     * 批量替换数据
     *
     * @param targetList 数据集
     */
    <T> int replaceBatch(List<T> targetList);

    /**
     * 批量保存数据
     *
     * @param targetList 数据集
     */
    <T> int saveBatch(List<T> targetList);

    /**
     * 删除 （根据查询构建器）
     *
     * @param targetClass  目标类型
     * @param queryWrapper 查询构建器
     */
    <T> int delete(Class<T> targetClass, QueryWrapper queryWrapper);

    /**
     * 删除 （根据主键）
     *
     * @param targetClass 目标类型
     * @param id          主键
     */
    <T> int deleteById(Class<T> targetClass, Integer id);

    /**
     * 更新
     *
     * @param targetClass  目标类型
     * @param setWrapper   更新构建器
     * @param queryWrapper 查询构建器
     */
    <T> int update(Class<T> targetClass, SetWrapper setWrapper, QueryWrapper queryWrapper);

    /**
     * 自定义 执行sql
     *
     * @param sql     sql语句
     * @param setSqls ?设置，预编译
     */
    <T> boolean exeSQL(String sql, Object[] setSqls);

    /**
     * 执行 自定义sql查询
     *
     * @param sql         要执行的sql
     * @param setSqls     ?设置，预编译
     * @param targetClass 目标类型
     * @return 结果集
     */
    <T> ArrayList<T> selectSQL(String sql, Object[] setSqls, Class<T> targetClass);

}
