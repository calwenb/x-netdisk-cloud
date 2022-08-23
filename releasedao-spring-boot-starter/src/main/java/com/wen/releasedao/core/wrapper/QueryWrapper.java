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
    private static final HashSet<OperatEnum> needWhereSet;

    static {
        needWhereSet = new HashSet<>();
        needWhereSet.add(OperatEnum.EQ);
        needWhereSet.add(OperatEnum.EQS);
        needWhereSet.add(OperatEnum.NOT_EQ);
        needWhereSet.add(OperatEnum.GREATER);
        needWhereSet.add(OperatEnum.LESS);
        needWhereSet.add(OperatEnum.G_EQ);
        needWhereSet.add(OperatEnum.L_EQ);
        needWhereSet.add(OperatEnum.LIKE);
        needWhereSet.add(OperatEnum.LIKE_LEFT);
        needWhereSet.add(OperatEnum.LIKE_RIGHT);
        needWhereSet.add(OperatEnum.IN);
        needWhereSet.add(OperatEnum.IS_NULL);
        needWhereSet.add(OperatEnum.ONT_NULL);

    }

    private String selectField;

    public String getSelectField() {
        return selectField;
    }

    /**
     * 指定字段查询
     */
    public QueryWrapper select(String SelectField) {
        this.selectField = SelectField;
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
        StringBuffer whereSQL = new StringBuffer();
        for (Node node : whereList) {
            OperatEnum operating = node.getOperating();
            String field = node.getField();
            Object value = node.getValue();
            if (needWhereSet.contains(operating)) {
                int len = whereSQL.length();
                if (len == 0) {
                    whereSQL.append(" WHERE ");
                } else {
                    if (!"OR".equals(whereSQL.substring(len - 3, len - 1))) {
                        whereSQL.append(" AND ");
                    }
                }
            }
            switch (operating) {
                case EQ:
                    whereSQL.append(" `").append(field).append("` ").append(" = ? ");
                    setList.add(value);
                    break;
                case IN:
                    whereSQL.append(" `").append(field).append("` ").append(" IN ( ");
                    Object[] inValues = (Object[]) value;
                    for (int j = 0; j < inValues.length; j++) {
                        if (j != 0) {
                            whereSQL.append(" , ");
                        }
                        whereSQL.append(" ? ");
                        setList.add(inValues[j]);
                    }
                    whereSQL.append(" ) ");
                    break;
                case NOT_EQ:
                    whereSQL.append(" `").append(field).append("` ").append(" <> ? ");
                    setList.add(value);
                    break;
                case GREATER:
                    whereSQL.append(" `").append(field).append("` ").append(" > ? ");
                    setList.add(value);
                    break;
                case LESS:
                    whereSQL.append(" `").append(field).append("` ").append(" < ? ");
                    setList.add(value);
                    break;
                case G_EQ:
                    whereSQL.append(" `").append(field).append("` ").append(" >=? ");
                    setList.add(value);
                    break;
                case L_EQ:
                    whereSQL.append(" `").append(field).append("` ").append(" <= ? ");
                    setList.add(value);
                    break;
                case IS_NULL:
                    whereSQL.append(" `").append(field).append("` ").append(" IS NULL ");
                    break;
                case ONT_NULL:
                    whereSQL.append(" `").append(field).append("` ").append(" IS NOT NULL ");
                    break;
                case OR:
                    whereSQL.append(" OR ");
                    break;
                case HEAD:
                    whereSQL.append(" WHERE ").append(" `").append(field).append("` ").append(" = ? ");
                    setList.add(value);
                    break;
                case LIKE:
                    whereSQL.append(" CONCAT( ").append(field).append(" ) ").append(" LIKE '%").append(value).append("%' ");
                    break;
                case LIKE_LEFT:
                    whereSQL.append(" CONCAT( ").append(field).append(" ) ").append(" LIKE '%").append(value).append("' ");
                    break;
                case LIKE_RIGHT:
                    whereSQL.append(" CONCAT( ").append(field).append(" ) ").append(" LIKE '").append(value).append("%' ");
                    break;
                case ORDER:
                    whereSQL.append(" ORDER BY ").append(field);
                    break;
                case ORDER_DESC:
                    whereSQL.append(" ORDER BY ").append(field).append(" DESC ");
                    break;
                case LIMIT:
                    whereSQL.append(" LIMIT ").append(field);
                    break;
                default:
                    whereSQL.append(operating).append(" `").append(field).append("` ").append("= ? ");
                    setList.add(value);
            }

        }
        whereSQL.append(";");
        map.put("sql", whereSQL);
        map.put("setSQL", setList);
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
