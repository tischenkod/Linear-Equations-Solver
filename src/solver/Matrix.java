package solver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix {
    private final List<Row> rows;
    private boolean isEchelonForm = false;
    private final int[] varPos;

    public Matrix(int varCount, int eqCount) {
        rows = new ArrayList<>(eqCount);
        varPos = new int[varCount];
        for (int i = 0; i < varCount; i++) {
            varPos[i] = i;
        }
    }

    public void toEchelonForm() {
        if (isEchelonForm)
            return;
        for (int echelonPos = 0; echelonPos < rows.size(); echelonPos++) {
            int nonZeroRow;
            int xOffset = 0;
            while ((nonZeroRow = findNonZero(echelonPos, xOffset)) < 0) {
                xOffset++;
                if (echelonPos + xOffset >= rows.get(0).size() - 1) {
                    isEchelonForm = true;
                    return;
                }
            }
            if (xOffset > 0)
                swapColumns(echelonPos, echelonPos + xOffset);
            if (nonZeroRow > echelonPos)
                swapRows(nonZeroRow, echelonPos);
            for (int i = echelonPos + 1; i < rows.size(); i++) {
                subtractWithMultiplication(echelonPos, rows.get(i).get(echelonPos).divide(rows.get(echelonPos).get(echelonPos)).multiply(-1), i);
            }
            rows.get(echelonPos).toOne(echelonPos);
            print();
        }
        isEchelonForm = true;
    }

    int solutionCount() {
        int zeroCount = 0;
        for (int i = rows.size() - 1; i >= 0; i--) {
            Row row = rows.get(i);
            if (row.leftAbsSum() <= 0.001) {
                if (row.get(row.size() - 1).abs() > 0.001)
                    return 0;
                else
                    zeroCount++;
            } else
                break;
        }
        if (rows.size() - zeroCount < rows.get(0).size() - 1)
            return -1;
        return 1;
    }

    //not safe for infinity or zero solutions count
    public void toUnitForm() {
        if (!isEchelonForm)
            toEchelonForm();
        for (int unitPos = rows.size() - 1; unitPos > 0; unitPos--) {
            for (int dstRowId = unitPos - 1; dstRowId >= 0; dstRowId--) {
                subtractWithMultiplication(unitPos, rows.get(dstRowId).get(unitPos).multiply(-1), dstRowId);
            }
        }
        print();
    }

    void subtractWithMultiplication(int src, Complex multiplier, int dst) {
        for (int i = 0; i < rows.get(0).size(); i++) {
            Row dstRow = rows.get(dst);
            Row srcRow = rows.get(src);
            dstRow.set(i, dstRow.get(i).add(srcRow.get(i).multiply(multiplier)));
        }
    }

    private void swapRows(int first, int second) {
        isEchelonForm = false;
        Collections.swap(rows, first, second);
    }

    private void swapColumns(int first, int second) {
        Complex tmpC;
        for (Row row :
                rows) {
            tmpC = row.get(first);
            row.set(first, row.get(second));
            row.set(second, tmpC);
        }
        int tmpI = varPos[first];
        varPos[first] = varPos[second];
        varPos[second] = tmpI;
    }

    private int findNonZero(int startFrom, int xOffset) {
        int i;
        for (i = startFrom; i < rows.size(); i++) {
            if (rows.get(i).get(startFrom + xOffset).abs() > 0.001)
                break;
        }
        return i == rows.size() ? -1 : i;
    }

    public void addRow(Complex[] items) {
        rows.add(new Row(items));
    }

    public void print() {
        for (Row row :
                rows) {
            row.print();
            System.out.println();
        }
        System.out.println();
    }

    public void printColumn(int columnId) {
        printColumn(columnId, System.out);
    }

    public void printColumn(int columnId, PrintStream writer) {
        for (int i = 0, varCount = rows.get(0).size() - 1; i < varCount; i++) {
            Row row = rows.get(varPos[i]);
            writer.println(row.get(columnId).toString());
        }
    }
}
