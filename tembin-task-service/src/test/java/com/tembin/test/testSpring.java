package com.tembin.test;

import com.google.inject.OutOfScopeException;
import com.tembin.common.base.ApiResult;

/**
 * @program: task-manager
 * @description:
 * @author: zhoucx
 * @create: 2019-08-05 11:40
 **/
public class testSpring {

    public static void main(String[] args) {
        ApiResult apiResult = testCallBack();
        System.out.println(apiResult);
    }

    public static ApiResult testCallBack(){
        System.out.println("主线程执行");
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程开始执行");
                throw new OutOfScopeException("错误");
            }
        }).start();
        System.out.println("主线程结束");
        return new ApiResult(200,"成功",null);
    }
}
