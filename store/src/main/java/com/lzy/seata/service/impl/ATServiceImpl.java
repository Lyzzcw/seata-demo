package com.lzy.seata.service.impl;

import com.lzy.seata.entity.Store;
import com.lzy.seata.mapper.StoreMapper;
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
    private StoreMapper storeMapper;

    @Transactional
    @Override
    public void deduct(Long id, Long num) {
        Store store = storeMapper.selectByPrimaryKey(id);
        Assert.notNull(store,"物品不存在");
        store.setNum(store.getNum() - num);
        storeMapper.updateByPrimaryKey(store);
    }

    @Override
    public Store detail(Long id) {
        return storeMapper.selectByPrimaryKey(id);
    }
}
