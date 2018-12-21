package basics.thread;
//线程同步锁 wait notify notifyall
public class ObjectWNA {
    final  static Object  object= new Object();
    public static class T1 extends Thread{
        public void run (){
            synchronized (object){
                System.out.println(System.currentTimeMillis()+ " T1 start ！");
                try {
                    System.out.println(System.currentTimeMillis()+" T1 wait for object");
                    object.wait();
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis()+" T1 end!");
            }
        }
    }
    public static class  T2 extends  Thread{
        public void run (){
            synchronized (object){
                System.out.println(System.currentTimeMillis()+" T2 start notify one thread");
                //随机通知一个线程
//                object.notify();
                //随机通知所有线程（没收到通知的线程将会一直等待,blocked状态）
                object.notifyAll();
                System.out.println(System.currentTimeMillis()+" T2 end!");
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){

                }

            }
        }
    }


    public static void main(String[] args) throws InterruptedException{
        T1 t1 = new T1();
        T1 t1_1 = new T1();
        T2 t2 = new T2();
        t1.start();
        t1_1.start();
        Thread.sleep(1000);
        t2.start();

    }
}
