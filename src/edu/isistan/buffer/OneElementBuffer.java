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

public class OneElementBuffer<T> implements IBuffer<T> {

	private volatile T element;
	
	@Override
	public T next() {
		while(this.element==null){
			//Esperar
		}
		T res = this.element;
		this.element = null;
		return res;
	}

	@Override
	public void add(T data) {
		while(this.element!=null){
			//Esperar
		}
		this.element = data;
	}

	@Override
	public int size() {
		return this.element==null ? 0:1;
	}

	@Override
	public int maxElements() {
		return 1;
	}

}
