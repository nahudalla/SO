package p4.e8_diccionario;

import p4.e5.Semaphore;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Diccionario {
    public static void main(String[] args) {
        String _palabras = "hola,chau,que,tal,biyuya,tarasca,ornitorrinco,huesito,camaleon," +
                "botella,pared,columna,estante,foco,luz,cielo,data,center,datacenter," +
                "video,conferencia,videoconferencia,caño,estudio,libro,resaltador," +
                "auriculares,mochilas,esdrújula,árbol,más,sí,baile,bailar,bailó,bailamos," +
                "está,cultura,exactas,ingeniería";

        final String palabras[] = _palabras.split(",");
        final Diccionario diccionario = new Diccionario();

        Thread modificadores[] = new Thread[5];
        Thread comprobadores[] = new Thread[5];

        final Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            Thread modificador = new Thread(() -> {
                String palabra;
                while (true) {
                    try {
                        Thread.sleep(2000 + random.nextInt(5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    palabra = palabras[random.nextInt(palabras.length)];
                    if (random.nextInt(2) == 1) {
                        System.out.printf("Thread %d agregando %s\n", finalI, palabra);
                        diccionario.agregar(palabra);
                    } else {
                        System.out.printf("Thread %d eliminando %s\n", finalI, palabra);
                        diccionario.eliminar(palabras[random.nextInt(palabras.length)]);
                    }
                }
            });
            modificador.start();
            modificadores[i] = modificador;
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i + 5;
            Thread comprobador = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(2000 + random.nextInt(5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int tam = random.nextInt(palabras.length);
                    Set<String> palabrasAVerificar = new HashSet<>(tam);
                    while(palabrasAVerificar.size() != tam) {
                        palabrasAVerificar.add(
                                palabras[random.nextInt(palabras.length)]
                        );
                    }

                    if(random.nextInt(2) == 1) {
                        System.out.printf(
                                "Thread %d verificando consistentemente las palabras (%d): %s\n",
                                finalI,
                                palabrasAVerificar.size(),
                                String.join(", ", palabrasAVerificar)
                        );
                        Set<String> res = diccionario.verificar_consistente(palabrasAVerificar);
                        System.out.printf("Thread %d resultado (%d): %s\n", finalI, res.size(), String.join(", ", res));
                    } else {
                        System.out.printf(
                                "Thread %d verificando inconsistentemente las palabras (%d): %s\n",
                                finalI,
                                palabrasAVerificar.size(),
                                String.join(", ", palabrasAVerificar)
                        );
                        Set<String> res = diccionario.verificar_inconsistente(palabrasAVerificar);
                        System.out.printf("Thread %d resultado (%d): %s\n", finalI, res.size(), String.join(", ", res));
                    }
                }
            });
            comprobador.start();
            comprobadores[i] = comprobador;
        }
    }

    private final Set<String> palabras = new HashSet<>();

    private final Semaphore rw_mutex = new Semaphore(1);
    private final Semaphore mutex = new Semaphore(1);
    private long readers_count = 0;

    public void agregar(String palabra) {
        this.rw_mutex.wait_a();

        this.palabras.add(palabra);

        this.rw_mutex.signal_a();
    }

    public void eliminar(String palabra) {
        this.rw_mutex.wait_a();

        this.palabras.remove(palabra);

        this.rw_mutex.signal_a();
    }

    public Set<String> verificar_inconsistente(Set<String> palabras) {
        Set<String> resultado = new HashSet<>();

        for (String palabra : palabras) {
            this.mutex.wait_a();
            this.readers_count++;
            if(this.readers_count == 1)
                this.rw_mutex.wait_a();
            this.mutex.signal_a();

            this.verificar_palabra(palabra, resultado);

            this.mutex.wait_a();
            this.readers_count--;
            if(this.readers_count == 0)
                this.rw_mutex.signal_a();
            this.mutex.signal_a();
        }

        return resultado;
    }

    public Set<String> verificar_consistente(Set<String> palabras) {
        Set<String> resultado = new HashSet<>();

        this.mutex.wait_a();
        this.readers_count++;
        if(this.readers_count == 1)
            this.rw_mutex.wait_a();
        this.mutex.signal_a();

        for (String palabra : palabras)
            this.verificar_palabra(palabra, resultado);

        this.mutex.wait_a();
        this.readers_count--;
        if(this.readers_count == 0)
            this.rw_mutex.signal_a();
        this.mutex.signal_a();

        return resultado;
    }

    private void verificar_palabra(String palabra, Set<String> resultado) {
        if(!this.palabras.contains(palabra))
            resultado.add(palabra);
    }
}
