package edu.isistan.matrix.mult;

import edu.isistan.matrix.IMatrix;
/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
*/
public interface IMultiplication {
	/**
	 * Multiplica dos matrices
	 * @param a
	 * @param b
	 * @return
	 */
	public IMatrix multiply(IMatrix a,IMatrix b);
}
