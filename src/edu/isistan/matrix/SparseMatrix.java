package edu.isistan.matrix;

import java.util.Map;
import java.util.HashMap;
/** Este código es distribuido como parte de un trabajo práctico de
 *  la materia Sistemas Operativos I dictada por la de Ciencias Exactas de
 *  la Universidad nacional del centro de la provincia de Buenos Aires (UNICEN).
 *  El código no debe usarse en ningún otro proyecto debido a que contiene o 
 *  puede contener malas prácticas y errores introducidos intencionalmente con 
 *  fines didácticos. Así mismo el código carece de cualquier tipo de optimización
 *  primando la legibilidad del mismo.
 *  @author Dr. Juan Manuel Rodriguez
 *  Implementación de una matriz rala @see edu.isistan.matrix.IMatrix
*/
public class SparseMatrix implements IMatrix {
	
	/*
	 * Almacena los valores no cero de la matriz organizados
	 * por columnas
	 */
	protected Map<Integer,Map<Integer,Double>> values;
	/*
	 * Almacena el número de filas
	 */
	protected int rows;
	/*
	 * Almacena el número de columnas
	 */
	protected int columns;
	public SparseMatrix(int rows,int cols){
		this.rows = rows;
		this.columns = cols;
		if (rows<=0)
			throw new RuntimeException("La matriz debe tener al menos una fila");
		if (cols<=0)
			throw new RuntimeException("La matriz debe tener al menos una columna");
		this.values = new HashMap<>();
	}
	@Override
	public int getRows() {
		return this.rows;
	}

	@Override
	public int getColumns() {
		return this.columns;
	}

	@Override
	public void set(int row, int col, double value) {
		if (value==0){
			if (this.values.containsKey(col)) {
				Map<Integer,Double> mcol = this.values.get(col);
				mcol.remove(row);
				if (mcol.size()==0)
					this.values.remove(col);
			}
		} else {
			Map<Integer,Double> mcol = null;
			if (this.values.containsKey(col)) {
				mcol = this.values.get(col);
			} else {
				mcol = new HashMap<>();
				this.values.put(col, mcol);
			}
			mcol.put(row, value);
		}
	}

	@Override
	public double get(int row, int col) {
		Map<Integer,Double> mcol = this.values.get(col);
		if (mcol!=null){
			Double d = mcol.get(row);
			if (d!=null)
				return d;
		}
		return 0;
	}

	@Override
	public IMatrix createMatrix(int rows, int cols) {
		return new SparseMatrix(rows,cols);
	}

}
