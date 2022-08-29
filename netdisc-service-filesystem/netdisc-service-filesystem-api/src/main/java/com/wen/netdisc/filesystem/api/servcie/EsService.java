package com.wen.netdisc.filesystem.api.servcie;

import com.wen.netdisc.common.pojo.MyFile;

import java.util.List;
import java.util.Map;


/**
 * ElasticSearch业务类
 * 对es进行数据同步操作，以及预热
 *
 * @author Mr.文
 */
public interface EsService {

    void addData(MyFile file);

    void updateData(MyFile file);

    void delDate(String id);

    List<Map<String, Object>> searchData(int storeId, String keyword);

    void esWarmUp();
}
