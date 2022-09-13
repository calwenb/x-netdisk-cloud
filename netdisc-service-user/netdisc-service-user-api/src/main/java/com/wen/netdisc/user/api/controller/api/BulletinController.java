package com.wen.netdisc.user.api.controller.api;

import com.wen.netdisc.common.annotation.PassAuth;
import com.wen.netdisc.common.pojo.Bulletin;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.user.api.converter.BulletinConverter;
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
    @Resource
    BulletinConverter converter;

    @PassAuth
    @GetMapping("/list")
    public ResultVO<List<BulletinVO>> list(@Valid @RequestBody BulletinFindDto findDto) {
        return ResultUtil.success(converter.list(service.list(findDto)));
    }

    @PassAuth
    @GetMapping("/{id}")
    public ResultVO<BulletinVO> get(@PathVariable Integer id) {
        return ResultUtil.success(converter.convert(service.get(id)));
    }

    @PassAuth
    @GetMapping("/need")
    public ResultVO<BulletinVO> need() {
        return ResultUtil.success(converter.convert(service.getNeed()));
    }


    @DeleteMapping("/{id}")
    public ResultVO<Object> del(@PathVariable Integer id) {
        return service.del(id) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }


    @PostMapping("")
    public ResultVO<Bulletin> save(@RequestBody BulletinDto bulletinDto) {
        return ResultUtil.success(service.save(bulletinDto));
    }


}
