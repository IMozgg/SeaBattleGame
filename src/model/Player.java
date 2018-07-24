package model;

import java.util.ArrayList;
import java.util.List;

/**
 * seaShoot - матрица попаданий
 * На этой матрице располагаются выстрелы игроков
 * 0 - пустое поле
 * 1 - по этой клетке был выстрел
 *
 * maskShipSea - матрица маски (тени) кораблей.
 * поле, где 100% нет корабля (согласно правилам Морского боя)
 *  * * * *
 *  * О О *
 *  * * * *
 *  Звездочками указано поле, где согласно правилам, не может находится ничего
 *  0 - пустое поле
 *  1 - поле в котором точно ничего нет
 */
public class Player {
    private String name;
    private Sea sea;
    private byte[][] seaShoot; // Матрицы выстрелов (смотреть куда игрок стрелял)
    private byte[][] maskShipSea;
    private List<Ship> ships;
    private int countShoot;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Sea getSea() {
        return sea;
    }

    public Player() {
        ships = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    public void createSea() {
        int size = 10;

        sea = new Sea(size);
        seaShoot = new byte[size][size];
        maskShipSea = new byte[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                seaShoot[i][j] = -2;
            }

        }
    }

    public byte[][] getSeaShoot() {
        return seaShoot;
    }

    public void createShip(Point curPoint, int size, int attitude) {
        Ship tempShip;

        tempShip = new Ship(curPoint, size, attitude);

        //  Применим маску к кораблю
        applyMaskShip(tempShip);
        //добавим корабль в список
        ships.add(tempShip);
        //Разместим корабль на поле
        sea.saveShipOnField(tempShip);
    }

    //  Метод выстрел
    //  В случае попадания в корабль будет выведен 0
    //  В случае уничтожения корабля будет выведена 1
    //  В случае промаха будет выведен -1
    //  В случае повторного попадания будет выведено 2
    //  -2 поле по умолчанию
    public int shoot(Point p) {
        if (seaShoot[p.getX()][p.getY()] == -2) {
            for (Ship ship : ships) {
                for (int i = 0; i < ship.getShipOnCells().length; i++) {
                    if (!ship.getShipOnCells()[i].isNegativeXY()) {
                        if (ship.getShipOnCells()[i].getX() == p.getX() & ship.getShipOnCells()[i].getY() == p.getY()) {
                            ship.decLives();
                            ship.getShipOnCells()[i].multiplyXY(-1);
                            if (ship.isDestroy()) {
                                sea.decCountShip(); //  Уменьшаем кол-во кораблей на поле
                                this.seaShoot[p.getX()][p.getY()] = 1;
                                applyMaskShip(ship);
                                return 1;
                            }
                            this.seaShoot[p.getX()][p.getY()] = 0;
                            return 0;
                        }
                    }
                }
            }
            this.seaShoot[p.getX()][p.getY()] = -1;
            return -1;
        } else {
            return 2;
        }
    }

    public void applyMaskShip(Ship s) {
        int x0;
        int x1;
        int y0;
        int y1;

        if (s.isDestroy()) {
            x0 = ((s.getShipOnCells()[0].getX()) * -1) - 1;
            x1 = (s.getShipOnCells()[s.getSize() - 1].getX() * -1) + 1;
            y0 = (s.getShipOnCells()[0].getY() * -1) - 1;
            y1 = (s.getShipOnCells()[s.getSize() - 1].getY() * -1) + 1;
        } else {
            x0 = (s.getShipOnCells()[0].getX()) - 1;
            x1 = s.getShipOnCells()[s.getSize() - 1].getX() + 1;
            y0 = s.getShipOnCells()[0].getY() - 1;
            y1 = s.getShipOnCells()[s.getSize() - 1].getY() + 1;
        }

        for (int i = x0; i <= x1; i++) {
            for (int j = y0; j <= y1 ; j++) {
                if ((i >= 0 & i < 10 & j >= 0 & j < 10) ^ ((i >= x0 + 1 & i <= x1 - 1) & (j >= y0 + 1 & j <= y1 - 1))) {
                    try {
                        maskShipSea[i][j] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Выход за пределы массива " + j + ":" + i);
                    }
                }
            }
        }
    }

    public void applyMaskShip(Point firstP, Point lastP) {
        for (int i = firstP.getX(); i <= lastP.getX(); i++) {
            for (int j = firstP.getY(); j <= lastP.getY() ; j++) {
                if ((i >= 0 & i < 10 & j >= 0 & j < 10) ^ ((i >= firstP.getX() + 1 & i <= lastP.getX() - 1) & (j >= firstP.getY() + 1 & j <= lastP.getY() - 1))) {
                    maskShipSea[i][j] = 1;
                }
            }
        }
    }

    public byte[][] getMaskShipSea() {
        return maskShipSea;
    }

    public void clearMaskShip() {
        for (int i = 0; i < sea.getSize(); i++) {
            for (int j = 0; j < sea.getSize(); j++) {
                maskShipSea[i][j] = 0;
            }
        }
    }

    public int getCountShoot() {
        return countShoot;
    }

    public void incCountShoot() {
        this.countShoot++;
    }

    public void clearAllShips() {
        ships.clear();
    }
}
