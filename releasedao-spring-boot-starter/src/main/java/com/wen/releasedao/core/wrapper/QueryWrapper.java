package com.wen.releasedao.core.wrapper;

import com.wen.releasedao.core.enums.OperatEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Where包装器类
 * 构建解析查询sql
 *
 * @author calwen
 * @since 2022/7/9
 */
public class QueryWrapper extends AbstractWrapper implements Wrapper {
    /**
     * 需要where 作为前缀拼接
     */
    private static final HashSet<OperatEnum> WHERE_SET;

    static {
        WHERE_SET = new HashSet<>();
        WHERE_SET.add(OperatEnum.EQ);
        WHERE_SET.add(OperatEnum.NOT_EQ);
        WHERE_SET.add(OperatEnum.GREATER);
        WHERE_SET.add(OperatEnum.LESS);
        WHERE_SET.add(OperatEnum.G_EQ);
        WHERE_SET.add(OperatEnum.L_EQ);
        WHERE_SET.add(OperatEnum.LIKE);
        WHERE_SET.add(OperatEnum.LIKE_LEFT);
        WHERE_SET.add(OperatEnum.LIKE_RIGHT);
        WHERE_SET.add(OperatEnum.IN);
        WHERE_SET.add(OperatEnum.IS_NULL);
        WHERE_SET.add(OperatEnum.ONT_NULL);

    }

    private String selectField;

    public String getSelectField() {
        return selectField;
    }

    /**
     * 指定字段查询
     */
    public QueryWrapper select(String selectField) {
        this.selectField = selectField;
        return this;
    }


    /**
     * 解析 构造 sql，预编译值
     *
     * @author calwen
     * @since 2022/7/15
     */
    @Override
    public HashMap<String, Object> getResult() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        ArrayList<Node> whereList = getWhereList();
        ArrayList<Object> setList = new ArrayList<>();
        StringBuffer whereSql = new StringBuffer();
        for (Node node : whereList) {
            OperatEnum operating = node.getOperating();
            String field = node.getField();
            Object value = node.getValue();
            if (WHERE_SET.contains(operating)) {
                int len = whereSql.length();
                if (len == 0) {
                    whereSql.append(" WHERE ");
                } else {
                    if (!"OR".equals(whereSql.substring(len - 3, len - 1))) {
                        whereSql.append(" AND ");
                    }
                }
            }
            switch (operating) {
                case EQ:
                    whereSql.append(" `").append(field).append("` ").append(" = ? ");
                    setList.add(value);
                    break;
                case IN:
                    whereSql.append(" `").append(field).append("` ").append(" IN ( ");
                    Object[] inValues = (Object[]) value;
                    for (int j = 0; j < inValues.length; j++) {
                        if (j != 0) {
                            whereSql.append(" , ");
                        }
                        whereSql.append(" ? ");
                        setList.add(inValues[j]);
                    }
                    whereSql.append(" ) ");
                    break;
                case NOT_EQ:
                    whereSql.append(" `").append(field).append("` ").append(" <> ? ");
                    setList.add(value);
                    break;
                case GREATER:
                    whereSql.append(" `").append(field).append("` ").append(" > ? ");
                    setList.add(value);
                    break;
                case LESS:
                    whereSql.append(" `").append(field).append("` ").append(" < ? ");
                    setList.add(value);
                    break;
                case G_EQ:
                    whereSql.append(" `").append(field).append("` ").append(" >=? ");
                    setList.add(value);
                    break;
                case L_EQ:
                    whereSql.append(" `").append(field).append("` ").append(" <= ? ");
                    setList.add(value);
                    break;
                case IS_NULL:
                    whereSql.append(" `").append(field).append("` ").append(" IS NULL ");
                    break;
                case ONT_NULL:
                    whereSql.append(" `").append(field).append("` ").append(" IS NOT NULL ");
                    break;
                case OR:
                    whereSql.append(" OR ");
                    break;
                case HEAD:
                    whereSql.append(" WHERE ").append(" `").append(field).append("` ").append(" = ? ");
                    setList.add(value);
                    break;
                case LIKE:
                    whereSql.append(" CONCAT( ").append(field).append(" ) ").append(" LIKE '%").append(value).append("%' ");
                    break;
                case LIKE_LEFT:
                    whereSql.append(" CONCAT( ").append(field).append(" ) ").append(" LIKE '%").append(value).append("' ");
                    break;
                case LIKE_RIGHT:
                    whereSql.append(" CONCAT( ").append(field).append(" ) ").append(" LIKE '").append(value).append("%' ");
                    break;
                case ORDER:
                    whereSql.append(" ORDER BY ").append(field);
                    break;
                case ORDER_DESC:
                    whereSql.append(" ORDER BY ").append(field).append(" DESC ");
                    break;
                case LIMIT:
                    whereSql.append(" LIMIT ").append(field);
                    break;
                default:
                    whereSql.append(operating).append(" `").append(field).append("` ").append("= ? ");
                    setList.add(value);
            }

        }
        whereSql.append(";");
        map.put("sql", whereSql);
        map.put("values", setList);
        return map;
    }


    /**
     * 等于 <br>
     * 相当于 ’=‘
     */
    public QueryWrapper eq(String field, Object value) {
        Node node = new Node(OperatEnum.EQ, field, value);
        super.getWhereList().add(node);
        return this;
    }


    /**
     * 不等于 <br>
     * 相当于 ’<>‘
     */
    public QueryWrapper notEq(String field, Object value) {
        Node node = new Node(OperatEnum.NOT_EQ, field, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 大于 <br>
     * 相当于 ’>‘
     */
    public QueryWrapper greater(String field, Object value) {
        Node node = new Node(OperatEnum.GREATER, field, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 小于<br>
     * 相当于 ’<‘
     */
    public QueryWrapper less(String field, Object value) {
        Node node = new Node(OperatEnum.LESS, field, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 大于等于 <br>
     * 相当于 '>='
     */
    public QueryWrapper gEq(String field, Object value) {
        Node node = new Node(OperatEnum.G_EQ, field, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 小于等于 <br>
     * 相当于 '<='
     */
    public QueryWrapper lEq(String field, Object value) {
        Node node = new Node(OperatEnum.L_EQ, field, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 范围匹配 <br>
     * 相当于 ’IN‘
     */
    public QueryWrapper in(String field, Object... inValues) {
        Node node = new Node(OperatEnum.IN, field, inValues);
        super.getWhereList().add(node);
        return this;
    }


    /**
     * 模糊匹配 <br>
     * 相当于 " LIKE '% ? %' "
     */
    public QueryWrapper like(String fields, Object value) {
        Node node = new Node(OperatEnum.LIKE, fields, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 左模糊匹配 <br>
     * 相当于 " LIKE '% ?' "
     */
    public QueryWrapper likeLeft(String fields, Object value) {
        Node node = new Node(OperatEnum.LIKE_LEFT, fields, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 右模糊 <br>
     * 相当于 " LIKE ' ?%' "
     */
    public QueryWrapper likeRight(String fields, Object value) {
        Node node = new Node(OperatEnum.LIKE_RIGHT, fields, value);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 或者 <br>
     * 相当于 ’OR‘
     */
    public QueryWrapper or() {
        Node node = new Node(OperatEnum.OR, null, null);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 分页
     *
     * @param offset 偏移量
     * @param rows   记录数
     */
    public QueryWrapper limit(int offset, int rows) {
        Node node = new Node(OperatEnum.LIMIT, offset + "," + rows, null);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 分页
     *
     * @param rows 记录数
     */
    public QueryWrapper limit(int rows) {
        Node node = new Node(OperatEnum.LIMIT, String.valueOf(rows), null);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 根据字段倒序排序  <br>
     * 相当于 'ORDER BY ? DESC'
     */
    public QueryWrapper orderDesc(String fields) {
        Node node = new Node(OperatEnum.ORDER_DESC, fields, null);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 根据字段升序排序 <br>
     * 相当于 'ORDER BY'
     */
    public QueryWrapper order(String fields) {
        Node node = new Node(OperatEnum.ORDER, fields, null);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 不为空 <br>
     * 相当于 'IS NOT NULL'
     */
    public QueryWrapper notNull(String filed) {
        Node node = new Node(OperatEnum.ONT_NULL, filed, null);
        super.getWhereList().add(node);
        return this;
    }

    /**
     * 为空 <br>
     * 相当于 'NOT NULL'
     */
    public QueryWrapper isNUll(String filed) {
        Node node = new Node(OperatEnum.IS_NULL, filed, null);
        super.getWhereList().add(node);
        return this;
    }


}
