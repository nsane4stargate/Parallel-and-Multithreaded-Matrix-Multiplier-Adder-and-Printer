import java.util.concurrent.CountDownLatch;

public interface MatMath {
    void multiply(int[][] A, int[][]B, int[][]C) throws InterruptedException;  // multiply A and B into C
    void add(int[][]A, int[][]B, int[][]C) throws InterruptedException;        // add A and B into C
    void print(int[][] A) throws InterruptedException;
}