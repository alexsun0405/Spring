package com.test;

import com.test.service.ServiceA;
import com.test.service.ServiceB;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-service.xml")
public class ServiceTest {

    @Autowired
    private ServiceA serviceA;
    @Autowired
    private ServiceB serviceB;

    /**
     * =================================================================================================================
     * methodA 调用 methodB, methodA methodB 使用同一个事务
     * 情况一 : methodB 抛出异常,  methodA 不做处理, 那么两个方法均回滚
     * 情况二 : methodA 捕捉 methodB 抛出的异常, 两个方法均回滚, 因为同一事务, 在 methodB 该事务已经被标记为 rollback-only
     * 情况三 : methodB 内部捕捉不抛出, 两个方法均不会回滚, Spring 是基于 AOP 在方法前后进行事务处理, 没有捕捉到异常则认为事务是成功的.
     * =================================================================================================================
     * methodA 调用 methodB, methodA 开启事务, methodB 开启新事务(Propagation.REQUIRES_NEW)
     * 情况一: methodB 抛出异常, 两个方法均回滚. Spring 在捕捉到异常后, 会继续抛出该异常. AbstractPlatformTransactionManager -> getTransaction()
     * 情况二: methodB 抛出异常, methodA 捕捉, methodB 回滚, methodA 不回滚.
     * 情况三: methodB 抛出异常, methodA 捕捉并抛出, 都回滚.
     * 情况四: methodB 捕捉不抛出, 都不回滚.
     * =================================================================================================================
     * methodA 调用 methodB, methodA 开启事务, methodB 嵌套事务(Propagation.NESTED)
     * 在执行 NESTED 事务前, 会设置保存点 savepoint, 如果 NESTED 的事务发生异常,
     */
    @Test
    public void test1() {
        serviceA.methodA();
    }

    /**
     * =================================================================================================================
     * 在一个 service 的事务方法中,调用同一个 service 的另外一个事务方法, 则另一个事务方法不会生效.因为 Spring 事务是基于 AOP 来实现
     * 的, 必须由代理对象来调用方法才可以得到事务的增强.
     * 解决方案:
     *  1. 通过 AopContext 获取当前的代理对象, AopContext.currentProxy(), 调用事务方法.
     *  2. 在 service 中注入 ApplicationContext 对象, 通过该对象也可以获取代理对象.
     */
    @Test
    public void test2() {
        serviceA.methodC();
    }

}
