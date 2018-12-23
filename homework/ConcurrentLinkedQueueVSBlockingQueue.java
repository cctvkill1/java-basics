
package basics.homework;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 比较 ConcurrentLinkedQueue 和 BlockingQueue的性能，并说明为什么。给出你的测试代码，和运行结果的截图。
 * 
 * 
 * 下个作业是 分析CopyOnWriteArrayList的核心代码，说明CopyOnWriteArrayList如何提高并行度
 * 读写分离：
       1. 写入新数组：写操作加锁，并每次复制一个长度加1的新数组，并将原数组的值复制到新数组中，再将新元素追加到新数组中，最后将引用指向新数组。
       2. 读旧数组：从旧数组中读取数值，这样读不用加锁，因为读的是老数组不会写新数据进去，速度快。
       CopyOnWriteArrayList的434行：
        public boolean add(E e) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                Object[] elements = getArray();
                int len = elements.length;
                Object[] newElements = Arrays.copyOf(elements, len + 1);
                newElements[len] = e;
                setArray(newElements);
                return true;
            } finally {
                lock.unlock();
            }
        }
 */
public class ConcurrentLinkedQueueVSBlockingQueue {
    

    private static ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    private static final int count = 1000000;
/* 

    count = 1000000 时候
    concurrentLinkedQueue 耗时: 426ms
    linkedBlockingQueue 耗时: 1506ms

    count = 10000000 时候
    concurrentLinkedQueue 耗时: 13001ms
    linkedBlockingQueue 耗时: 11245ms
    why

*/


    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            // Thread thread = new Thread(() -> {
                concurrentLinkedQueue.add(i);
            // });
            // thread.start();
            // thread.join();
        }
        System.out.println(String.format("concurrentLinkedQueue 耗时: %dms", System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            // Thread thread = new Thread(() -> {
                linkedBlockingQueue.add(i);
            // });
            // thread.start();
            // thread.join();
        }
        System.out.println(String.format("linkedBlockingQueue 耗时: %dms", System.currentTimeMillis() - startTime));
    }
}

// 以下 https://blog.csdn.net/arkblue/article/details/6151488
// 原著中conQueue.add(1);写了两次 并且这种性能测试很不准 结果显示linkedBlockingQueue 明显快些 快2~3倍 因为大部分性能耗在了线程创建上
// public class ConcurrentLinkedQueueVSBlockingQueue {
//     private static int COUNT = 100000;
//     private static int THREAD_NUM = 10;
//     private static CyclicBarrierThread cyclicBarrierThread = new CyclicBarrierThread();

//     private static ConcurrentLinkedQueue conQueue = new ConcurrentLinkedQueue();
//     private static LinkedBlockingQueue linkQueue = new LinkedBlockingQueue();

//     static class ConcurrentLinkedQueueProducer extends TestPerformance {

//         public ConcurrentLinkedQueueProducer(String id, CyclicBarrier barrier, long count, int threadNum,
//                 ExecutorService executor) {
//             super(id, barrier, count, threadNum, executor);
//         }

//         @Override
//         protected void test() {
//             conQueue.add(1);
//         }
//     }

//     static class LinkedBlockingQueueProducer extends TestPerformance {

//         public LinkedBlockingQueueProducer(String id, CyclicBarrier barrier, long count, int threadNum,
//                 ExecutorService executor) {
//             super(id, barrier, count, threadNum, executor);
//         }

//         @Override
//         protected void test() {
//             linkQueue.add(1);
//         }
//     }

//     static class CyclicBarrierThread extends Thread {
//         @Override
//         public void run() {
//             conQueue.clear();
//             linkQueue.clear();
//         }
//     }

//     public static void test(String id, long count, int threadNum, ExecutorService executor) {

//         final CyclicBarrier barrier = new CyclicBarrier(threadNum + 1, cyclicBarrierThread);

//         System.out.println("==============================");
//         System.out.println("count = " + count + "    Thread Count = " + threadNum);

//         concurrentTotalTime += new ConcurrentLinkedQueueProducer("ConcurrentLinkedQueueProducer", barrier, COUNT,
//                 threadNum, executor).startTest();
//         linkedBlockingTotalTime += new LinkedBlockingQueueProducer("LinkedBlockingQueueProducer ", barrier, COUNT,
//                 threadNum, executor).startTest();

//         totalThreadCount += threadNum;
//         executor.shutdownNow();

//         System.out.println("==============================");
//     }

//     static long concurrentTotalTime = 0;
//     static long linkedBlockingTotalTime = 0;

//     static long totalThreadCount = 0;

//     public static void main(String[] args) throws InterruptedException {
//         for (int i = 1; i < 20; i++) {
//             ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM * i);
//             test("", COUNT, 10 * i, executor);
//         }

//         System.out.println("ConcurrentLinkedQueue Avg Time = " + concurrentTotalTime / totalThreadCount);

//         System.out.println("LinkedBlockingQueue Avg Time = " + linkedBlockingTotalTime / totalThreadCount);
//     }
// } 