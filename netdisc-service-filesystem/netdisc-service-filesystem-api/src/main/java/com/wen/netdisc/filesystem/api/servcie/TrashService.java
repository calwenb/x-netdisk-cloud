package com.wen.netdisc.filesystem.api.servcie;

import com.wen.netdisc.common.pojo.MyFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * TrashService业务接口
 *
 * @author calwen
 */
public interface TrashService {


//    List<MyFile> queryTrashs(int userId, int parentFolderId, int pageNum);

    List<MyFile> getListByUid(int userId, int pageNum);


    //    List<Map<String, String>> queryTrashsByUid(int userId, int pageNum, boolean preview) throws IOException;
    Boolean addTrash(MyFile trash, int uid);

    boolean deleteById(MyFile trash, int uid);

    boolean restored(MyFile trash, int uid);

    /**
     * 倒垃圾
     *
     * @author calwen
     * @since 2022/7/11
     */
    int takeOutTrash();

    /**
     * 文件下载业务
     */


    ResponseEntity<InputStreamResource> getData(MyFile trash);
}
