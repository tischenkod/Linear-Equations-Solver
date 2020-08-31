package solver;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        args = new String[] {"-in", "Linear Equations Solver\\task\\src.txt",
//                "-out", "Linear Equations Solver\\task\\output.txt"};
        File input = new File(args[1]);
        File output = new File(args[3]);
        int varCount;
        int eqCount;
        Matrix matrix;
        try(Scanner scanner = new Scanner(input)) {
            varCount = scanner.nextInt();
            eqCount = scanner.nextInt();
            scanner.nextLine();
            matrix = new Matrix(varCount, eqCount);
            for (int i = 0; i < eqCount; i++) {
                matrix.addRow(Arrays.stream(scanner.nextLine().split(" ")).map(item -> Complex.parse(item)).toArray(Complex[]::new));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error open input file: " + input.getAbsoluteFile());
            return;
        }
        try (FileOutputStream writer = new FileOutputStream(output)) {
            PrintStream printStream = new PrintStream(writer);

            System.out.println("Input matrix:\n");
            matrix.print();
            matrix.toEchelonForm();

            switch (matrix.solutionCount()) {
                case -1:
                    System.out.println("Infinitely many solutions");
                    printStream.println("Infinitely many solutions");
                    break;
                case 0:
                    System.out.println("No solutions");
                    printStream.println("No solutions");
                    break;
                case 1:
                    matrix.toUnitForm();
                    System.out.println("Solution:\n");
                    matrix.printColumn(varCount);
                    matrix.printColumn(varCount, printStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error open output file: " + output.getAbsoluteFile());
            return;
        }



    }
}
