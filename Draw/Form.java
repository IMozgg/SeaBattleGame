package SeaBattle.Draw;

import javax.swing.*;
import java.awt.*;

public class Form extends JFrame {
    String version;
    JPanel leftPanel;
    JPanel rightPanel;
    JButton[] leftCells;
    JButton[] rightCells;

    public Form(String version) {
        this.version = version;
    }

    public void intForm() {
        this.setTitle("Морской бой " + version);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1024, 480);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(new GridLayout(1, 2, 10, 50));

        leftPanel = new JPanel(new GridLayout(10,10));
        rightPanel = new JPanel(new GridLayout(10,10));

        leftCells = new JButton[100];
        rightCells = new JButton[100];

        for (int i = 0; i < leftCells.length; i++) {
            leftCells[i] = new JButton();
            rightCells[i] = new JButton();

            leftPanel.add(leftCells[i]);
            rightPanel.add(rightCells[i]);
        }

        this.add(leftPanel);
        this.add(rightPanel);
    }
}
