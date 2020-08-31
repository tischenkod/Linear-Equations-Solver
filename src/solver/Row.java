package solver;

import java.util.Locale;

public class Row {
    private Complex[] items;

    public Row(Complex[] items) {
        this.items = items;
    }

    public Complex get(int i) {
        return items[i];
    }

    public void set(int i, Complex value) {
        items[i] = value;
    }

    int size() {
        return items.length;
    }

    public void print() {
        for (Complex item :
                items) {
            System.out.print(item.toString() + " ");
        }
    }

    public void toOne(int pos) {
        Complex divider = items[pos];
        for (int i = pos; i < items.length; i++) {
            items[i] =  items[i].divide(divider);
        }
    }

    public double leftAbsSum() {
        double sum = 0;
        for (int i = 0; i < items.length - 1; i++) {
            sum += items[i].abs();
        }
        return sum;
    }
}
