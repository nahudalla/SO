package parcial2017;

import p4.e5.Semaphore;

import java.util.Random;

public class Sincronizacion {
    public static void main(String[] args) {
        final Sincronizacion sincronizacion = new Sincronizacion();

        for (int i = 0; i < 12; i++) {
            int finalI = i;
            Thread jugador = new Thread(() -> {
                while (true) {
                    sincronizacion.jugar(finalI);

                    System.out.printf("Jugador %d jugando...\n", finalI);

                    try {
                        Thread.sleep((new Random()).nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    sincronizacion.abandonar(finalI);
                }
            });
            jugador.start();
        }
    }

    private final Object en_juego = new Object();
    private Semaphore mutex = new Semaphore(1);
    private int cant_jugadores = 0;
    private Semaphore consola = new Semaphore(4);

    public void jugar(int n) {
        System.out.printf("Jugador %d en sala de espera...\n", n);
        consola.wait_a();
        mutex.wait_a();
        cant_jugadores ++;
        synchronized (en_juego) {
            if(cant_jugadores == 4) {
                System.out.printf("Jugador %d (ultimo) listo, avisando a los demas...\n", n);
                System.out.println("Iniciando partida");
                en_juego.notifyAll();
                mutex.signal_a();
                return;
            }

            mutex.signal_a();
            System.out.printf("Jugador %d esperando el resto de los jugadores...\n", n);
            try {
                en_juego.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void abandonar(int n) {
        System.out.printf("Jugador %d abandona el juego\n", n);
        mutex.wait_a();
        cant_jugadores--;
        if(cant_jugadores == 0) {
            System.out.printf("Partida finalizada\n");
            consola.signal_a();
            consola.signal_a();
            consola.signal_a();
            consola.signal_a();
        }
        mutex.signal_a();
    }
}
