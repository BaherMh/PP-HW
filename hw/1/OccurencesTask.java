import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class OccurencesTask extends RecursiveTask<Integer> {
    public int threshold = 5000;
    public static List<Integer> lst;
    private Integer target;
    private int left;
    private int right;
    public OccurencesTask(List<Integer> lst, Integer target)
    {
        this.lst = lst;
        this.target = target;
        this.left = 0;
        this.right = lst.size() - 1;
    }
    public OccurencesTask( Integer target, int left, int right)
    {
        this.target = target;
        this.left = left;
        this.right = right;
    }
    @Override
    protected Integer compute() {
        if(right - left + 1< threshold)
        {
            return compute_occurrences();
        }
        else
        {
            int mid = (right + left) / 2;
            OccurencesTask firstSubtask =
                    new OccurencesTask(target, left, mid);
            OccurencesTask secondSubtask =
                    new OccurencesTask(target, mid+1, right);

            firstSubtask.fork(); // queue the first task
            int res = secondSubtask.compute() + firstSubtask.join();
            return res;
        }
    }
    private int compute_occurrences()
    {
        int cnt = 0;
        for (int i = left; i <= right; i++)
        {
            if(lst.get(i) == target)
            {
                cnt ++;
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        Random random = new Random();
        List<Integer> lst = random
                .ints(200000000, 1, 3)
                .boxed()
                .collect(toList());
        ForkJoinPool pool = new ForkJoinPool();
        OccurencesTask task = new OccurencesTask(lst, lst.get(0));
        Instant start = Instant.now();
        System.out.println(pool.invoke(task));
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println(timeElapsed);



        start = Instant.now();
        int cnt =0;
        for (int item : lst)
        {
            if(item == lst.get(0))
            {
                cnt++;
            }
        }
        System.out.println(cnt);
        end = Instant.now();
        timeElapsed = Duration.between(start, end);
        System.out.println(timeElapsed);

        start = Instant.now();
        long answers = lst.stream().filter(c -> c == lst.get(0)).count();
        end = Instant.now();
        timeElapsed = Duration.between(start, end);
        System.out.println((answers));
        System.out.println(timeElapsed);


        start = Instant.now();
        answers = lst.parallelStream().filter(c -> c == lst.get(0)).count();
        end = Instant.now();
        timeElapsed = Duration.between(start, end);
        System.out.println((answers));
        System.out.println(timeElapsed);




    }
}
