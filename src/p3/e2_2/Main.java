package p3.e2_2;

import edu.isistan.matrix.IMatrix;
import edu.isistan.matrix.Utils;
import edu.isistan.matrix.mult.IMultiplication;
import edu.isistan.matrix.mult.SimpleMultiplication;

public class Main {
    public static void main(String[] args) {
        matDensa_3();
    }

    private static void matDensa_1() {
        // Promedio en Surface: 31378

        IMatrix m1 = Utils.generateDenseSquareMatrix(1500),
                m2 = Utils.generateDenseSquareMatrix(1500);
        IMultiplication mult = new SimpleMultiplication();
        long time = 0;

        for (int i = 0; i < 10; i++) {
            time += Utils.measureTime(m1, m2, mult);
        }

        System.out.printf("MatrizDensa 1: %d\n", (time/10));
    }

    private static void matDensa_2() {
        // Promedio en Surface: 16114

        IMatrix m1 = Utils.generateDenseSquareMatrix(1500),
                m2 = Utils.generateDenseSquareMatrix(1500);
        IMultiplication mult = new ThreadedMultiplication();

        System.out.print("Realizando prueba de funcionamiento...");

        if(Utils.verifyMultiplication(m1, m2, mult, 10)) {
            System.out.println(" OK");
            System.out.println("Realizando medicion de tiempo...");

            long time = 0;

            for (int i = 0; i < 10; i++) {
                time += Utils.measureTime(m1, m2, mult);
            }

            System.out.printf("MatrizDensa 2 tomó: %d\n", (time / 10));
        } else {
            System.out.println(" ERROR");
        }
    }

    private static void matDensa_3() {
        // Promedio en Surface: 15864

        IMatrix m1 = Utils.generateDenseSquareMatrix(1500),
                m2 = Utils.generateDenseSquareMatrix(1500);
        IMultiplication mult = new ThreadedMultiplication();

        System.out.print("Realizando prueba de funcionamiento...");

        if(Utils.verifyMultiplication(m1, m2, mult, 1)) {
            System.out.println(" OK");
            System.out.println("Realizando medicion de tiempo...");

            long time = 0;

            for (int i = 0; i < 10; i++) {
                time += Utils.measureTime(m1, m2, mult);
            }

            System.out.printf("MatrizDensa 2 tomó: %d\n", (time / 10));
        } else {
            System.out.println(" ERROR");
        }
    }

    private static void matRala_1() {
        // TODO: Promedio en Surface: (1 ejecucion: 30247)

        IMatrix m1 = Utils.generateSparseSquareMatrix(650, 0.9),
                m2 = Utils.generateSparseSquareMatrix(650, 0.9);
        IMultiplication mult = new SimpleMultiplication();

        long time = 0;

        for (int i = 0; i < 10; i++) {
            time += Utils.measureTime(m1, m2, mult);
        }

        System.out.printf("MatrizRala 1: %d\n", (time/10));
    }

    private static void matRala_2() {
        // Promedio en Surface: 6506

        IMatrix m1 = Utils.generateSparseSquareMatrix(650, 0.9),
                m2 = Utils.generateSparseSquareMatrix(650, 0.9);
        IMultiplication mult = new ThreadSafeMultiplication();

        System.out.print("Realizando prueba de funcionamiento...");

        if(Utils.verifyMultiplication(m1, m2, mult, 10)) {
            System.out.println(" OK");
            System.out.println("Realizando medicion de tiempo...");

            long time = 0;

            for (int i = 0; i < 10; i++) {
                time += Utils.measureTime(m1, m2, mult);
            }

            System.out.printf("MatrizRala 2 tomó: %d\n", (time / 10));
        } else {
            System.out.println(" ERROR");
        }
    }
}
