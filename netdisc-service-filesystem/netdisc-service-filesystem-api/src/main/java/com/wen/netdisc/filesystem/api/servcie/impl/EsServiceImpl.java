package com.wen.netdisc.filesystem.api.servcie.impl;

import com.alibaba.fastjson.JSON;
import com.wen.netdisc.common.util.LoggerUtil;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.filesystem.api.mapper.MyFileMapper;
import com.wen.netdisc.filesystem.api.servcie.EsService;
import com.wen.netdisc.filesystem.api.util.ConfigUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class EsServiceImpl implements EsService {
    @Resource
    @Qualifier("restHighLevelClient")
    RestHighLevelClient client;
    @Resource
    MyFileMapper fileMapper;
    @Resource
    ConfigUtil configUtil;

    @Override
    public List<Map<String, Object>> searchData(int storeId, String keyword) {
        LinkedList<Map<String, Object>> list = new LinkedList<>();
        SearchRequest request = new SearchRequest(configUtil.getEsIndex());

        //两个条件 根据用户仓库 以及文件名 搜索
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("fileStoreId", storeId));
        boolQuery.must(QueryBuilders.matchQuery("myFileName", keyword));
        sourceBuilder.query(boolQuery);
        request.source(sourceBuilder);
        SearchResponse resp;
        try {
            resp = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("es服务错误");
        }
        for (SearchHit hit : resp.getHits().getHits()) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

    @Async
    @Override
    public void addData(MyFile file) {
        IndexRequest request = new IndexRequest(configUtil.getEsIndex());
        request.id(String.valueOf(file.getMyFileId()));
        request.timeout("1m");
        request.source(JSON.toJSONString(file), XContentType.JSON);
        client.indexAsync(request, RequestOptions.DEFAULT, null);
    }

    @Async
    @Override
    public void updateData(MyFile file) {
        UpdateRequest request = new UpdateRequest(configUtil.getEsIndex(), String.valueOf(file.getMyFileId()));
        request.timeout("5s");
        request.doc(JSON.toJSONString(file), XContentType.JSON);
        client.updateAsync(request, RequestOptions.DEFAULT, null);
    }

    @Async
    @Override
    public void delDate(String id) {
        DeleteRequest request = new DeleteRequest(configUtil.getEsIndex(), id);
        request.timeout("5s");
        client.deleteAsync(request, RequestOptions.DEFAULT, null);
    }

    @Override
    public void esWarmUp() {
        LoggerUtil.info("开始预热Elasticsearch", EsServiceImpl.class);
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(configUtil.getEsIndex());
        try {
            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("ES预热失败");
        }
        List<MyFile> files = fileMapper.queryAllFiles();
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("5s");
        for (MyFile file : files) {
            bulkRequest.add(
                    new IndexRequest(configUtil.getEsIndex())
                            .id(String.valueOf(file.getMyFileId()))
                            .source(JSON.toJSONString(file), XContentType.JSON)
            );
        }
        BulkResponse resp;
        try {
            resp = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        resp.hasFailures();
    }


}
