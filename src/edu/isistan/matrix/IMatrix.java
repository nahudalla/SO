package edu.isistan.matrix;
/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
*/
public interface IMatrix {

	/**
	 * Obtiene el número de filas
	 */
	public int getRows();
	
	/**
	 * Obtiene el número de columnas
	 */
	public int getColumns();
	/**
	 * Asigna un valor a una posición de la matriz
	 * @param row
	 * @param col
	 * @param value
	 */
	public void set(int row, int col, double value);
	/**
	 * Retorna el valor de una posición de la matriz
	 * @param row
	 * @param col
	 * @return
	 */
	public double get(int row, int col);
	/**
	 * Crea una matriz del mismo tipo.
	 * @param rows cantidad de filas.
	 * @param cols cantidad de columnas.
	 * @return
	 */
	public IMatrix createMatrix(int rows, int cols);
}
