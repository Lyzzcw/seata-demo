package com.lzy.seata.service.impl;

import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.entity.User;
import com.lzy.seata.mapper.UserMapper;
import com.lzy.seata.service.TCCService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/28
 * Time: 13:45
 * Description: No Description
 */
@Service
public class TCCServiceImpl implements TCCService {

    @Resource
    private UserMapper UserMapper;

    @Transactional
    @Override
    public String frozen(String userId, Long money) {
        User User = UserMapper.selectByUserId(userId);
        if (Objects.isNull(User))
            return FeignCodes.ERROR.getStatus();
        if (User.getMoney()<money)
            return FeignCodes.ERROR.getStatus();
        User.setMoney(User.getMoney()-money);
        User.setFrozen(User.getFrozen()+money);
        UserMapper.updateByPrimaryKeySelective(User);
        return FeignCodes.SUCCESS.getStatus();
    }

    @Transactional
    @Override
    public String cancelFrozen(String userId, Long money) {
        User User = UserMapper.selectByUserId(userId);
        if (Objects.isNull(User))
            return FeignCodes.ERROR.getStatus();
        User.setMoney(User.getMoney()+money);
        User.setFrozen(User.getFrozen()-money);
        UserMapper.updateByPrimaryKeySelective(User);
        return FeignCodes.SUCCESS.getStatus();
    }

    @Transactional
    @Override
    public String commitFrozen(String userId, Long money) {
        User User = UserMapper.selectByUserId(userId);
        if (Objects.isNull(User))
            return FeignCodes.ERROR.getStatus();
        User.setFrozen(User.getFrozen()-money);
        UserMapper.updateByPrimaryKeySelective(User);
        return FeignCodes.SUCCESS.getStatus();
    }
}
