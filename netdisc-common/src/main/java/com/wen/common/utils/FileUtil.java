package com.wen.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * FileUtil类
 * @author Mr.文
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
    /**
     * 仓库根文件夹ID
     */
    public static int STORE_ROOT_ID;
    public static long STORE_MAX_SIZE;
    private static Map<String, String> fileTypeMap = new HashMap<>();


    static {
        String textType = ".txt .doc .docx .xls .xlsx .csv .PPTX .md";
        String codeType = ".c .cpp .java .py .sql .html .js .css .vue .json .xml .yml .jsp";
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

    public static boolean checkFileSize(MultipartFile file) {
        long size = file.getSize();
        if (size <= FILE_MAX_SIZE) {
            return true;
        }
        return false;
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
    @Value("${netdisc.file.max-size}")
    public void setFileMaxSize(long fileMaxSize) {
        FILE_MAX_SIZE = fileMaxSize * 1024 * 1024;
    }

    @Value("${netdisc.file.show-row}")
    public void setFileShowRow(int fileShowRow) {
        FILE_SHOW_ROW = fileShowRow;
    }

    @Value("${netdisc.store.root-path}")
    public void setStoreRootPath(String storeRootPath) {
        STORE_ROOT_PATH = storeRootPath;
    }

    @Value("${netdisc.store.root-id}")
    public void setStoreRootId(int storeRootId) {
        STORE_ROOT_ID = storeRootId;
    }

    @Value("${netdisc.store.max-size}")
    public void setStoreMaxSize(long storeMaxSize) {
        STORE_MAX_SIZE = storeMaxSize * 1024L * 1024;
    }

}
