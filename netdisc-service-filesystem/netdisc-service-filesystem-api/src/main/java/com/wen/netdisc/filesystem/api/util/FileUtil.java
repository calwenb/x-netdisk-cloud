package com.wen.netdisc.filesystem.api.util;


import com.alibaba.fastjson.JSON;
import com.wen.commutil.util.ThreadPoolUtil;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.MyFile;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * FileUtil类
 *
 * @author calwen
 */
@Component
public class FileUtil {
    /**
     * 上传文件的大小最大2GB
     */
    private static long FILE_MAX_SIZE;
    /**
     * 文件展示的行数
     */
    public static int FILE_SHOW_ROW;
    public static String STORE_ROOT_PATH;
    public static String ROOT_PATH;
    /**
     * 仓库根文件夹ID
     */
    public static int STORE_ROOT_ID;
    /**
     * 仓库最大初始容量
     */
    public static long STORE_MAX_SIZE;
    private static final Map<String, String> fileTypeMap = new HashMap<>();


    static {
        String textType = ".txt .doc .docx .xls .xlsx .csv .PPTX .md";
        String codeType = ".c .cpp .java .py .go .sql .html .js .css .vue .json .xml .yml .jsp";
        String pictureType = ".bmp .gif .jpg .pic .png .tif .pdf .PNG";
        String compressionType = ".rar .zip";
        String soundType = ".wav .mp3";
        String videoType = ".flv .mp4/.m4v .rm/.rmvb ";
        fileTypeMap.put("文档", textType);
        fileTypeMap.put("代码", codeType);
        fileTypeMap.put("图片", pictureType);
        fileTypeMap.put("压缩包", compressionType);
        fileTypeMap.put("音频", soundType);
        fileTypeMap.put("视频", videoType);
    }

    public static String getTypeChinese(String typeE) {
        String rs = "";
        switch (typeE) {
            case "document":
                rs = "文档";
                break;
            case "code":
                rs = "代码";
                break;
            case "image":
                rs = "图片";
                break;
            case "compressedFile":
                rs = "压缩包";
                break;
            case "audio":
                rs = "音频";
                break;
            case "video":
                rs = "视频";
                break;
            default:
                rs = "其他";
        }
        return rs;
    }

    public static String getRootPath() {
        String storeRootPath = STORE_ROOT_PATH;
        ROOT_PATH = storeRootPath.substring(0, storeRootPath.lastIndexOf("store/"));
        return ROOT_PATH;
    }


    public static boolean checkFileSize(MultipartFile file) {
        long size = file.getSize();
        return size <= FILE_MAX_SIZE;
    }

    public static String getFileType(String suffixName) {
        for (Map.Entry<String, String> entry : fileTypeMap.entrySet()) {
            if (entry.getValue().contains(suffixName)) {
                return entry.getKey();
            }
        }
        return "其他";
    }

    /**
     * 单位 MB
     *
     * @param fileMaxSize 配置文件
     */
    @Value("${x-netdisc.file.max-size}")
    public void setFileMaxSize(long fileMaxSize) {
        FILE_MAX_SIZE = fileMaxSize * 1024 * 1024;
    }

    @Value("${x-netdisc.file.show-row}")
    public void setFileShowRow(int fileShowRow) {
        FILE_SHOW_ROW = fileShowRow;
    }

    @Value("${x-netdisc.store.root-path}")
    public void setStoreRootPath(String storeRootPath) {
        STORE_ROOT_PATH = storeRootPath;
    }

    @Value("${x-netdisc.store.root-id}")
    public void setStoreRootId(int storeRootId) {
        STORE_ROOT_ID = storeRootId;
    }

    @Value("${x-netdisc.store.max-size}")
    public void setStoreMaxSize(long storeMaxSize) {
        STORE_MAX_SIZE = storeMaxSize * 1024;
    }


    public static List<Map<String, String>> previewImage(List<MyFile> files) {
        //多线程处理缩略图，使用数组保证files顺序不变
        Map<String, String>[] rs = new Map[files.size()];
        CountDownLatch latch = new CountDownLatch(files.size());
        ThreadPoolExecutor threadPool = ThreadPoolUtil.getThreadPool();
        for (int i = 0; i < files.size(); i++) {
            int finalI = i;
            threadPool.execute(() -> {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                try {
                    Thumbnails.of(files.get(finalI).getMyFilePath())
                            .size(400, 400)
                            .outputFormat("jpg")
                            .toOutputStream(os);
                    byte[] bytes = os.toByteArray();
                    String base64Str = Base64.encodeBase64String(bytes);
                    HashMap<String, String> map = new HashMap<>(2);
                    map.put("msg", JSON.toJSONString(files.get(finalI)));
                    map.put("data", "data:image/jpg;base64," + base64Str);
                    rs[finalI] = map;
                } catch (Exception e) {
                    throw new FailException("生成缩略图失败");
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(rs);
    }


}
