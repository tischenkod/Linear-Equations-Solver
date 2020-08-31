package solver;

public class Complex {
    private double real;
    private double imaginary;

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex() {
        real = 0;
        imaginary = 0;
    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex parse(String str)  throws NumberFormatException {
        double real;
        double imaginary;
        String origStr = new String(str);
        try {
            if (str.matches("[+-]?[0-9]+(\\.[0-9]+)?")) { //real only
                real = Double.parseDouble(str);
                imaginary = 0;
            } else if (str.matches("[+-]?[0-9]*(\\.[0-9]+)?i")) { //imaginary only
                real = 0;
                str = str.substring(0, str.length() - 1);
                if (str.equals("+") || str.isEmpty())
                    imaginary = 1;
                else if (str.equals("-"))
                    imaginary = -1;
                else imaginary = Double.parseDouble(str);
            } else if (str.matches("[+-]?[0-9]+(\\.[0-9]+)?[+-][0-9]*(\\.[0-9]+)?i?")) {
                int i;
                for (i = 1; i < str.length(); i++) {
                    if (str.charAt(i) == '+' || str.charAt(i) == '-')
                        break;
                }
                real = Double.parseDouble(str.substring(0, i));
                String imaginaryStr = str.substring(i, str.length() - 1);
                if (imaginaryStr.equals("+"))
                    imaginary = 1;
                else if (imaginaryStr.equals("-"))
                    imaginary = -1;
                else imaginary = Double.parseDouble(imaginaryStr);
            } else
                throw new NumberFormatException("Invalid complex: " + str);
            return new Complex(real, imaginary);
        } catch (Exception e) {
            System.out.println(e.getCause());
            throw new NumberFormatException("Invalid complex: " + origStr);
        }
    }

    @Override
    public String toString() {
        if (imaginary == 0)
            return Double.toString(real);
        return Double.toString(real) + (imaginary > 0 ? "+" : "") + Double.toString(imaginary) + 'i';
    }

    public Complex add(Complex item) {
        return new Complex(getReal() + item.getReal(), getImaginary() + item.getImaginary());
    }

    public Complex divide(Complex divider) {
        double x1 = real;
        double x2 = divider.getReal();
        double y1 = imaginary;
        double y2 = divider.getImaginary();
        return new Complex((x1 * x2 + y1 * y2) / (x2 * x2 + y2 * y2),
                (y1 * x2 - x1 * y2) / (x2 * x2 + y2 * y2));
    }

    public Complex multiply(double multiplier) {
        return new Complex(getReal() * multiplier, getImaginary() * multiplier);
    }

    public Complex multiply(Complex multiplier) {
        return new Complex(getReal() * multiplier.getReal() - getImaginary()*multiplier.getImaginary(),
                getReal() * multiplier.getImaginary() + getImaginary() * multiplier.getReal());
    }

    public double abs() {
        return Math.sqrt(getReal() * getReal() + getImaginary() * getImaginary());
    }
}
