package basics.homework;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 阶段二 6.1线程池会吃掉堆栈，能不能想办法，在异常的时候 打印出 提交任务的线程的堆栈？给出你的实现代码和截图
 * @author zhaoqide https://blog.csdn.net/yangguangdesenlin/article/details/80222622
 * @desciption  线程池会吃掉堆栈，能不能想办法，在异常的时候 ，打印出提交线程的堆栈？并给出你的实现代码
 * @updatetime 2018年5月7日上午10:43:53
 */
public class ThreadRuntimeException {

    static ExecutorService executor = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    public static void main(String[] args) {
        //没有打印堆栈信息
        Future future = executor.submit(new Worker());
        //在异常的时候 ，打印出提交线程的堆栈
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class Worker implements Runnable{
        @Override
        public void run() {
            System.out.println("nihao");
            throw new RuntimeException("asdasd");
        }

    }
}
