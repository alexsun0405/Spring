package com.test.service.impl;

import com.test.dao.TestDao;
import com.test.service.ServiceA;
import com.test.service.ServiceB;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class ServiceAImpl implements ServiceA {

    @Autowired
    private TestDao testDao;

    @Autowired
    private ServiceB serviceB;

    @Override
    @Transactional
    public void methodA() {
        System.out.println("====================== MethodA 开始执行 ======================");
        testDao.reduceNumber(1000);
        System.out.println("====================== MethodA 执行完毕 ======================");
        try {
            serviceB.methodB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        testDao.reduceNumber(1000);
    }

    @Override
    @Transactional
    public void methodC() {
        try {
            ((ServiceA)AopContext.currentProxy()).methodD();
        } catch (Exception e) {
            System.out.println("=============出错了,应该回滚===============");
        }
        System.out.println("====================== MethodC 开始执行 ======================");
        testDao.addNumber(1001);
        System.out.println("====================== MethodC 执行完毕 ======================");
    }

    @Override
    @Transactional
    public void methodD() {
        System.out.println("====================== MethodD 开始执行 ======================");
        testDao.reduceNumber(1000);
        System.out.println("====================== MethodD 执行完毕 ======================");
        throw new RuntimeException("出错了");
    }


}
