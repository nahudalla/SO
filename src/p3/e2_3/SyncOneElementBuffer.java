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

public class SyncOneElementBuffer<T> implements IBuffer<T> {

	private T element;

	private final Object not_full = new Object();
	private final Object not_empty = new Object();

	@Override
	public T next() {
	    T res;
	    synchronized (this.not_empty) {
            while (this.element == null) {
                try {
                    this.not_empty.wait();
                } catch (InterruptedException e) {
                    // Should not happen
                    e.printStackTrace();
                    return null;
                }
            }
            synchronized (this) {
                res = this.element;
                this.element = null;
            }
        }
        synchronized (this.not_full) {
            this.not_full.notify();
        }
        return res;
    }

	@Override
	public void add(T data) {
	    synchronized (this.not_full) {
            while (this.element != null) {
                try {
                    this.not_full.wait();
                } catch (InterruptedException e) {
                    // Should not happen
                    e.printStackTrace();
                    return;
                }
            }
            synchronized (this) {
                this.element = data;
            }
        }
        synchronized (this.not_empty) {
            this.not_empty.notify();
        }
    }

	@Override
	public synchronized int size() {
		return this.element==null ? 0:1;
	}

	@Override
	public int maxElements() {
		return 1;
	}
}
