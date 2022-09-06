package com.wen.netdisc.user.api.converter;

import com.wen.netdisc.common.pojo.Bulletin;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.user.api.service.UserService;
import com.wen.netdisc.user.api.vo.BulletinVO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BulletinConverter implements Converter<Bulletin, BulletinVO> {
    @Resource
    UserService userService;

    public List<BulletinVO> list(List<Bulletin> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public BulletinVO convert(Bulletin model) {
        BulletinVO vo = new BulletinVO();
        BeanUtils.copyProperties(model, vo);
        Integer level = model.getLevel();
        if (level != null && level == 3) {
            vo.setLevel("high");
        } else if (level != null && level == 2) {
            vo.setLevel("mid");
        } else {
            vo.setLevel("low");
        }
        User user = userService.getUserById(model.getUserId());
        if (Optional.ofNullable(user).isPresent()) {
            vo.setUserName(user.getUserName());
        }
        return vo;
    }
}
