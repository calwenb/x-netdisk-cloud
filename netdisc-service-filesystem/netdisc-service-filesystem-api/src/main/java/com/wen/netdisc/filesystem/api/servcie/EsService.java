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

    /**
     * 添加数据到ElasticSearch
     *
     * @param file 要添加的文件
     */
    void addData(MyFile file);

    /**
     * 更新ElasticSearch中的数据
     *
     * @param file 要更新的文件
     */
    void updateData(MyFile file);

    /**
     * 从ElasticSearch中删除数据
     *
     * @param id 要删除的数据的ID
     */
    void delDate(String id);

    /**
     * 在ElasticSearch中搜索数据
     *
     * @param storeId 店铺ID
     * @param keyword 关键字
     * @return 搜索结果列表
     */
    List<Map<String, Object>> searchData(int storeId, String keyword);

    /**
     * 预热ElasticSearch
     */
    void esWarmUp();
}
