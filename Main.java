package SeaBattle;

import SeaBattle.Draw.DrawingToDisplay;
import SeaBattle.Draw.IDrawing;
import SeaBattle.InputData.IInputData;
import SeaBattle.InputData.InputKey;

public class Main {
    public static void main(String[] args) {
        IDrawing drawing = new DrawingToDisplay();
        IInputData inputData = new InputKey();
        Game game = Game.getInstance(drawing, inputData);
        System.out.println("Выиграл игрок: " + game.startAutoGame());
    }
}
