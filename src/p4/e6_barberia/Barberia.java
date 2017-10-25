package p4.e6_barberia;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Barberia {
    public static void main(String[] args) {
        final Barberia barberia = new Barberia(5);
        final Random random = new Random();

        Thread barbero = new Thread(() -> {
            while(true) {
                barberia.atender("Barbero 1");
            }
        });
        barbero.start();

        long i = 0;
        while(true) {
            try {
                int wait = 1000 + random.nextInt(2000);
                System.out.printf("Nuevo cliente en %d milisegundos\n", wait);
                System.out.flush();
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final long iFinal = i++;
            Thread cliente = new Thread(() -> {
                barberia.nuevoCliente("Cliente " + iFinal);
            });
            cliente.start();
        }
    }

    private static final int WAIT_TIME = 5000;

    private final int nSillas;
    private int clientesEsperando = 0;
    private Queue<String> clientes;

    public Barberia(int nSillas) {
        this.nSillas = nSillas;
        this.clientes = new ArrayDeque<>(nSillas);
    }

    public void atender(String nombreBarbero) {
        String nombreCliente;
        synchronized (this) {
            this.clientesEsperando--;
            if (this.clientesEsperando < 0) {
                try {
                    System.out.printf("%s se va a dormir\n", nombreBarbero);
                    System.out.flush();
                    this.wait();
                    System.out.printf("%s despertó\n", nombreBarbero);
                } catch (InterruptedException e) {
                    // Should not happen
                    e.printStackTrace();
                }
            }

            nombreCliente = this.clientes.remove();
        }
        System.out.printf("%s atendiendo a cliente %s...\n", nombreBarbero, nombreCliente);
        System.out.flush();
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            // Should not happen.
            e.printStackTrace();
        }
        System.out.printf("%s terminó de atender a %s\n", nombreBarbero, nombreCliente);
        System.out.flush();
    }

    public synchronized boolean nuevoCliente(String nombreCliente) {
        System.out.printf("%s ingresa\n", nombreCliente);
        if(this.clientesEsperando == this.nSillas) {
            System.out.printf("%s se va por falta de lugar\n", nombreCliente);
            System.out.flush();
            return false;
        }
        System.out.printf("%s esperando a ser atendido...\n", nombreCliente);
        System.out.flush();

        this.clientesEsperando++;
        this.clientes.add(nombreCliente);

        if(this.clientesEsperando <= 0)
            this.notify();

        return true;
    }
}
