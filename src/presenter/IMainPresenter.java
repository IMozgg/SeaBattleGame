package presenter;

import model.GameModel;
import view.GameView;

import java.awt.event.ActionEvent;

public interface IMainPresenter {
    GameModel getModel();

    void setModel(GameModel gameModel);

    GameView getView();

    void setView(GameView gameView);

    void run();

    void changePlayer();

    int getActivePlayer();

    int getResultLastShoot();

    int getCountShipFromPlayer(int numPlayer);

    void setOnEventFinishGame(Runnable onEventFinishGame);

    boolean isGameOver();

    String getNamePlayer(int numPlayer);

    void doShoot(ActionEvent event, int x, int y);
}
