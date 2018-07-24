import presenter.MainPresenter;
import model.GameModel;
import view.GameView;

import javax.swing.*;

public class SeaBattleMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel gameModel = GameModel.getInstance();
            MainPresenter mainPresenter = new MainPresenter();
            mainPresenter.setModel(gameModel);
            GameView gameView = new GameView("v1.0");
            mainPresenter.setView(gameView);
            mainPresenter.setOnEventFinishGame(() -> JOptionPane.showMessageDialog(null, "Выиграл игрок " + gameModel.getActivePlayer()));
            mainPresenter.run();
        });
    }
}
