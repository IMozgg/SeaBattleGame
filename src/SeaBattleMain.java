package SeaBattle;

import SeaBattle.presenter.MainPresenter;
import SeaBattle.model.GameModel;
import SeaBattle.view.DrawingToDisplay;
import SeaBattle.view.GameView;
import SeaBattle.view.IDrawing;

import javax.swing.*;

public class SeaBattleMain {
    public static void main(String[] args) {
        IDrawing drawing = new DrawingToDisplay();

        SwingUtilities.invokeLater(() -> {
            GameModel gameModel = GameModel.getInstance(drawing);
            MainPresenter mainPresenter = new MainPresenter();
            mainPresenter.setModel(gameModel);
            GameView gameView = new GameView("v1.0");
            mainPresenter.setView(gameView);
            mainPresenter.setOnEventFinishGame(() -> JOptionPane.showMessageDialog(null, "Выиграл игрок " + gameModel.getActivePlayer()));
            mainPresenter.run();
        });
    }
}
