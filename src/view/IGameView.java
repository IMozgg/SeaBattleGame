package SeaBattle.view;

import SeaBattle.model.Point;
import SeaBattle.presenter.MainPresenter;

public interface IGameView {
    void updateFromViewToModel();

    void updateFromModelToView();

    void updateGameFieldFromModel(int numPlayer, Point p);

    void showShipsPlayer(int numPlayer);

    void showMessageDialogue(String msg);

    void open();

    void close();

    void setPresenter(MainPresenter mainPresenter);

    MainPresenter getPresenter();

    String setNameDialog();
}
