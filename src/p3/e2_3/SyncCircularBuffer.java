package p3.e2_3;

import edu.isistan.buffer.IBuffer;

/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
*/

public class SyncCircularBuffer<T> implements IBuffer<T> {

	private Object[] elements;
	private int posNext;
	private int lastElem;

	private final Object not_full = new Object();
	private final Object not_empty = new Object();
	/**
	 * Crea un buffer de 10 elementos
	 */
	public SyncCircularBuffer(){
		this(10);
	}
	/**
	 * Crea un buffer de size elementos
	 * @param size
	 */
	public SyncCircularBuffer(int size){
		this.elements = new Object[size];
		this.lastElem = 0;
		this.posNext = 0;
	}
	@Override
	public T next() {
	    T e;
	    synchronized (this.not_empty) {
            while (this.posNext == this.lastElem) {
                try {
                    this.not_empty.wait();
                } catch (InterruptedException e1) {
                    // Should not happen
                    e1.printStackTrace();
                    return null;
                }
            }
            synchronized (this) {
                e = (T) this.elements[this.posNext];
                this.posNext = (this.posNext + 1) % this.elements.length;
            }
        }
        synchronized (this.not_full) {
	        this.not_full.notify();
        }
		return e;
	}

	@Override
	public void add(T data) {
	    synchronized (this.not_full) {
            while (((this.posNext + 1) % this.elements.length) == this.lastElem) {
                try {
                    this.not_full.wait();
                } catch (InterruptedException e) {
                    // Should not happen
                    e.printStackTrace();
                    return;
                }
            }
            synchronized (this) {
                int p = this.lastElem;
                this.lastElem = (this.lastElem + 1) % this.elements.length;
                this.elements[p] = data;
            }
        }
        synchronized (this.not_empty) {
	        this.not_empty.notify();
        }
	}

	@Override
	public synchronized int size() {
		return (this.lastElem-this.posNext)%this.elements.length;
	}

	@Override
	public int maxElements() {
		return this.elements.length;
	}

}
