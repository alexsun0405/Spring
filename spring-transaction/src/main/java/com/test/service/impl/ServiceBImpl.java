package com.test.service.impl;

import com.test.dao.TestDao;
import com.test.service.ServiceB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceBImpl implements ServiceB {

    @Autowired
    private TestDao testDao;

    @Override
    @Transactional( propagation = Propagation.NESTED)
    public void methodB() {
        System.out.println("====================== MethodB 开始执行 ======================");
        testDao.addNumber(1001);
        System.out.println("====================== MethodB 执行完毕 ======================");
//        try {
            throw new RuntimeException("error");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
    }
}
