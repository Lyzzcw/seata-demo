package com.lzy.seata.service.impl;

import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.entity.Result;
import com.lzy.seata.entity.Store;
import com.lzy.seata.mapper.StoreMapper;
import com.lzy.seata.service.ATService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
public class ATServiceImpl implements ATService {

    @Resource
    private StoreMapper storeMapper;

    @Transactional
    @Override
    public String deduct(Long productId, Long num) {
        Store store = storeMapper.findByProductId(productId);
        Assert.notNull(store,"物品不存在");
        store.setNum(store.getNum() - num);
        storeMapper.updateByPrimaryKey(store);
        return FeignCodes.SUCCESS.getStatus();
    }

    @Override
    public Store detail(Long productId) {
        return storeMapper.findByProductId(productId);
    }
    
}
