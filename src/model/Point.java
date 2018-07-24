package SeaBattle.model;

public class Point {
    private int x;
    private int y;

    public Point() {

    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void multiplyXY(int val) {
        this.x *= val;
        this.y *= val;
    }

    public boolean isNegativeXY() {
        if (this.x < 0 & this.y < 0) {
            return true;
        } else {
            return false;
        }
    }

    public void copy(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public void setXY(int value) {
        this.x = this.y = value;
    }

    public void deductXY(int value) {
        this.x -= value;
        this.y -= value;
    }

    public void addXY(int value) {
        this.x += value;
        this.y += value;
    }

    @Override
    public String toString() {
        return "{ " + x + "; " + y + " }";
    }
}
