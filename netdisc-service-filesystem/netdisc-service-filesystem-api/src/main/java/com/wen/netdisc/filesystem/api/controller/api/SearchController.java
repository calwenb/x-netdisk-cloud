package com.wen.netdisc.filesystem.api.controller.api;

import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.servcie.EsService;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * SearchController类
 *
 * @author Mr.文
 */
@RestController
@RequestMapping("/searchs")
public class SearchController extends BaseController {
    @Resource
    EsService esService;

    @GetMapping("/{key}")
    public ResultVO<List<Map<String, Object>>> searchData(@PathVariable("key") String keyword)  {
        Integer uid = UserUtil.getUid();
        int storeId = storeService.queryStoreByUid(uid).getFileStoreId();
        List<Map<String, Object>> data = esService.searchData(storeId, keyword);
        return ResultUtil.success(data);
    }
}
