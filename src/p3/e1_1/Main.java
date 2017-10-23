package p3.e1_1;

public class Main {
    private static final int N = 10;
    private static final Thread threads[] = new Thread[N];
    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            threads[i] = new Thread(new Ejecutable());
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
