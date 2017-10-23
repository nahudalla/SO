package edu.isistan.matrix;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import edu.isistan.matrix.mult.IMultiplication;
import edu.isistan.matrix.mult.SimpleMultiplication;
/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
*/
public class Utils {

	/*
	 * Inicializador de detección de excepciones no manejadas
	 * */
	static {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {
				System.err.println("Error en el thread: "+arg0.getName()+" ID: "+arg0.getId());
				arg1.printStackTrace();
				System.exit(1);
			}
		});
	}
	
	private static DeadlockDetectionThread DDT;
	
	/**
	 * Compara dos matrices
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equalMatrices(IMatrix a,IMatrix b){
		if (a.getRows()!=b.getRows())
			return false;
		if (a.getColumns()!=b.getColumns())
			return false;
		for (int i=0;i<a.getRows();i++)
			for (int j=0;j<a.getColumns();j++)
				if (a.get(i, j)!=b.get(i, j))
					return false;
		return true;
	}
	/**
	 * Mide el tiempo de ejecución requerido para multiplicar dos matrices.
	 * @param a
	 * @param b
	 * @param m
	 * @return
	 */
	public static long measureTime(IMatrix a,IMatrix b, IMultiplication m){
		long start = System.currentTimeMillis();
		m.multiply(a, b);
		return System.currentTimeMillis()-start;
	}
	/**
	 * Genera una matriz cuadrada densa inicializada al azar
	 * @param size
	 * @return
	 */
	public static IMatrix generateDenseSquareMatrix(int size){
		IMatrix res = new DenseMatrix(size,size);
		for (int i=0;i<res.getRows();i++)
			for (int j=0;j<res.getColumns();j++)
				res.set(i, j, Math.random());
		return res;
	}
	/**
	 * Genera una matriz cuadrada sparse inicializada al azar
	 * @param size
	 * @param sparsity Porcentaje de valores 0
	 * @return
	 */
	public static IMatrix generateSparseSquareMatrix(int size,double sparsity){
		IMatrix res = new SparseMatrix(size,size);
		for (int i=0;i<res.getRows();i++)
			for (int j=0;j<res.getColumns();j++)
				if (Math.random() > sparsity)
					res.set(i, j, Math.random());
		return res;
	}
	/**
	 * Verifica un algoritmo de multiplicación contra
	 * el algoritmo simple. Verifica los respultados tests veces.
	 * @param a
	 * @param b
	 * @param m
	 * @param tests
	 * @return
	 */
	public static boolean verifyMultiplication(IMatrix a,IMatrix b, IMultiplication m, int tests){
		IMatrix resEsp = (new SimpleMultiplication()).multiply(a,b);
		for (int i = 0;i<tests;i++) {
			IMatrix res = m.multiply(a, b);
			if (!equalMatrices(resEsp,res))
				return false;
		}
		return true;
	}
	/**
	 * Activa la detección de deadlocks. Verifica el estado del 
	 * sistema un vez por segundo.
	 * Este método solo detecta deadlocks una vez que ocurren y
	 * no detecta potenciales deadlocks.  
	 * 
	 */
	public synchronized static void activateDeadlockDetection(){
		if(DDT!=null)
			return;
		DDT = new DeadlockDetectionThread();
		DDT.start();
	}
	/**
	 * Desactiva la detección de deadlock, si está se encuentra activada.
	 */
	public synchronized static void deactivateDeadlockDetection(){
		if(DDT!=null) { 
			DDT.setActive(false);
			DDT = null;
		}
	}
	
	private static class DeadlockDetectionThread extends Thread {
		private boolean active = true;

		public DeadlockDetectionThread() {
			super("DeadlockDetection");
			this.setDaemon(true);
		}

		@Override
		public void run() {
			ThreadMXBean bean = ManagementFactory.getThreadMXBean();
			Object wait =  new Object();
			while(this.isActive()){
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

		public synchronized boolean isActive() {
			return active;
		}

		public synchronized void setActive(boolean active) {
			this.active = active;
		}
		
	}
}
