package p3.e1_1;

public class Ejecutable implements Runnable {
    private long var = 0;
    @Override
    public void run() {
        while(true) {
            System.out.printf("%d; var = %d\n", Thread.currentThread().getId(), var++);
        }
    }
}
