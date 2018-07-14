package SeaBattle.Draw;

import SeaBattle.gamesInventory.Player;
import SeaBattle.gamesInventory.Point;
import SeaBattle.gamesInventory.Sea;

public class DrawingToDisplay implements IDrawing {
    public DrawingToDisplay() {

    }

    @Override
    public void draw(Player player) {
        Sea tempSea = player.getSea();
        Point tempPoint;

        for (char charCount = 'A'; charCount <= 'J'; charCount++) {
            System.out.print("\t" + charCount);
        }

        System.out.println();
        tempSea = player.getSea();
        tempPoint = new Point();

        for (int j = 0; j < tempSea.getSize(); j++) {
            System.out.print((j + 1) + "\t");   //  Отрисовываем поля
            for (int k = 0; k < tempSea.getSize(); k++) {
                tempPoint.setX(k);
                tempPoint.setY(j);
                // Если в данной точки нет информации о корабле, значит там нет ничего
                if (tempSea.getShipOnFields(tempPoint) == null) {
                    if (player.getSeaShoot()[tempPoint.getX()][tempPoint.getY()] == 1) {
                        System.out.print("*\t");
                    } else {
                        System.out.print("_\t");
                    }
                } else {
                    // Если в данной точки есть корабль, то смотрим на его составную часть, если она null (подбита) то ставим Х
                    if (!player.getSea().getShipOnFields(tempPoint).isPointShipOnCells(tempPoint)) {
                        System.out.print("X\t");
                    } else {
                        System.out.print("O\t");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }


    public void showSeaPlayers() {
    }

    public void drawMask(Player p) {
        for (char charCount = 'A'; charCount <= 'J'; charCount++) {
            System.out.print("\t" + charCount);
        }
        System.out.println();

        for (int i = 0; i < p.getSea().getSize(); i++) {
            System.out.print((i + 1) + "\t");
            for (int j = 0; j < p.getSea().getSize(); j++) {
                System.out.print(p.getMaskShipSea()[j][i] + "\t");
            }
            System.out.println();
        }
    }
}
