
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MathMathImpl implements MatMath, Runnable{
    private int A [][];
    private int B [][];
    private int C [][];
    private int r [][];
    private int s [][];
    private int t [][];
    private int tempA[];
    private int tempB[];

    private int row, col;
    private CountDownLatch addLatch;
    private CountDownLatch multiplyLatch;
    private CountDownLatch printLatch;

    public MathMathImpl(int[][]A, int[][]B, int[][]C, int[][] r,  int[][] s, int[][] t, int size, int row,
                        int col, CountDownLatch addLatch, CountDownLatch printLatch, CountDownLatch multiplyLatch){
        this.A = A;
        this.B = B;
        this.r = r;
        this.row = row;
        this.col = col;
        this.addLatch = addLatch;
        this.printLatch = printLatch;
        this.multiplyLatch = multiplyLatch;
    };
    public MathMathImpl(int[][]A, int row, CountDownLatch latch){
        this.A = A;
        this.row = row;
        this.col = col;
        this.printLatch = latch;
    };

    public MathMathImpl(int[][] A, int[][] B, int[][] r, int size, int row, int col, CountDownLatch addLatch) {
        this.A = A;
        this.B = B;
        this.r = r;
        this.row = row;
        this.col = col;
        this.addLatch = addLatch;
    }

    public MathMathImpl(int[] tempA, int[] tempB, int[][] r, int size, int row, int col, CountDownLatch multiplyLatch) {
        this.tempA = tempA;
        this.tempB = tempB;
        this.s = r;
        this.t = r;
        this.row = row;
        this.col = col;
        this.multiplyLatch = multiplyLatch;

    }
    @Override
    public void multiply(int[][] A, int[][] B, int[][] s) throws InterruptedException {
        MainClass.methodExecuted = "multiply";
        ExecutorService service  = Executors.newSingleThreadExecutor();
        int tempA[],tempB[] = new int[A[0].length];
        int row = 0;
        int rowA = 0;
        int col = 0;
        int colB = 0;
        boolean switchColumn = false;
        if(A[0].length != B.length){
            System.out.println(" Cannot perform matrix multiplication on with these dimensions");
        }

        for(int cycles = 0; cycles < (s.length * 2); cycles++) {
            tempA = A[rowA];
            populateTempB(tempB, colB, B);
            service.execute(new MathMathImpl(tempA, tempB, s, 25, row, col, multiplyLatch));
            row++;
            colB++;
            if(row == s.length){
                row = 0;
                switchColumn = true;
                col++;
                rowA++;
            }
            if(switchColumn){colB = 0; switchColumn = false;}
        }
        multiplyLatch.await();
        service.shutdown();
        /* Reinitialize the  multiplyLatch for the next time this functions is called */
        multiplyLatch = new CountDownLatch(A[0].length * B.length);
    }

    public void populateTempB(int[] tempB, int col, int [][] B){
        int index = 0;
        for(int[] i : B){
            tempB[index++] = i[col];
        }
    }

    @Override
    public void add(int[][] A, int[][] B, int[][] results) throws InterruptedException {
        MainClass.methodExecuted = "add";
        ExecutorService service = Executors.newSingleThreadExecutor();
        int row = 0;
        for(int []i: r) {
            service.execute(new MathMathImpl(A, B, r, 25, row, col, addLatch));
            row++;
        }

        addLatch.await();
        service.shutdown();
        /* Reinitialize the addLatch for the next time this functions is called */
        addLatch = new CountDownLatch(A.length);

    }

    @Override
    public void print(int[][] A) throws InterruptedException {
        MainClass.methodExecuted= "print";
        int row = 0;
        ExecutorService service = Executors.newSingleThreadExecutor();

        for(int []i: A) {
            service.execute(new MathMathImpl(A, row, printLatch));
            row++;
        }
        printLatch.await();
        service.shutdown();
        for(int i = 0; i < 3; i ++) System.out.println("");
        /* Reinitialize the printLatch for the next time this functions is called */
        printLatch = new CountDownLatch(A.length);
    }

    public void run(){
        switch(MainClass.methodExecuted) {
            case "print":
                int print[] = new int[A[0].length];
                IntStream.range(0,A[row].length).forEach(i->print[i] = A[row][i]);
                Arrays.stream(print).parallel().forEach(System.out::println);
                printLatch.countDown();
                break;
            case "add":
                r[row] = IntStream.range(0, A[row].length).parallel().map(index -> A[row][index] + B[row][index]).toArray();
                addLatch.countDown();
                break;
            case "multiply":
                /* I still don't understand streams enough to keep traverse more than one array */
                int accumulator = 0;
                for(int i = 0; i <tempA.length; i ++){
                    accumulator += tempA[i] * tempB[i];
                }
                s[col][row] = accumulator;
                multiplyLatch.countDown();
                break;
            default:
                break;
        }
    }
}
