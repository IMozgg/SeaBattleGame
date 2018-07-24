package view;

import model.Point;
import presenter.MainPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameView extends JFrame implements IGameView {
    private final int SIZE = 10;
    private MainPresenter mainPresenter;

    private String version;
    private JPanel leftPanel;
    private JPanel rightPanel;
    public JButton leftCells[][];
    public JButton rightCells[][];

    public GameView(String version) {
        this.version = version;
        initComponent();
    }

    private void initComponent() {
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

                leftCells[j][i].setPreferredSize(new Dimension(50, 50));
                rightCells[j][i].setPreferredSize(new Dimension(50, 50));

                final int x = j;
                final int y = i;

                leftCells[j][i].addActionListener(event -> setActionButton(event, x, y));
                rightCells[j][i].addActionListener(event -> setActionButton(event, x, y));

                leftPanel.add(leftCells[j][i]);
                rightPanel.add(rightCells[j][i]);
            }

        }

        this.add(leftPanel);
        this.add(rightPanel);

        this.pack();
    }

    private void setActionButton(ActionEvent event, int x, int y) {
        getPresenter().doShoot(event, x, y);
    }

    public boolean checkAccessToButton(ActionEvent event, int x, int y) {
        if (getPresenter().getActivePlayer() == 0) {
            if (!event.getSource().equals(leftCells[x][y])) {
                return true;
            }
        } else {
            if (event.getSource().equals(leftCells[x][y])) {
                return true;
            }
        }

        System.err.println("Сейчас очередь игрока " + getPresenter().getActivePlayer());
        return false;
    }

    @Override
    public void updateFromViewToModel() {

    }

    @Override
    public void updateFromModelToView() {

    }

    @Override
    public void open() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        //  Уничтожаем окно
        dispose();
    }

    @Override
    public void setPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public MainPresenter getPresenter() {
        return mainPresenter;
    }

    @Override
    public void updateGameFieldFromModel(int numPlayer, Point p) {
        JButton selectBtn[][];
        if (numPlayer == 0) {
            selectBtn = leftCells;
        } else {
            selectBtn = rightCells;
        }

        if ((getPresenter().getModel().players[numPlayer].getSeaShoot()[p.getX()][p.getY()] == 1) |
                (getPresenter().getModel().players[numPlayer].getSeaShoot()[p.getX()][p.getY()] == 0)) {
            selectBtn[p.getX()][p.getY()].setText("X");
        } else {
            selectBtn[p.getX()][p.getY()].setText("o");
        }
        selectBtn[p.getX()][p.getY()].setEnabled(false);

        for (int i = 0; i < this.SIZE; i++) {
            for (int j = 0; j < this.SIZE; j++) {
                //  Запрашиваем у модели поле выстрелов и смотрим что там. Если 1 или 0 то значит было либо попадание либо уничтожение
                if (getPresenter().getModel().players[numPlayer].getMaskShipSea()[j][i] == 1) {
                    selectBtn[j][i].setText("o");
                    selectBtn[j][i].setEnabled(false);
                }
            }
        }
    }

    @Override
    public void showShipsPlayer(int numPlayer) {
        for (int i = 0; i < this.SIZE; i++) {
            for (int j = 0; j < this.SIZE; j++) {
                if ((getPresenter().getModel().players[numPlayer].getSea().getShipOnFields(new Point(i, j)) != null)) {
                    if (numPlayer == 0) {
                        leftCells[i][j].setText("O");
                    } else {
                        rightCells[i][j].setText("O");
                    }
                }
            }
        }
    }

    @Override
    public void showMessageDialogue(String msg) {
        Runnable runnable = () -> JOptionPane.showMessageDialog(null, msg);
        runnable.run();
    }

    @Override
    public String setNameDialog() {
        return JOptionPane.showInputDialog("Укажите имя игрока");
    }
}
