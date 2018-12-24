package basics.thread;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join 模式看作并行版本的 Divide and Conquer （分而治之）策略
 * 工作窃取（work-stealing）算法是指某个线程从其他队列里窃取任务来执行
 *
 * @author cctv
 * */
public  class ForkJoin extends RecursiveTask<Long>{

        private static final  int THRESHOLD = 100000;
        private long start;
        private long end;
        public ForkJoin(long start ,long end){
            this.start = start;
            this.end = end;
        }


        @Override
        public Long compute(){
            long sum = 0;
            if( (end -start)< THRESHOLD){
                for (long i =start;i<=end;i++){
                    sum+=i;
                }
            }else{
                //分成100个小任务 分而治之
                long step = (start+end)/100;
                ArrayList<ForkJoin> subTasks = new ArrayList<ForkJoin>();
                long pos = start;
                for(int i=0;i<100;i++){
                    long lastOne = pos + step;
                    if(lastOne >end){
                        lastOne=end;
                    }
                    ForkJoin subTask = new ForkJoin(pos, lastOne);
                    pos+=step+1;
                    subTasks.add(subTask);
                    // 创建子任务执行
                    subTask.fork();
                }
                for(ForkJoin t:subTasks){
                    // 等待子任务执行结果
                    sum+=t.join();
                }
            }
            return sum;
        }


    public static void main(String[] args) throws InterruptedException{

        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoin task = new ForkJoin(0,20000L);
        ForkJoinTask<Long> result = forkJoinPool.submit(task);
        try{
            long res = result.get();
            System.out.println("sum = "+res);
        }catch (InterruptedException e ){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        System.out.println(String.format("耗时: %dms", System.currentTimeMillis() - startTime));

    }
}
