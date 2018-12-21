package basics.atomic;

import org.apache.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
* cas 原子操作 测试
* 用到了线程池ThreadPool 阿里规范建议不要适用Executors 创建线程池
* 使用了信号量Semaphore 个人理解 并发的时候允许执行的线程数量限制 每个线程拿到许可执行 执行完了释放许可 acquire release是阻塞的
* 等待线程全部结束 使用了countDownLatch 当计数器
*
 * @author cctv
 * */
public class AtomicIntegerTest {
    //    private static Logger log = Logger.getLogger(AtomicIntegerTest.class);

    public static int clientTotal = 5000;
    public static int threadTotal = 200;
    public static AtomicInteger count = new AtomicInteger(0);

//    private static final int POOL_SIZE = 40;
//    private static ThreadPoolExecutor comitTaskPool =(ThreadPoolExecutor) new ScheduledThreadPoolExecutor(POOL_SIZE,
//            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

//        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
//        ExecutorService pool = new ThreadPoolExecutor(5, 200,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
//        pool.shutdown();//gracefully shutdown

    public static void main(String[] args) throws Exception {


        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (Exception e) {
//                    log.error("exception", e);
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });

        }
        countDownLatch.await();
        executorService.shutdown();
//        log.info(String.format("count:%d", count.get()));
        System.out.println(String.format("count:%d", count.get()));
    }

    private static void add() {
        count.incrementAndGet();
// count.getAndIncrement();
    }


}
