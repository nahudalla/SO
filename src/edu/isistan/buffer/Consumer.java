package edu.isistan.buffer;

import java.util.concurrent.atomic.AtomicInteger;

/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
*/

public class Consumer extends Thread {
	private static final AtomicInteger ID = new AtomicInteger(1);

	private IBuffer<String> buffer;
	private long waiting;
	private int elemsToConsume;
	
	/**
	 * Crea un nuevo consumidor
	 * @param buffer buffer compartido
	 * @param waiting tiempo de espera. Si es 0 no se espera
	 * @param elemsToConsume cantidad de elementos que se consumiran;
	 */
	public Consumer(IBuffer<String> buffer, long waiting, int elemsToConsume) {
		super("Consumer "+ID.getAndIncrement());
		this.buffer = buffer;
		this.waiting = waiting;
		this.elemsToConsume = elemsToConsume;
	}


	@Override
	public void run() {
		for(int i=0;i<this.elemsToConsume;i++){
			if(this.waiting>0){
				synchronized(this){
					try {
						this.wait(this.waiting);
					} catch (InterruptedException e1) {
						// No debería occurrir nunca
						e1.printStackTrace();
					}
				}
			}
			String e=this.buffer.next();
			System.out.println("El consumidor "+Thread.currentThread().getId()+
					" consumió el elemento "+e);
		}
	}

}
