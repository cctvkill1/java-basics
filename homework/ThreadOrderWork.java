package basics.homework;

import java.util.concurrent.*;

/**
 * 1.现在有T1、T2、T3三个线程，你怎样保证T2在T1执行完后执行，T3在T2执行完后执行
 * 最后总结出3种方法 countDownLatch最优雅
 */
public class ThreadOrderWork {
    static final  int count=10;


    private String name;

    public ThreadOrderWork(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println(String.format("%s is running", name));
    }

    public static void main(String[] args) throws InterruptedException {
        long startMillis = System.currentTimeMillis();
//        方法一 join
        for (int i = 0; i < count; i++) {
            Thread tmp = new Thread(() -> System.out.println(Thread.currentThread().getName()), "join-" + i);
            tmp.start();
            tmp.join();
        }

//        // 方法2 newSingleThreadExecutor 单个线程池 这里不会阻塞主线程 会先打印执行时间
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < count; i++) {
            ThreadOrderWork threadOrderWork = new ThreadOrderWork("SingleThread-"+i);
            executorService.submit(() -> {
                threadOrderWork.run();
            });
        }
        executorService.shutdown();

        // 方法3 countDownLatch 可同时阻塞多个线程，但它们可并发执行
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < count; i++) {
            new Thread(() ->{ System.out.println(Thread.currentThread().getName());countDownLatch.countDown();}, "countDownLatch-" + i).start();
        }
        countDownLatch.await();



        System.out.println(String.format("执行时间:%sms",System.currentTimeMillis() - startMillis));

    }
}
