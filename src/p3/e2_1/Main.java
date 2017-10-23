package p3.e2_1;

public class Main {
    private static Thread threads[] = new Thread[10];
    public static void main(String[] args) {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Tarea());
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
