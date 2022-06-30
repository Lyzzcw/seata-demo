package com.lzy.seata.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 16:09
 * Description: No Description
 */
@Component
public class TCCOrderServiceImpl implements TCCOrderService{

    @Transactional
    @Override
    public boolean tryCreate(BusinessActionContext businessActionContext, String userId, String productId, Long num) {

        return false;
    }

    @Transactional
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        return false;
    }

    @Transactional
    @Override
    public boolean cancel(BusinessActionContext businessActionContext) {
        return false;
    }
}
