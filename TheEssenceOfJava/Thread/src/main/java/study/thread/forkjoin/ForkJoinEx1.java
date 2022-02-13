package study.thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinEx1 {

    static final ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        long from = 1L, to = 100_000_000L;

        SumTask task = new SumTask(from, to);

        long start = System.currentTimeMillis();
        Long result = pool.invoke(task);
        System.out.println("Elapsed time(multi): " + (System.currentTimeMillis() - start));
        System.out.printf("sum of %d~%d = %d%n", from, to, result);
        System.out.println();

        result = 0L;
        start = System.currentTimeMillis();
        for(long i = from; i <= to; i++) {
            result += i;
        }
        System.out.println("Elapsed time(multi): " + (System.currentTimeMillis() - start));
        System.out.printf("sum of %d~%d = %d%n", from, to, result);
        System.out.println();
    }

    static class SumTask extends RecursiveTask<Long> {
        long from, to;

        SumTask(long from, long to) {
            this.from = from;
            this.to = to;
        }

        public Long compute() {
            long size = to - from + 1;  // from <= i <= to

            if (size <= 5) {    // 더할 숫자가 5개 이하면 숫자의 합을 반환
                return sum();
            }

            // 범위를 반으로 나누어 두 개의 작업을 생성
            long half = (from + to) / 2;
            SumTask leftSum = new SumTask(from, half);
            SumTask rightSum = new SumTask(half + 1, to);

            leftSum.fork();
            return rightSum.compute() + leftSum.join();
        }

        private long sum() {
            long tmp = 0L;

            for(long i = from; i <= to; i++) {
                tmp += i;
            }

            return tmp;
        }
    }

}
