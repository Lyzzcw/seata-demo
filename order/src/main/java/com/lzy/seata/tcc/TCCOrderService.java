package com.lzy.seata.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 11:20
 * Description: TCC 事务模型
 */
@LocalTCC
public interface TCCOrderService {

    /**
     *  @LocalTCC：该注解开启TCC事务
     *  @TwoPhaseBusinessAction：该注解标注在try方法上，其中的三个属性如下：
     * name：TCC事务的名称，必须是唯一的
     * commitMethod：confirm方法的名称，默认是commit
     * rollbackMethod：cancel方法的名称，，默认是rollback
     * confirm和cancel的返回值尤为重要，返回false则会不断的重试。
     */

    /*
        一阶段的try方法，用于资源的预留
        该注解@TwoPhaseBusinessAction:用于设置参数到businessActionContext中，这样二阶段commit和cancel方法便能够获取参数
     */
    @TwoPhaseBusinessAction(name = "orderTcc",commitMethod = "commit",rollbackMethod = "cancel")
    boolean tryCreate(BusinessActionContext businessActionContext,
                      @BusinessActionContextParameter(paramName = "userId") String userId,
                      @BusinessActionContextParameter(paramName = "productId") Long productId,
                      @BusinessActionContextParameter(paramName = "orderId") String orderId,
                      @BusinessActionContextParameter(paramName = "num") Long num,
                      @BusinessActionContextParameter(paramName = "money") Long money);

    /*
        二阶段的commit方法，用于事务的提交
        businessActionContext 获取一阶段的入参
        返回ture表示成功执行，false执行失败，Seata默认会不断重试，直至成功
     */
    boolean commit(BusinessActionContext businessActionContext);

    /*
        二阶段的cancel方法，用于事务的回滚
        businessActionContext 获取一阶段的入参
        返回ture表示成功执行，false执行失败，Seata默认会不断重试，直至成功
     */
    boolean cancel(BusinessActionContext businessActionContext);
}
