package p4.e10_Blancanieves_y_Enanitos;

import p3.e2_3.SyncCircularBuffer;
import p4.e5.Semaphore;

import java.util.Random;

public class Solucion {
    private static final SyncCircularBuffer<Semaphore> trabajoDeBlancanieves = new SyncCircularBuffer<>(5);
    private static final SyncCircularBuffer<Integer> sillas = new SyncCircularBuffer<>(5);

    private static final Random random = new Random();

    private static final Runnable thread_blancanieves = () -> {
        long i = 0;
        while(true) {
            // Espero el siguiente plato requerido
            Semaphore semaphore = trabajoDeBlancanieves.next();

            System.out.printf("Blancanieves prepara el plato numero %d\n", i);

            // Lo preparo
            try {
                Thread.sleep(100 + random.nextInt(1000));
            } catch (InterruptedException e) {
                // No deberia pasar
                e.printStackTrace();
            }

            System.out.printf("Plato %d terminado\n", i++);

            // Aviso que esta listo
            semaphore.signal_a();
        }
    };

    private static final Thread enanitos[] = new Thread[7];
    private static final Thread blancanieves = new Thread(thread_blancanieves);

    public static void main(String[] args) {
        blancanieves.start();

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            Thread enanito = new Thread(() -> {
                while(true) {
                    System.out.printf("Enanito %d trabajando...\n", finalI);

                    // Voy a trabajar
                    try {
                        Thread.sleep(100 + random.nextInt(1000));
                    } catch (InterruptedException e) {
                        // No deberia pasar
                        e.printStackTrace();
                    }

                    System.out.printf("Enanito %d esperando una silla...\n", finalI);
                    // Esperar una silla
                    int nro_silla = sillas.next();

                    System.out.printf("Enanito %d agarro la silla %d\n", finalI, nro_silla);

                    Semaphore semaphore = new Semaphore(0);

                    // Le digo a blancanieves que me prepare la comida
                    trabajoDeBlancanieves.add(semaphore);

                    // Espero que la comida este lista
                    semaphore.wait_a();

                    // Como
                    System.out.printf("Enanito %d comiendo...\n", finalI);

                    try {
                        Thread.sleep(100 + random.nextInt(1000));
                    } catch (InterruptedException e) {
                        // No deberia pasar
                        e.printStackTrace();
                    }

                    // Libero mi silla
                    sillas.add(nro_silla);
                }
            });
            enanito.start();

            enanitos[i] = enanito;
        }

        for (int i = 0; i < 4; i++) {
            sillas.add(i);
        }

        try {
            blancanieves.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread enanito : enanitos) {
            try {
                enanito.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
