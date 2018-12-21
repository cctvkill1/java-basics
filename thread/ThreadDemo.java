package basics.thread;

//设置线程 优先级 守护进程 中断
public class ThreadDemo {
    public static class Demo extends  Thread{
        public void run(){
            while(true){
                System.out.println("I am alive "+this.getId());
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
//                    e.printStackTrace();
                    System.out.println(this.getId()+" 这里收到中断 结束掉线程");
                    break;
                }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        Thread t = new ThreadDemo.Demo();
        Thread t1 = new ThreadDemo.Demo();
//        守护进程
//        t.setDaemon(true);
//        优先级
        t1.setPriority(Thread.MIN_PRIORITY);
        t1.start();
        t.start();
        Thread.sleep(2000);
        t.interrupt();
    }
}
