package presenter;

import model.GameModel;
import model.Point;
import view.GameView;
import java.awt.event.ActionEvent;

public class MainPresenter implements IMainPresenter {
    public static boolean NEXT_SHOOT_PC = false;

    private GameModel gameModel;
    private GameView gameView;
    private int resultLastShoot;
    private Runnable onEventFinishGame;

    public MainPresenter(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public MainPresenter() {

    }

    @Override
    public GameModel getModel() {
        return this.gameModel;
    }

    @Override
    public void setModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public GameView getView() {
        return this.gameView;
    }

    @Override
    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void run() {
        gameView.setPresenter(this);
        gameModel.players[0].setName(gameView.setNameDialog());

        gameView.showShipsPlayer(0);
        //gameView.showShipsPlayer(1);
        gameView.open();

        if (gameModel.getActivePlayer().equals(gameModel.players[1])) {
            MainPresenter.NEXT_SHOOT_PC = true;
        }
    }

    private void shootToPlayer(Point p) {
        String temp;
        Point clonePoint = p;

        do {
            //  Если ходит первый игрок, ток стрельба должна быть по второму игроку
            if (!MainPresenter.NEXT_SHOOT_PC) {
                resultLastShoot = this.gameModel.shootPlayer(clonePoint, getActivePlayer() == 0 ? gameModel.players[1] : gameModel.players[0]);
            } else {
                clonePoint = gameModel.iiShoot(gameModel.players[0]);
                resultLastShoot = gameModel.players[0].getSeaShoot()[clonePoint.getX()][clonePoint.getY()];
            }

            temp = String.format("Проведен выстрел в точку %s:%s игроком №%s", clonePoint.getX(), clonePoint.getY(), getActivePlayer() == 0 ? 2 : 1);
            System.out.println(temp);
            this.gameView.updateGameFieldFromModel(getActivePlayer() == 0 ? 1 : 0, clonePoint);

            if (gameModel.players[0].getSea().getCountShip() <= 0 | gameModel.players[1].getSea().getCountShip() <= 0) {
                gameModel.setGameOver(true);
                this.onEventFinishGame.run();
                System.exit(0);
            }

            //  Смена игрока если промах
            if (getResultLastShoot() == -1) {
                changePlayer();
                if (gameModel.getActivePlayer().equals(gameModel.players[0])) {
                    MainPresenter.NEXT_SHOOT_PC = false;
                } else {
                    MainPresenter.NEXT_SHOOT_PC = true;
                }
            }
        } while (MainPresenter.NEXT_SHOOT_PC);
    }

    @Override
    public void doShoot(ActionEvent event, int x, int y) {
        if (!isGameOver()) {
            //  Если игрок ходит по своему полю то делаем выстрел
            if (gameView.checkAccessToButton(event, x, y)) {
                shootToPlayer(new model.Point(x, y));
            }
        } else {
            gameView.showMessageDialogue("Игра закончена\nПобедил игрок " + getNamePlayer(getActivePlayer()));
            System.exit(0);
        }
    }

    @Override
    public void changePlayer() {
        this.gameModel.changePlayer();
    }

    @Override
    public int getActivePlayer() {
        return (this.gameModel.getActivePlayer().equals(this.gameModel.players[0]) ? 0 : 1);
    }

    @Override
    public int getResultLastShoot() {
        return resultLastShoot;
    }

    @Override
    public int getCountShipFromPlayer(int numPlayer) {
        return gameModel.players[numPlayer].getSea().getCountShip();
    }

    @Override
    public void setOnEventFinishGame(Runnable onEventFinishGame) {
        this.onEventFinishGame = onEventFinishGame;
    }

    @Override
    public boolean isGameOver() {
        return this.gameModel.isGameOver();
    }

    @Override
    public String getNamePlayer(int numPlayer) {
        return this.gameModel.players[numPlayer].getName();
    }


}
