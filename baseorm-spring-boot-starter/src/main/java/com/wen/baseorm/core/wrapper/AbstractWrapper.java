package com.wen.baseorm.core.wrapper;

import com.wen.baseorm.core.enums.OperatEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 抽象wrapper类
 * 1.
 * Node数据结构：
 * { operating：包装操作指令，field：字段，value：预编译值 }
 * 2.
 * whereList：保存各个Node，以便解析
 * 3.
 * getResult()抽象方法：将whereList解析成sql
 *
 * @author calwen
 * @since 2022/7/9
 */
public abstract class AbstractWrapper {

    //WhereNode的内部类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        private OperatEnum operating;
        private String field;
        private Object value;
    }

    private final ArrayList<Node> whereList;

    public AbstractWrapper() {
        whereList = new ArrayList<>();
    }


    public AbstractWrapper add(String field, Object value) {
        Node node = new Node(OperatEnum.HEAD, field, value);
        whereList.add(node);
        return this;
    }


    public AbstractWrapper and(String field, Object value) {
        Node node = new Node(OperatEnum.AND, field, value);
        whereList.add(node);
        return this;
    }

/*    public AbstractWrapper or(String field, Object value) {
        Node node = new Node(OperatEnum.OR, field, value);
        whereList.add(node);
        return this;
    }

    public AbstractWrapper or() {
        Node node = new Node(OperatEnum.OR,null,null);
        whereList.add(node);
        return this;
    }*/

    public ArrayList<Node> getWhereList() {
        return whereList;
    }

    /**
     * 抽象方法
     * 解析包装器数据结构返回sql语句和值列表
     *
     * @return
     */
    public abstract HashMap<String, Object> getResult();

}
