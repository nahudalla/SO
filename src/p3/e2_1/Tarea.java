package p3.e2_1;

public class Tarea implements Runnable {
    @Override
    public void run() {
        for (int n = 0; n < 10000; ++n) {
            System.out.printf(
                    "Hola Threads!. Soy el thread [%d] ejecutando por [%d] vez.\n",
                    Thread.currentThread().getId(),
                    n
            );
        }
    }
}
