package SeaBattle.view;

import javax.swing.*;
import java.awt.*;

public class Form extends JFrame {
    private final int SIZE = 10;

    private String version;
    private JPanel leftPanel;
    private JPanel rightPanel;
    public JButton leftCells[][];
    public JButton rightCells[][];

    public Form(String version) {
        this.version = version;
    }

    public void initForm() {
        this.setTitle("Морской бой " + version);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1024, 480);
        this.setLocationRelativeTo(null);
        //this.setVisible(true);
        this.setLayout(new GridLayout(1, 2, 10, 50));

        leftPanel = new JPanel(new GridLayout(10, 10));
        rightPanel = new JPanel(new GridLayout(10, 10));

        leftCells = new JButton[SIZE][SIZE];
        rightCells = new JButton[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                leftCells[j][i] = new JButton();
                rightCells[j][i] = new JButton();
                leftPanel.add(leftCells[j][i]);
                rightPanel.add(rightCells[j][i]);
            }

        }

        this.add(leftPanel);
        this.add(rightPanel);
    }
}
