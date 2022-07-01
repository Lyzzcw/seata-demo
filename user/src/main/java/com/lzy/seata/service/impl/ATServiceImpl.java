package com.lzy.seata.service.impl;

import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.entity.User;
import com.lzy.seata.mapper.UserMapper;
import com.lzy.seata.service.ATService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 13:45
 * Description: No Description
 */
@Service
public class ATServiceImpl implements ATService {

    @Resource
    private UserMapper userMapper;

    @Transactional
    @Override
    public String deduct(String userId, Long money) {
        User user = userMapper.selectByUserId(userId);
        Assert.notNull(user,"用户不存在");
        user.setMoney(user.getMoney() - money);
        userMapper.updateByPrimaryKey(user);
        return FeignCodes.SUCCESS.getStatus();
    }
}
