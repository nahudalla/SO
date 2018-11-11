package parcial2018;

import p4.e5.Semaphore;

public class Parcial2018 {
    private static Semaphore mutex_for_paralelo = new Semaphore(1);
    private static int i_for_paralelo;

    public static void main(String[] args) {
        int cantThreads = 4;

        String[] usuarios = {
                "Nahuel",
                "Noelia",
                "Camila",
                "Bruno",
                "Lucrecia",
                "Mauro",
                "Alejandro",
                "Mercedes",
                "Sofia",
                "Nicolas",
                "Luciano",
                "Julieta",
                "Federico",
                "Rosario",
                "Catalina",
                "Selena",
                "Britney",
                "Mora",
                "Lua",
                "Mia",
                "Dua",
                "Sebastian",
                "Matias"
        };

        Thread[] threads = new Thread[cantThreads];

        i_for_paralelo = 0; // indice por el cual empieza el for paralelo

        for (int i = 0; i < cantThreads; i++) {
            final int final_i = i;
            threads[i] = new Thread(() -> {
                System.out.println("Thread " + final_i + " iniciado.");
                while(true) { // repito por siempre, corto con break cuando el thread termina
                    // Intento obtener un usuario para procesar:
                    mutex_for_paralelo.wait_a(); // adquiero acceso a mutex
                        if(i_for_paralelo >= usuarios.length) {
                            mutex_for_paralelo.signal_a(); // IMPORTANTE: libero el mutex antes de terminar
                            break; // termino si no hay mas usuarios que procesar
                        }
                        int indice_usuario = i_for_paralelo; // me quedo con el indice actual para procesar ese usuario
                        i_for_paralelo++; // incremento el proximo indice a procesar
                    mutex_for_paralelo.signal_a(); // libero el mutex

                    // aca ya tengo un usuario para procesar: usuarios[indice_usuario]
                    System.out.println("Thread " + final_i + " procesa el usuario " + usuarios[indice_usuario]);

                    // hago algo con el usuario
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < cantThreads; i++) {
            try {
                threads[i].join();
                System.out.println("Thread liberado: " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
