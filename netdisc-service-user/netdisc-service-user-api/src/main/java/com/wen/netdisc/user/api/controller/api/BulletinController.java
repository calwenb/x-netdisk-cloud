package com.wen.netdisc.user.api.controller.api;

import com.wen.netdisc.common.pojo.Bulletin;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.user.api.dto.BulletinDto;
import com.wen.netdisc.user.api.dto.BulletinFindDto;
import com.wen.netdisc.user.api.service.BulletinService;
import com.wen.netdisc.user.api.vo.BulletinVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author calwen
 * @since 2022/9/6
 */
@RestController
@RequestMapping("/bulletins")
public class BulletinController {
    @Resource
    BulletinService service;

    @GetMapping("/list")
    public ResultVO<List<BulletinVO>> list(@Valid @RequestBody BulletinFindDto findDto) {
        service.list(findDto);
        return null;
    }

    @GetMapping("/{id}")
    public ResultVO<BulletinVO> get(@PathVariable Integer id) {
        service.get(id);
        return null;
    }

    @DeleteMapping("/{id}")
    public ResultVO<BulletinVO> del(@PathVariable Integer id) {
        return service.del(id) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

    @PostMapping("")
    public ResultVO<Bulletin> add(@RequestBody BulletinDto bulletinDto) {
        return ResultUtil.success(service.add(bulletinDto));
    }

    @PutMapping("")
    public ResultVO<Bulletin> update(@RequestBody BulletinDto bulletinDto) {
        return ResultUtil.success(service.update(bulletinDto));
    }

}
