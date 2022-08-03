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
    boolean addData(List<MyFile> list);

    boolean addData(MyFile file);

    boolean updateData(MyFile file);

    boolean delDate(String id);

    List<Map<String, Object>> searchData(int storeId, String keyword);

    boolean esWarmUp();
}
