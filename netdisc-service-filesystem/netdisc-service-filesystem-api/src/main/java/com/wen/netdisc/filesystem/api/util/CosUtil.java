package com.wen.netdisc.filesystem.api.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import com.wen.netdisc.common.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author calwen
 * @since 2023/8/15
 */
@Slf4j
public class CosUtil {
    private static String bucketName = "XXX";

    // 创建 TransferManager 实例，这个实例用来后续调用高级接口
    private static volatile TransferManager transferManager = null;

    public static TransferManager getTransferManager() {
        if (transferManager == null) {
            synchronized (CosUtil.class) {
                if (transferManager == null) {
                    transferManager = new TransferManager(getClient(), ThreadPoolUtil.getThreadPool());
                    // 设置高级接口的配置项 分块上传阈值和分块大小分别为 5MB 和 1MB
                    TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
                    transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
                    transferManagerConfiguration.setMinimumUploadPartSize(1 * 1024 * 1024);
                    transferManager.setConfiguration(transferManagerConfiguration);
                }
            }
        }
        return transferManager;
    }

    private static COSClient getClient() {
        String secretId = "XXX";
        String secretKey = "XXX";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("XXX");
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    public void shutdownTransferManager() {
        transferManager.shutdownNow(true);
    }


    public static void upload(String key, InputStream input) throws InterruptedException {
        PutObjectRequest request = new PutObjectRequest(bucketName, key, input, new ObjectMetadata());
        Upload upload = getTransferManager().upload(request);
        UploadResult uploadResult = upload.waitForUploadResult();
        log.info("[CosUtil] upload key = {} , requestId = {} ",  uploadResult.getKey(),uploadResult.getRequestId());
    }

    public static COSObjectInputStream download(String key) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        COSObject object = getClient().getObject(request);
        log.info("[CosUtil] download key = {} ", object.getKey());
        return object.getObjectContent();
    }

    public static void remove(String key) {
    }
}
