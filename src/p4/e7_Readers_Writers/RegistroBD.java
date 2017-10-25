package p4.e7_Readers_Writers;

import p4.e5.Semaphore;

import java.util.Random;

public class RegistroBD {
    public static void main(String[] args) {
        final Random random = new Random();

        Thread writers[] = new Thread[2];
        Thread readers[] = new Thread[10];

        final RegistroBD registro = new RegistroBD();

        for (int i = 0; i < 2; i++) {
            int finalI = i;
            Thread writer = new Thread(() -> {
                while (true) {
                    int waitTime = 1000 + random.nextInt(3000);
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        // Should not happen
                        e.printStackTrace();
                    }

                    registro.atomicIncrement();
                }
            });
            writer.start();
        }

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread reader = new Thread(() -> {
                while (true) {
                    int waitTime = 1000 + random.nextInt(3000);
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        // Should not happen
                        e.printStackTrace();
                    }

                    System.out.printf("Leido: %d; Por: %d\n", registro.leer(), finalI);
                }
            });
            reader.start();
        }
    }

    private int data = 0;

    private final Semaphore rw_mutex = new Semaphore(1);
    private final Semaphore mutex = new Semaphore(1);
    private int reader_count = 0;

    public void escribir(int newData) {
        this.rw_mutex.wait_a();

        this.data = newData;

        this.rw_mutex.signal_a();
    }

    public void atomicIncrement() {
        this.rw_mutex.wait_a();

        this.data++;

        this.rw_mutex.signal_a();
    }

    public int leer() {
        int data;

        this.mutex.wait_a();
        this.reader_count++;
        if(reader_count == 1)
            this.rw_mutex.wait_a();
        this.mutex.signal_a();

        data = this.data;

        this.mutex.wait_a();
        this.reader_count--;
        if(this.reader_count == 0)
            this.rw_mutex.signal_a();
        this.mutex.signal_a();

        return data;
    }
}
