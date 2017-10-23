package edu.isistan.matrix;
/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
 *  Implementación de una matriz densa @see edu.isistan.matrix.IMatrix
*/
public class DenseMatrix implements IMatrix {

	private double[][] values;
	
	public DenseMatrix(int rows,int cols){
		if (rows<=0)
			throw new RuntimeException("La matriz debe tener al menos una fila");
		if (cols<=0)
			throw new RuntimeException("La matriz debe tener al menos una columna");
		this.values = new double[rows][cols];
	}
	@Override
	public int getRows() {
		return this.values.length;
	}

	@Override
	public int getColumns() {
		return this.values[0].length;
	}

	@Override
	public void set(int row, int col, double value) {
		this.values[row][col] = value;
	}

	@Override
	public double get(int row, int col) {
		return this.values[row][col];
	}

	@Override
	public IMatrix createMatrix(int rows, int cols) {
		return new DenseMatrix(rows,cols);
	}

}
