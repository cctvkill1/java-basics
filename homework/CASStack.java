package basics.homework;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * // 实现一个无锁的Stack，并写一段测试代码（多线程访问），证明这个Stack是线程安全的。给出程序以及运行的截图。 
 * https://blog.csdn.net/whf584201314/article/details/78961749
 * 关键点：无锁须利用CAS类
 * 利用原子操作类 将obj数组和当前栈的索引index做原子操作 失败就无限循环 由于是原子操作 线程是安全的 且性能不错
 */
public class CASStack {

    // data 
    static private AtomicInteger j = new AtomicInteger(0);

    // 控制插入的指针
    static private AtomicInteger index = new AtomicInteger(0);

    // 存放集合
    @SuppressWarnings("unchecked")
    static private AtomicReference<Object>[] arr = new AtomicReference[] { new AtomicReference<Object>(),
            new AtomicReference<Object>(), new AtomicReference<Object>(), new AtomicReference<Object>(),
            new AtomicReference<Object>(), new AtomicReference<Object>(), new AtomicReference<Object>(),
            new AtomicReference<Object>(), new AtomicReference<Object>(), new AtomicReference<Object>() };

    static final private int MAXLENGTH = 10;

    public static Integer get() {
        return index.get();
    }

    // 入栈
    public static void add() {
        Integer i = get();
        if (i < MAXLENGTH && i >= 0) {
            if (index.compareAndSet(i, i + 1)) {
                // 自增cas操作
                int data = j.incrementAndGet();
                if (arr[i].compareAndSet(null, data))
                    System.out.println("index:" + i + "  data:" + data);
                else {
                    System.out.println("插入失败data:" + data);
                    readd(data);
                }
            }
        }
    }

    // 处理插入失败的数据
    public static void readd(Integer data) {
        boolean flag = true;
        while (flag) {
            Integer i = get();
            if (i < MAXLENGTH && i >= 0) {
                if (index.compareAndSet(i, i + 1)) {
                    if (arr[i].compareAndSet(null, data)) {
                        flag = false;
                        System.out.println("index:" + i + "data:" + data + "重新插入成功！！！！");
                    }
                }
            }
        }
    }

    // 出栈
    public static void remove() {
        Integer i = get();
        if (i > 0) {
            if (index.compareAndSet(i, i - 1)) {
                int index = i - 1;
                if (arr[index] != null && arr[index].get() != null) {
                    Object o = arr[index].get();
                    if (arr[index].compareAndSet(arr[index].get(), null))
                        System.out.println("出队 [" + index + "] 值:" + o);
                }
            }
        }
        System.out.println("这里是出栈失败了"+i);
    }

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                while (true) {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    add();
                }
            }).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    remove();
                }
            }).start();
        }
    }
}