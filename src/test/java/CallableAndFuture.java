import java.util.Random;
import java.util.concurrent.*;

/**
 * @author xiaos
 * @create 2018-02-12-17:16
 */
public class CallableAndFuture {

    public static void main(String[] args) {

        //    Callable<Integer> callable = new Callable<Integer>() {
        //        @Override
        //        public Integer call() throws Exception {
        //            return new Random().nextInt();
        //        }
        //    };
        //
        //    FutureTask<Integer> future = new FutureTask<Integer>(callable);
        //    new Thread(future).start();
        //
        //    try {
        //        Thread.sleep(5000);
        //        System.out.println(future.get());
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    } catch (ExecutionException e) {
        //        e.printStackTrace();
        //    }
        //
        //}

        ExecutorService threadPool = Executors.newCachedThreadPool();
        Future<Integer> future = threadPool.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return new Random().nextInt();
            }
        });

            try {
                Thread.sleep(5000);
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


    }
}
