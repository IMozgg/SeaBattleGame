package SeaBattle.gamesInventory;

public class Sea {
    private int size;
    private int countShip;
    private Ship[][] fieldShips;

    public Sea(int size) {
        this.size = size;
        this.fieldShips = new Ship[size][size];
    }

    public Sea() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCountShip() {
        return countShip;
    }

    public void decCountShip() {
        this.countShip--;
    }

    public Ship getShipOnFields(Point position) {
        return fieldShips[position.getX()][position.getY()];
    }

    public void saveShipOnField(Ship ship) {
        for (int i = 0; i < ship.getShipOnCells().length; i++) {
            fieldShips[ship.getShipOnCells()[i].getX()][ship.getShipOnCells()[i].getY()] = ship;
        }
        this.countShip++;
    }

    public void refresh() {

    }
}
