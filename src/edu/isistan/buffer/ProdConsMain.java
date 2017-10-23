package edu.isistan.buffer;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
*/

public class ProdConsMain {

	/**
	 * buffer actual de la prueba. Se puede variar por cualquier implementación
	 * de IBuffer. Base:
	 * + ArrayListBuffer
	 * + CircularBuffer
	 * + OneElementBuffer
	 */
	public static IBuffer<String> buffer = new CircularBuffer<>();
	/**
	 * Tiempo de espera en milisegundos para consumir un 
	 * elemento. Si es 0 no se espera nada.
	 * Probar distintos tiempos: 0, 100, 1000
	 */
	public static long waitConsumer = 100;
	/**
	 * Tiempo de espera en milisegundos para producir un 
	 * nuevo elemento. Si es 0 no se espera nada.
	 * Probar distintos tiempos: 0, 100, 1000
	 */
	public static long waitProducer = 100;
	/**
	 * Cantidad de elementos que produce un 
	 * productor. 
	 * Variar para analizar el resultado de la ejecución
	 * entre 10 y 1000. Use 1000 solo con tiempos de espera
	 * cortos.
	 * produce * nProducer = consume * nConsumer
	 */
	private static int produce = 100;
	/**
	 * Cantidad de elementos que consume un 
	 * consumidor.
	 * Variar para analizar el resultado de la ejecución
	 * entre 10 y 1000. Use 1000 solo con tiempos de espera
	 * cortos.
	 * produce * nProducer = consume * nConsumer
	 */
	private static int consume = 100;
	/**
	 * Cantidad de productores
	 * produce * nProducer = consume * nConsumer
	 */
	private static int nProducers = 1;
	/**
	 * Cantidad de consumidores
	 * produce * nProducer = consume * nConsumer
	 */
	private static int nConsumers = 1;
	/**
	 * Detectar deadlocks automaticamente. 
	 */
	private static boolean deadlockDetection = false;
	/* ***************************************************************
	 * No modificar a partir de este punto!!
	 * ***************************************************************
	 */
	public static void main(String[] args) {
		if(deadlockDetection){
			DeadlockDetectionThread ddt = new DeadlockDetectionThread();
			ddt.start();
		}
		executeTest();
	}

	/**
	 * Ejecuta el test con la configuración actual de la clase
	 */
	public static void executeTest() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {
				System.err.println("Error en el thread: "+arg0.getName());
				arg1.printStackTrace();
				System.exit(1);
			}
		});
		
		Thread[] threads = new Thread[nProducers+nConsumers];
		for(int i=0;i<nProducers;i++){
			Producer p = new Producer(buffer,waitProducer,produce);
			p.start();
			threads[i] = p;
		}
		for(int i=0;i<nConsumers;i++){
			Consumer c = new Consumer(buffer,waitConsumer,consume);
			c.start();
			threads[i+nProducers] = c;
		}
		try {
			for(Thread t:threads)
				t.join();
		} catch (InterruptedException e) {
			System.err.println("Error en el thread principal: ");
			e.printStackTrace();
		}
		System.out.println("La cantidad de elementos en el buffer es "+buffer.size());
	}

	
	private static class DeadlockDetectionThread extends Thread {

		public DeadlockDetectionThread() {
			super("DeadlockDetection");
			this.setDaemon(true);
		}

		@Override
		public void run() {
			ThreadMXBean bean = ManagementFactory.getThreadMXBean();
			Object wait =  new Object();
			while(true){
				long[] threadIds = bean.findDeadlockedThreads(); 
				if (threadIds != null) {
					ThreadInfo[] infos = bean.getThreadInfo(threadIds);
					System.err.println("Deadlock detectado!!!");
					for (ThreadInfo info : infos) {
						System.err.println(info);
					}
					System.exit(1);
				}
				synchronized (wait) {
					try {
						wait.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}
