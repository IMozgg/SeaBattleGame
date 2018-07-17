package SeaBattle.controller;

import SeaBattle.model.Game;
import SeaBattle.view.Form;

public class DeamonChangeLive implements Runnable {
    private Form view;
    private Game game;

    public DeamonChangeLive(Form view, Game game) {
        this.view = view;
        this.game = game;
    }

    @Override
    public void run() {
        while (true) {
            if (Controller.FLAG_CHANGE) {
                updateView();
                Controller.FLAG_CHANGE = false;
            }
        }
    }

    private void updateView() {
        for (int i = 0; i < view.leftCells.length; i++) {
            for (int j = 0; j < view.rightCells.length; j++) {
                if (game.players[1].getMaskShipSea()[j][i] != 0 | game.players[1].getSeaShoot()[j][i] != 0) {
                    view.leftCells[j][i].setText("*");
                }
                if (game.players[0].getMaskShipSea()[j][i] != 0 | game.players[0].getSeaShoot()[j][i] != 0) {
                    view.rightCells[j][i].setText("*");
                }
            }
        }
    }
}
