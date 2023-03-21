import java.util.HashMap;
import java.util.concurrent.RecursiveTask;


/*
However, besides being a dumb way to compute Fibonacci functions (there is a simple fast linear algorithm that you'd use in practice),
 this is likely to perform poorly because the smallest subtasks are too small to be worthwhile splitting up.
 Instead, as is the case for nearly all fork/join applications,
 you'd pick some minimum granularity size (for example 10 here) for which you always sequentially solve rather than subdividing.
 */

public class Fibonacci extends RecursiveTask<Integer> {
    final int n;
    public static HashMap<Integer,Integer> cache=new HashMap<Integer,Integer>();
    public Fibonacci(int n) { this.n = n; }

    public Integer compute() {

        if(n > 20) {
            if (n <= 1)
                return n;
            if(cache.get(n) != null)
            {
                System.out.println("getting from cache");
                return cache.get(n);
            }
            Fibonacci f1 = new Fibonacci(n - 1);
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            int res = f2.compute() + f1.join();
            cache.put(n, res);
            return res;

        }else{
            return computeSeq();
        }
    }

    public Integer computeSeq() {
        if (n <= 1)
            return n;
        if(cache.get(n) != null)
        {
            System.out.println("getting from cache");
            return cache.get(n);
        }
        Fibonacci f1 = new Fibonacci(n - 1);
        Fibonacci f2 = new Fibonacci(n - 2);
        int res = f2.computeSeq() + f1.computeSeq();
        cache.put(n, res);
        return res;
    }

    public static void main (String[] args)
    {
        Fibonacci fb = new Fibonacci(40);
        System.out.println(fb.compute());
    }

}
