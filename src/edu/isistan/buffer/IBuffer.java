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

public interface IBuffer<T> {
	/**
	 * Retorna el siguiente elemento
	 * se bloquea si no hay elementos
	 * @return
	 */
	public T next();
	/**
	 * Agrega un elemento
	 * se bloquea si no hay espacio para agregarlo
	 * @return
	 */
	public void add(T data);
	/**
	 * Retorna la cantidad de elementos
	 * @return
	 */
	public int size();
	/**
	 * Retorna la cantidad maxima de elementos
	 * @return
	 */
	public int maxElements();

}
