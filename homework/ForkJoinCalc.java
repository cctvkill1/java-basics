package basics.homework;

import java.util.concurrent.*;

/**
 * 计算给定函数y=1/x 在定义域 [1,100]上围城与X轴围成的面积，计算步长0.01
 * 可以查看https://www.2cto.com/kf/201611/568223.html 里面有个图 一眼就明白题意了
 * TODO 时间不够 回家再搞
 */
public class ForkJoinCalc {

    static ExecutorService executor = new ThreadPoolExecutor(5, 10, 3600, TimeUnit.SECONDS, new SynchronousQueue<>());

    public static void main(String[] args) {

    }
}
