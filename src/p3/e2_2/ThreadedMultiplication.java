package p3.e2_2;

import edu.isistan.matrix.IMatrix;
import edu.isistan.matrix.mult.IMultiplication;

public class ThreadedMultiplication implements IMultiplication {
    private static final int processors = Runtime.getRuntime().availableProcessors();

    @Override
    public IMatrix multiply(IMatrix a, IMatrix b) {
        Thread threads[] = new Thread[processors];
        final int rowStart[] = new int[processors],
                  colStart[] = new int[processors],
                  rowEnd[]   = new int[processors],
                  colEnd[]   = new int[processors];

        final int
                resRows = a.getRows(),
                resCols = b.getColumns(),
                mulLength = b.getRows(),
                resElems = resRows * resCols;

        final double elemsPerThread = resElems/processors;

        final IMatrix res = a.createMatrix(resRows, resCols);

        rowStart[0] = 0;
        colStart[0] = 0;

        rowEnd[0] = (int) elemsPerThread / resRows;
        colEnd[0] = (int) (elemsPerThread - (rowEnd[0] * resCols)) - 1;

        if(colEnd[0] < 0) {
            colEnd[0] = resCols - 1;
            rowEnd[0]--;
        }

        for (int i = 0; i < processors; i++) {
            if (i != 0) {
                rowStart[i] = rowEnd[i-1];
                colStart[i] = colEnd[i-1] + 1;
                if (colStart[i] >= resCols) {
                    colStart[i] = 0;
                    rowStart[i]++;
                }

                if(i == (processors - 1)) {
                    rowEnd[i] = resRows - 1;
                    colEnd[i] = resCols - 1;
                } else {
                    int nRows, elemsLeft = (int) elemsPerThread;

                    rowEnd[i] = rowStart[i];
                    colEnd[i] = colStart[i];

                    if (colStart[i] != 0) {
                        int leftInRow = (resCols - colStart[i]);
                        if (leftInRow >= elemsLeft) {
                            colEnd[i] += (elemsLeft - 1);
                            elemsLeft = 0;
                        } else {
                            elemsLeft -= leftInRow;
                            rowEnd[i]++;
                        }
                    }

                    if(elemsLeft > 0) {
                        nRows = (int) (elemsLeft / resCols);
                        rowEnd[i] += nRows;
                        elemsLeft = elemsLeft - (nRows * resCols);

                        if (elemsLeft == 0) {
                            rowEnd[i]--;
                            colEnd[i] = resCols - 1;
                        } else {
                            colEnd[i] = elemsLeft - 1;
                        }
                    }
                }
            }

            final int i_f = i;
            threads[i] = new Thread(() -> {
                for(int row = rowStart[i_f]; row <= rowEnd[i_f]; ++row)
                    for(
                            int col = (row == rowStart[i_f] ? colStart[i_f] : 0);
                            col <= (row == rowEnd[i_f] ? colEnd[i_f] : resCols-1);
                            ++col
                    ) this.calculateCell(a, b, res, row, col, mulLength);
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    protected void calculateCell(IMatrix a, IMatrix b, IMatrix res, int row, int col, int mulLength) {
        for (int k = 0; k < mulLength; ++k)
            res.set(
                    row,
                    col,
                    res.get(row, col) + (a.get(row, k) * b.get(k, col))
            );
    }
}
