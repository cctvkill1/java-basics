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
 */
public class ForkJoinCalc extends RecursiveTask<Double> {

    private static final long serialVersionUID = 1L;
    private static final int THRESHOLD = 3;
    private static final int START = 1;
    private static final int END = 100;
    private static final int NUMBER = 10;
    private static final float STEP = 0.01F;
    private double x1;
    private int start;
    private int end;

    public ForkJoinCalc() {
        this.x1 = START;
        this.start = 0;
        this.end = (int) ((END - START + 1) / STEP);
    }

    public ForkJoinCalc(double x1, int start, int end) {
        this.x1 = x1;
        this.start = start;
        this.end = end;
    }

    @Override
    public Double compute() {
        double sum = 0D;
        // 判断是否是拆分完毕
        if ((end - start) < THRESHOLD) {
            for (int i = start; i <= end; i++) {
                // 计算面积
                double x2 = x1 + STEP;
                double y1 = 1 / x1;
                double y2 = 1 / x2;
                double area = 0.01 * y2 + (y1 - y2) * 0.01 / 2;
                System.out.println(String.format("x1:%f area:%f", x1, area));
                sum += area;
                x1 += STEP;
            }
        } else {
            // 没有拆分完毕就开始拆分 分成NUMBER个小任务 分而治之
            // 先计算每个并行执行多少个任务
            int step = (start + end) / NUMBER;
            ArrayList<ForkJoinCalc> subTasks = new ArrayList<ForkJoinCalc>();
            int pos = start;
            // BUG 待处理 pos>end
            for (int i = 0; i < NUMBER; i++) {
                int last = pos + step;
                if (last > end) {
                    last = end;
                }
                ForkJoinCalc subTask = new ForkJoinCalc(x1 + STEP, pos, last);
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
        ForkJoinCalc task = new ForkJoinCalc();
        ForkJoinTask<Double> result = forkJoinPool.submit(task);
        try {
            double res = result.get();
            System.out.println("sum = " + res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("耗时: %dms", System.currentTimeMillis() - startTime));

    }
}
