package SeaBattle;

import SeaBattle.controller.Controller;
import SeaBattle.model.Game;
import SeaBattle.view.DrawingToDisplay;
import SeaBattle.view.Form;
import SeaBattle.view.IDrawing;

public class Main {
    public static void main(String[] args) {
        IDrawing drawing = new DrawingToDisplay();
        Game game = Game.getInstance(drawing);
        Form form = new Form("v1.0");
        form.initForm();
        Controller controller = new Controller(game, form);

        controller.showForm();
    }
}
