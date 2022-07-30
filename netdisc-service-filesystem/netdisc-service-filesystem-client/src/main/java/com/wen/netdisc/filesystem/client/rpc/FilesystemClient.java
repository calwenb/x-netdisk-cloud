package com.wen.netdisc.filesystem.client.rpc;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "netdisc-filesystem", path = "/rpc/filesystems")
public interface FilesystemClient extends FileResource {

}
