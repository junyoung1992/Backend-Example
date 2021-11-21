import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciTest {

    @Test
    void testFibonacci() {
        int[][] cases = {{0, 0}, {1, 1}, {2, 1}, {3, 2}};

        for (int[] c : cases)
            assertEquals(c[1], fib(c[0]));
    }

    private int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

}