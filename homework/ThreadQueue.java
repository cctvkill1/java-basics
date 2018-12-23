package basics.homework;

import java.util.LinkedList;
import java.util.List;

/**
 * 使用 wait notify 实现一个队列，队列有2个方法，add 和 get 。 add方法往队列中添加元素，get方法往队列中获得元素。
 * 队列必须是线程安全的。如果get执行时，队列为空，线程必须阻塞等待，直到有队列有数据。如果add时，队列已经满，则add线程要等待，直到队列有空闲空间
 * 开始网上的代码改的https://blog.csdn.net/qq_25343557/article/details/78010203 发现有点不对
 * 1.虽然消费者和生产者可以交换着取出、插入队列 但是get没返回值 改成while循环之后有返回了 
 * 2.每个生产者生产循环生产任务 我觉得应该一个新任务开一个新线程更符合实际
 * 
 * 后来仔细看了下运行结果 发现根本不对！！！ 超出队列插入需要等待 结果光等待 唤醒之后并没有插入(可以看他的截图 生产者3的生产2并没有插入)
 * 改进就是把生产者的else去掉就对了 唤醒之后也要插入才行  
 */
public class ThreadQueue {

    public class MyQueue<E> {

        private List<E> queue = new LinkedList<E>();
        private int queueSize = 10;

        public MyQueue(int queueSize) {
            this.queueSize = queueSize;
        }

        public MyQueue() {
        }

        public int getSize() {
            return queue.size();
        }

        public void add(E e) {
        synchronized (queue) {
        while (queue.size() == queueSize) {
        System.out.println(String.format("%s: 队列满了，等待空闲空间",
        Thread.currentThread().getName()));
        try {
        queue.wait();
        } catch (InterruptedException ex) {
        ex.printStackTrace();
        queue.notifyAll();
        }
        }
        queue.add(e);
        System.out.println(String.format("%s 往队列插入一个元素[%s]，队列长度：%s",
        Thread.currentThread().getName(),
        String.valueOf(e), queue.size()));
        queue.notifyAll();
        }
        }

        public E get() {
        E temp = null;
        synchronized (queue) {
        while (queue.isEmpty()) {
        System.out.println(String.format("%s: 队列空，等待数据到来",
        Thread.currentThread().getName()));
        try {
        queue.wait();
        } catch (InterruptedException e) {
        e.printStackTrace();
        queue.notifyAll();
        }
        }
        temp = queue.get(0);
        System.out.println(String.format("%s 从队列取走一个元素[%s]，队列长度：%s", Thread.currentThread().getName(),
                String.valueOf(temp), queue.size()));
        queue.remove(0);
        queue.notifyAll();

        }
        return temp;
        }

        // public void add(E e) {
        //     synchronized (queue) {
        //         if (queue.size() == queueSize) {
        //             System.out.println(String.format("%s: 队列满了，等待空闲空间", Thread.currentThread().getName()));
        //             try {
        //                 queue.wait();
        //             } catch (InterruptedException ex) {
        //                 ex.printStackTrace();
        //                 queue.notifyAll();
        //             }
        //         }  
        //             queue.add(e);
        //             System.out.println(String.format("%s 往队列插入一个元素[%s]，队列长度：%s", Thread.currentThread().getName(),
        //                     String.valueOf(e), queue.size()));
        //             queue.notifyAll();
                 
        //     }
        // }

        // public E get() {
        //     E temp = null;
        //     synchronized (queue) {
        //         if (queue.isEmpty()) {
        //             System.out.println(String.format("%s: 队列空，等待数据到来", Thread.currentThread().getName()));
        //             try {
        //                 queue.wait();                        
        //             } catch (InterruptedException e) {
        //                 e.printStackTrace();
        //                 queue.notifyAll();
        //             }
        //         } else {
        //             temp = queue.get(0);
        //             System.out.println(String.format("%s 从队列取走一个元素[%s]，队列长度：%s", Thread.currentThread().getName(),
        //                     String.valueOf(temp), queue.size()));
        //             queue.remove(0);
        //             queue.notifyAll();
        //         }
        //     }
        //     return temp;
        // }

    }

    public static void main(String[] args) throws InterruptedException {

        ThreadQueue tq = new ThreadQueue();
        MyQueue<Integer> queue = tq.new MyQueue<Integer>();
        // 生产者个数
        int producer = 30;
        // 消费者个数
        int consumer = 3;
        for (int i = 0; i < producer; i++) {
            new Thread(() -> {
                // 网上的代码启动
                // for (int j = 0; j < producer; j++) {
                // queue.add((int) (Math.random() * 1000));
                // }

                // 我的方法
                queue.add((int) (Math.random() * 1000));
            }, "生产者" + i).start();
        }
        for (int i = 0; i < consumer; i++) {
            new Thread(() -> {
                while (true) {
                    // 网上的代码启动
                    // queue.get();
                    // 我的方法
                    int e = queue.get();
                    System.out.println("取值是"+e);
                }
            }, "消费者" + i).start();
        }

    }

}
