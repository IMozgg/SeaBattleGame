package SeaBattle.controller;

import SeaBattle.model.Game;
import SeaBattle.model.Point;
import SeaBattle.view.Form;

public class Controller {
    public volatile static boolean FLAG_CHANGE;
    private Game game;
    private Form view;

    public Controller(Game game, Form view) {
            this.game = game;
            this.view = view;
            Thread deamonChange = new Thread(new DeamonChangeLive(view, game));
            deamonChange.setDaemon(true);
            deamonChange.start();

            initFormAction();
    }

    private void initFormAction() {
        setActionButton();
    }

    public void showForm() {
        this.view.setVisible(true);
    }

    private void setActionButton() {
        for (int i = 0; i < view.leftCells.length; i++) {
            for (int j = 0; j < view.leftCells.length; j++) {
                int finalI = i;
                int finalJ = j;
                view.leftCells[j][i].addActionListener(e -> {
                    if (game.getActivePlayer().equals(game.players[0])) {
                        game.shootPlayer(new Point(finalJ, finalI), game.players[1]);
                        game.changePlayer();
                        FLAG_CHANGE = true;
                    } else {
                        System.err.println("Ходит компьютер");
                    }
                });
                view.rightCells[j][i].addActionListener(e -> {
                    if (game.getActivePlayer().equals(game.players[1])) {
                        game.shootPlayer(new Point(finalJ, finalI), game.players[0]);
                        game.changePlayer();
                        FLAG_CHANGE = true;
                    } else {
                        System.err.println("Ходит игрок");
                    }
                });
            }
        }
    }
}
