package com.wen.releasedao.core.mapper;

import com.wen.releasedao.core.wrapper.QueryWrapper;
import com.wen.releasedao.core.wrapper.SetWrapper;

import java.util.List;

/**
 * 封装基本CRUD <br>
 * core<br>
 * <b>  eClass 为实体类型</b>
 *
 * @author calwen
 * @since 2022/7/9
 */
public interface BaseMapper {

    /**
     * 查询
     *
     * @param eClass 实体类型
     */
    <T> T get(Class<T> eClass);

    /**
     * 查询（通过构建器）
     *
     * @param eClass  实体类型
     * @param wrapper 构建器
     */
    <T> T get(Class<T> eClass, QueryWrapper wrapper);


    /**
     * 查询（通过id）
     *
     * @param eClass 实体类型
     * @param id     主键
     */
    <T> T getById(Class<T> eClass, Object id);

    /**
     * 查询列表
     *
     * @param eClass 实体类型
     * @return 结果集
     */
    <T> List<T> getList(Class<T> eClass);

    /**
     * 查询列表 （查询构造器）
     *
     * @param eClass       实体类型
     * @param queryWrapper 查询构造器
     * @return 结果集
     */
    <T> List<T> getList(Class<T> eClass, QueryWrapper queryWrapper);


    /**
     * 返回匹配实体表数据行数
     * count(*)
     *
     * @param eClass 实体类型
     * @return count(*)
     */
    <T> int getCount(Class<T> eClass);

    /**
     * 返回匹配指定条件的行数
     * count(*)
     *
     * @param eClass       实体类型
     * @param queryWrapper 查询构造器
     * @return count(*)
     */
    <T> int getCount(Class<T> eClass, QueryWrapper queryWrapper);

    /**
     * 保存 数据<br>
     * entity会更新成数据库数据
     *
     * @param entity 数据
     */
    <T> boolean save(T entity);

    /**
     * 插入 数据<br>
     * entity会更新成数据库数据
     *
     * @param entity 数据
     */
    <T> boolean add(T entity);

    /**
     * 批量插入数据
     *
     * @param entityList 数据集
     */
    <T> boolean addBatch(List<T> entityList);

    /**
     * 替换数据<br>
     * entity会更新成数据库数据
     *
     * @param entity 数据
     */
    <T> boolean replace(T entity);

    /**
     * 批量替换数据
     *
     * @param entityList 数据集
     */
    <T> boolean replaceBatch(List<T> entityList);

    /**
     * 批量保存数据
     *
     * @param entityList 数据集
     */
    <T> boolean saveBatch(List<T> entityList);

    /**
     * 删除 （根据查询构建器）
     *
     * @param eClass       实体类型
     * @param queryWrapper 查询构建器
     */
    <T> boolean delete(Class<T> eClass, QueryWrapper queryWrapper);

    /**
     * 删除 （根据主键）
     *
     * @param eClass 实体类型
     * @param id     主键
     */
    <T> boolean deleteById(Class<T> eClass, Integer id);

    /**
     * 更新
     *
     * @param eClass       实体类型
     * @param setWrapper   更新构建器
     * @param queryWrapper 查询构建器
     */
    <T> boolean update(Class<T> eClass, SetWrapper setWrapper, QueryWrapper queryWrapper);

    /**
     * 自定义 执行sql
     *
     * @param sql    sql语句
     * @param values ?设置，预编译
     */
    <T> boolean exeSQL(String sql, Object[] values);

    /**
     * 执行 自定义sql查询
     *
     * @param sql    要执行的sql
     * @param values ?设置，预编译
     * @param eClass 实体类型
     * @return 结果集
     */
    <T> List<T> selectSQL(Class<T> eClass, String sql, Object[] values);

}
