package p3.e2_3;

import edu.isistan.buffer.CircularBuffer;
import edu.isistan.buffer.Consumer;
import edu.isistan.buffer.IBuffer;
import edu.isistan.buffer.Producer;

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
	private static IBuffer<String> buffer = new SyncCircularBuffer<>();

	private static int nProducers = 10;
	private static int produce = 10;
	private static long waitProducer = 100;
	private static int nConsumers = 10;
	private static int consume = 10;
	private static long waitConsumer = 1000;

	/**
	 * Detectar deadlocks automaticamente. 
	 */
	private static boolean deadlockDetection = true;
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
