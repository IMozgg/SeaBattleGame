package SeaBattle.InputData;

import java.util.Scanner;

public class InputKey implements IInputData {
    public InputKey() {
    }

    //  Считать строку с клавиатуры
    @Override
    public String getInputString() {
        Scanner sc = new Scanner(System.in);

        while(true) {
            String answer;
            answer = sc.nextLine();

            //  Проверяем что пользователь ввел что-нибудь
            if (answer.length() > 0) {
                return answer;
            } else {
                System.err.println("Вы ничего не ввели.\nПопробуйте еще раз");
            }
        }
    }
}
