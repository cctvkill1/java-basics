package basics.atomic;

import java.util.concurrent.atomic.AtomicReference;

// 多线程同时修改string 只有一个成功 juc的atomic 原子类
public class AtomicReferenceTest {
    public final static AtomicReference<String> atomicStr = new AtomicReference<String>("abc");

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(Math.abs((int) (Math.random() * 100)));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (atomicStr.compareAndSet("abc", "def")) {
                    System.out.println("Thread:" + Thread.currentThread().getId() + " change value");
                } else {
                    System.out.println("Thread:" + Thread.currentThread().getId() + " fail");
                }

            }).start();
        }
    }
}
