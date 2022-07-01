package com.lzy.seata.service.impl;

import com.lzy.seata.constant.FeignCodes;
import com.lzy.seata.entity.Store;
import com.lzy.seata.mapper.StoreMapper;
import com.lzy.seata.service.ATService;
import com.lzy.seata.service.TCCService;
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
public class TCCServiceImpl implements TCCService {

    @Resource
    private StoreMapper storeMapper;

    @Transactional
    @Override
    public String frozen(Long productId, Long num) {
        Store store = storeMapper.findByProductId(productId);
        if (Objects.isNull(store))
            return FeignCodes.ERROR.getStatus();
        if (store.getNum()<num)
            return FeignCodes.ERROR.getStatus();
        store.setNum(store.getNum()-num);
        store.setFrozen(store.getFrozen()+num);
        storeMapper.updateByPrimaryKeySelective(store);
        return FeignCodes.SUCCESS.getStatus();
    }

    @Transactional
    @Override
    public String cancelFrozen(Long productId, Long num) {
        Store store = storeMapper.findByProductId(productId);
        if (Objects.isNull(store))
            return FeignCodes.ERROR.getStatus();
        store.setNum(store.getNum()+num);
        store.setFrozen(store.getFrozen()-num);
        storeMapper.updateByPrimaryKeySelective(store);
        return FeignCodes.SUCCESS.getStatus();
    }

    @Transactional
    @Override
    public String commitFrozen(Long productId, Long num) {
        Store store = storeMapper.findByProductId(productId);
        if (Objects.isNull(store))
            return FeignCodes.ERROR.getStatus();
        store.setFrozen(store.getFrozen()-num);
        storeMapper.updateByPrimaryKeySelective(store);
        return FeignCodes.SUCCESS.getStatus();
    }
}
