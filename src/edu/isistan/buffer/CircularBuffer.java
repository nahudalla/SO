package edu.isistan.buffer;
/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
*/

public class CircularBuffer<T> implements IBuffer<T> {

	private volatile Object[] elements;
	private volatile int posNext;
	private volatile int lastElem;
	/**
	 * Crea un buffer de 10 elementos
	 */
	public CircularBuffer(){
		this(10);
	}
	/**
	 * Crea un buffer de size elementos
	 * @param size
	 */
	public CircularBuffer(int size){
		this.elements = new Object[size];
		this.lastElem = 0;
		this.posNext = 0;
	}
	@Override
	public T next() {
		while(this.posNext==this.lastElem){
			//Hago nada... esperar
		}
		@SuppressWarnings("unchecked")
		T e = (T) this.elements[this.posNext];
		this.posNext=(this.posNext+1)%this.elements.length;
		return e;
	}

	@Override
	public void add(T data) {
		while(((this.posNext+1)%this.elements.length)==this.lastElem){
			//Hago nada...esperar
		}
		int p = this.lastElem;
		this.lastElem=(this.lastElem+1)%this.elements.length;
		this.elements[p] = data;
	}

	@Override
	public int size() {
		return (this.lastElem-this.posNext)%this.elements.length;
	}

	@Override
	public int maxElements() {
		return this.elements.length;
	}

}
