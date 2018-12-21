package basics.thread;
// 线程同步锁
public class Sync implements  Runnable{
    static  int i=0;
    // 静态方法同步加锁
    public static synchronized void increase(){
        i++;
    }

    public void run(){
        for (int j= 0 ;j<10000000;j++){
            increase();
        }
    }
    public static void main(String[] args) throws InterruptedException{
//        当前实例加锁
//        Sync s = new Sync();
        Thread t1 = new Thread(new Sync());
        Thread t2 = new Thread(new Sync());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);

    }
}
