package basics.example;


import java.util.concurrent.CountDownLatch;

/*
 *   volatile 不是原子操作
 *   volatile 的适用场景必须满足2个条件：
 *   1.对变量的写操作不依赖于当前值。
 *   2.该变量没有包含在具有其他变量的不变式中。
 *
 * */
public class VolatileDemo {

    private volatile int count = 0;
    static int TEST_THREAD_COUNT = 100;
    final CountDownLatch latch = new CountDownLatch(TEST_THREAD_COUNT);


    public int getCount() {
        return this.count;
    }


    public void setCount() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.count++;
    }


    public static void main(String[] args) {
        VolatileDemo demo = new VolatileDemo();
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            new Thread(() -> {
                demo.setCount();
                demo.latch.countDown();
            }).start();
        }
        try {
            demo.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("最后结果是:%s", demo.getCount()));
    }

}