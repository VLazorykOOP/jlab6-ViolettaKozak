import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.Exception;
import java.util.Random;

public class Second extends JFrame {
    private static final int MATRIX_SIZE = 15;
    private static final String FILENAME = "input.txt";
    private double[][] A;
    private double[][] B;
    private int[] C;
    void main(){
        start();
        setTitle("Second task");
        setSize(1000, 1000);
        setBackground(Color.lightGray);
        setVisible(true);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("Enter the path to input file:");
        label.setBounds(10, 10, 200, 25);
        panel.add(label);

        JTextField inputFileField = new JTextField();
        inputFileField.setBounds(220, 10, 250, 25);
        panel.add(inputFileField);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(10, 40, 100, 25);
        panel.add(calculateButton);
        calculateButton.addActionListener(e -> {
            try {
                readInputData(inputFileField.getText());
                calculateVectorC();
                displayResult();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InvalidInputDataException ex) {
                ex.printStackTrace();
            } catch (CustomException ex) {
                ex.printStackTrace();
            }
        });

        getContentPane().add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
    private void readInputData(String filename) throws IOException, InvalidInputDataException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        A = new double[MATRIX_SIZE][MATRIX_SIZE];
        B = new double[MATRIX_SIZE][MATRIX_SIZE];

        for (int i = 0; i < MATRIX_SIZE; i++) {
            String[] lineA = reader.readLine().split(" ");
            String[] lineB = reader.readLine().split(" ");

            if (lineA.length != MATRIX_SIZE || lineB.length != MATRIX_SIZE) {
                throw new InvalidInputDataException();
            }

            for (int j = 0; j < MATRIX_SIZE; j++) {
                A[i][j] = Double.parseDouble(lineA[j]);
                B[i][j] = Double.parseDouble(lineB[j]);
            }
        }
    }
    private void calculateVectorC() throws CustomException {
        C = new int[MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            boolean allElementsGreaterThan = true;

            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (A[i][j] <= B[i][j]) {
                    allElementsGreaterThan = false;
                    break;
                }
            }
            if (allElementsGreaterThan) {
                C[i] = 1;
            } else {
                C[i] = 0;
            }
        }
        int numOnes = 0;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            if (C[i] == 1) {
                numOnes++;
            }
        }
        if (numOnes > MATRIX_SIZE / 2) {
            throw new CustomException("More than half of vector X is 1");
        }
    }

    private void displayResult() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        // create table model for matrix A
        String[] columnNames = new String[MATRIX_SIZE+1];
        for (int i = 0; i < MATRIX_SIZE+1; i++) {
            columnNames[i] = Integer.toString(i);
        }
        JTable tableA = new JTable(15, 16);
        DefaultTableModel a = new DefaultTableModel(columnNames, MATRIX_SIZE);
        tableA.setModel(a);
        for (int i=0; i<15;i++){
            for (int j = 0; j<15;j++){
                tableA.setValueAt(A[i][j], i, j+1);
                tableA.setValueAt(columnNames[j+1], j, 0);
            }
        }
        JScrollPane scrollPaneA = new JScrollPane(tableA);
        scrollPaneA.setBounds(10, 100, 700, 264);
        resultPanel.add(scrollPaneA);

        JTable tableB = new JTable(15, 16);
        tableB.setModel(a);
        for (int i=0; i<15;i++){
            for (int j = 0; j<15;j++){
                tableB.setValueAt(B[i][j], i, j+1);
                tableB.setValueAt(columnNames[j+1], j, 0);
            }
        }
        JScrollPane scrollPaneB = new JScrollPane(tableB);
        scrollPaneB.setBounds(10, 420, 700, 264);
        resultPanel.add(scrollPaneB);

        // add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(720, 365, 150, 50);
        refreshButton.addActionListener(e -> {
            try {
                readInputData(FILENAME);
                calculateVectorC();
                displayResult();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InvalidInputDataException ex) {
                ex.printStackTrace();
            } catch (CustomException ex) {
                ex.printStackTrace();
            }
        });
        resultPanel.add(refreshButton);

        String vectorXStr = "Vector C: [ ";
        for (int i = 0; i < MATRIX_SIZE; i++) {
            vectorXStr += C[i] + " ";
        }
        vectorXStr += "]";
        JLabel v = new JLabel(vectorXStr);
        v.setBounds(10, 10, 250, 50);
        resultPanel.add(v);
        JLabel a1 = new JLabel("Matrix A");
        a1.setBounds(310, 50, 100, 50);
        resultPanel.add(a1);
        JLabel b = new JLabel("Matrix B");
        b.setBounds(350, 380, 100, 50);
        resultPanel.add(b);
        JLabel vectorXLabel = new JLabel("");
        resultPanel.add(vectorXLabel);

        getContentPane().removeAll();
        getContentPane().add(resultPanel, BorderLayout.CENTER);
        revalidate();
    }

    private void start(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
            Random rand = new Random();
            String out = new String();
            double[] k = new double[15];
            for (int j=0;j<30;j++){
                for (int i =0; i<15; i++){
                    k[i] = Math.round(rand.nextDouble(20) * 100.0)/100.0;
                    if(i<14){
                        out += k[i] +" ";
                    }else {
                        out += k[i] + "\n";
                    }
                }
                writer.write(out);
                out = "";
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class InvalidInputDataException extends Exception{
    InvalidInputDataException(){
        super("Invalid input data format");
    }
}

class CustomException extends Exception{
    CustomException(String msg){
        super(msg);
    }
}
