package p3.e2_2;

import edu.isistan.matrix.IMatrix;

public class ThreadSafeMultiplication extends ThreadedMultiplication {
    @Override
    protected void calculateCell(IMatrix a, IMatrix b, IMatrix res, int row, int col, int mulLength) {
        double acum = 0;
        for (int k = 0; k < mulLength; ++k)
            acum += a.get(row, k) * b.get(k, col);

        synchronized (res) {
            res.set(row, col, acum);
        }
    }
}
