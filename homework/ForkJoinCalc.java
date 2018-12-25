package basics.homework;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 计算给定函数y=1/x 在定义域 [1,100]上围城与X轴围成的面积，计算步长0.01
 * 可以查看https://www.2cto.com/kf/201611/568223.html 里面有个图 一眼就明白题意了 我擦 题目真难啊
 * 可利用定积分求曲边梯形面积 但是完全不会定积分- - 那就当三角形吧 简单点 首先是1~100 步长是0.01 那么就是计算10000次
 * 每次是个矩形加三角形的面积 矩形是 0.01*y2 其中 y2=1/x2 三角形是 (y1-y2)*0.01/2 循环1W次 求和 so easy
 * 敲代码的时候遇到几个问题 1. pos >end 2.(end - start) 不加1 结果是4.33088  加1会算到101了结果是4.6164217错误
 */
public class ForkJoinCalc extends RecursiveTask<Float> {

    private static final long serialVersionUID = 1L;
    private static final int THRESHOLD = 10;
    private static final int START = 1;
    private static final int END = 100;
    private static final float STEP = 0.01F;
    private int start;
    private int end;

    public ForkJoinCalc() {
        this.start = 0;
        this.end = (int) ((END - START + 1) / STEP);
    }

    public ForkJoinCalc(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Float compute() {
        float sum = 0;
        // 判断是否是拆分完毕
        if ((end - start) < THRESHOLD) {
            System.out.println("子任务开始了start:" + start + " end:" + end + "  total:" + (end - start) / STEP);
            for (int i = 0; i < (end - start) / STEP; i++) {
                // 计算面积
                int intX1 = (int) ((start + i * STEP) * 100);
                int intX2 = (int) (intX1 + STEP * 100);
                float x1 = (float) intX1 / 100;
                float x2 = (float) intX2 / 100;
                float y1 = 1 / x1;
                float y2 = 1 / x2;
                float area = (float) (0.01 * y2 + (y1 - y2) * 0.01 / 2);
                // 注意精度丢失问题 导致x1 x2会打印出0.99999的问题
                System.out.println(String.format("x1:%f x2:%f area:%f", x1, x2, area));
                sum += area;
            }
        } else {
            // 没有拆分完毕就开始拆分 分成NUMBER个小任务(递归) 分而治之
            // 先计算每个并行执行多少个任务
            int step = (end - start) / THRESHOLD;
            ArrayList<ForkJoinCalc> subTasks = new ArrayList<ForkJoinCalc>();
            int pos = start;
            for (int i = 0; i < THRESHOLD; i++) {
                int last = pos + step;
                if (last > end) {
                    last = end;
                }
                if (pos > end) {
                    break;
                }
                System.out.println("pos:" + pos + " last:" + last);
                ForkJoinCalc subTask = new ForkJoinCalc(pos, last);
                pos += step + 1;
                subTasks.add(subTask);
                // 创建子任务执行
                subTask.fork();
            }
            for (ForkJoinCalc t : subTasks) {
                // 等待子任务执行结果
                sum += t.join();
            }
        }
        return sum;
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinCalc task = new ForkJoinCalc(1, 100);
        ForkJoinTask<Float> result = forkJoinPool.submit(task);
        try {
            float res = result.get();
            System.out.println("sum = " + res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("耗时: %dms", System.currentTimeMillis() - startTime));

    }
}
