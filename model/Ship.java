package SeaBattle.model;

public class Ship {
    private Point[] shipOnCells;   // Составной корабль. Состоит из нескольких однопалубных
    private int lives;
    private int size;
    private int attitude;
    private boolean destroy;

    public Ship() {

    }

    //  attitude, 0 - ЮГ, 1 - ВОСТОК
    public Ship(Point p, int size, int attitude) {
        Point tempPoint;
        this.lives = size;
        this.size = size;
        this.attitude = attitude;
        shipOnCells = new Point[size];

        shipOnCells[0] = new Point(p.getX(), p.getY());
        tempPoint = p;
        for (int i = 1; i < shipOnCells.length; i++) {
            switch (attitude) {
                case 0:
                    tempPoint.setY(tempPoint.getY() + 1);
                    shipOnCells[i] = new Point(p.getX(), p.getY());
                    break;
                case 1:
                    tempPoint.setX(tempPoint.getX() + 1);
                    shipOnCells[i] = new Point(p.getX(), p.getY());
                    break;
                default:
                    System.err.println("Такого направления не существует");
                    break;
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getSize() {
        return size;
    }

    public int getAttitude() {
        return attitude;
    }

    public Point[] getShipOnCells() {
        return shipOnCells;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void decLives() {
        this.lives--;
        if (this.lives == 0) {
            this.destroy = true;
        }
    }

    public boolean isPointShipOnCells(Point p) {
        for (int i = 0; i < shipOnCells.length; i++) {
            if (!shipOnCells[i].isNegativeXY() && p.getX() == shipOnCells[i].getX() & p.getY() == shipOnCells[i].getY()) {
                return true;    // Значит что корабль в этой точке не поврежден
            }
        }
        return false;   //  Значит что корабль поврежден в этой точке
    }
}
