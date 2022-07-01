package com.lzy.seata;

import com.lzy.seata.entity.Order;
import com.lzy.seata.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/7/1
 * Time: 17:41
 * Description: No Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LzyTest {
    @Resource
    private OrderMapper orderMapper;

    @Test
    public void test(){
        Order order = orderMapper.findByOrderId("1dxuacYXQWNo5uLFViWK3");
        System.out.println(order);
    }
}
