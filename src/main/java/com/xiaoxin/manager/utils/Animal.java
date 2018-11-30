package com.xiaoxin.manager.utils;

/**
 * @Author:jzwx
 * @Desicription: Animal
 * @Date:Created in 2018-11-27 17:03
 * @Modified By:
 */
public abstract class Animal extends Thread {
    public int length = 2000;//比赛长度

    public abstract void runing();

    @Override
    public void run() {
        super.run();
        while (length > 0) {
            runing();
        }
    }

    //在需要回调数据的地方（两个子类需要），声明一个接口
    public static interface Calltoback {
        public void win();
    }

    // 创建接口对象
    public Calltoback calltoback;
}
