import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MainClass {
    public static String methodExecuted;
    public static void main(String[] args) throws InterruptedException {
        /* vars */
        int[][] A = new int [2][2], B = new int [2][2], C = new int [2][2], D = new int [2][2];
        int[][] r = new int [2][2], s = new int [2][2], t = new int [2][2];
        int capacity = A.length * A[0].length;

        CountDownLatch addLatch = new CountDownLatch(A.length);
        CountDownLatch printLatch = new CountDownLatch(A.length);
        CountDownLatch multiplyLatch = new CountDownLatch(A[0].length * B.length);


        generateValues(A,B,C,D,r,s,t);

        MathMathImpl u = new MathMathImpl(A,B,C,r,s,t,capacity,0,0, addLatch, printLatch, multiplyLatch);

        // code to initialize A,B,C,D
        u.print(A);
        u.print(B);
        u.print(C);
        u.add(A,B,r);
        u.print(r);
        u.multiply(r,C,s);
        u.print(s);
        u.multiply(s,D,t);
        u.print(t);

    }

     public static void generateValues(int[][]A, int [][]B, int [][]C, int [][]D, int [][]r, int [][]s, int [][]t){
        Random rand = new Random();
        int rand_int = rand.nextInt(10);

        for(int [] i: A){
            for(int j = 0; j < i.length; j ++){
                i[j]=rand_int;
                rand_int=rand.nextInt(10);
            }
        }
        for(int [] i: B){
            for(int j = 0; j < i.length; j ++){
                i[j] = rand_int;
                rand_int = rand.nextInt(10);
            }
        }
        for(int [] i: C){
            for(int j = 0; j < i.length; j ++){
                i[j]=rand_int;
                rand_int=rand.nextInt(10);
            }
        }
        for(int [] i: D){
            for(int j = 0; j < i.length; j ++){
                i[j]=rand_int;
                rand_int=rand.nextInt(10);
            }
        }
         for(int [] i: r){
             for(int j = 0; j < i.length; j ++){
                 i[j]=0;
             }
         }
         for(int [] i: s){
             for(int j = 0; j < i.length; j ++){
                 i[j]=0;
             }
         }
         for(int [] i: t){
             for(int j = 0; j < i.length; j ++){
                 i[j]=0;
             }
         }
    }
}
