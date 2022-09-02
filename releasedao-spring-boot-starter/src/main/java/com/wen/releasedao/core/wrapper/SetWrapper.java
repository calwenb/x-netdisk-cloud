package com.wen.releasedao.core.wrapper;

import com.wen.releasedao.core.enums.OperatEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * set操作包装器类
 * 构建解析查询sql
 *
 * @author calwen
 * @since 2022/7/9
 */
public class SetWrapper extends AbstractWrapper implements Wrapper {

    /**
     * 构建更新操作sql
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
            switch (operating) {
                case HEAD:
                    whereSql.append(" SET ")
                            .append(" `").append(field).append("` ").append("= ? ");
                    break;
                default:
                    whereSql.append(operating).append(" `").append(field).append("` ").append("= ? ");
            }
            setList.add(value);
        }
        map.put("sql", whereSql);
        map.put("values", setList);
        return map;
    }
}
