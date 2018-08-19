package controller.Interfacing;

import controller.Entity.playerConfigController;
import view.Cli;

import java.io.IOException;
import java.util.Scanner;

public class CLIController extends AbstractInterfaceController{
    Cli  userInterface = new Cli();

    Scanner scanner = new Scanner(System.in);

    @Override
    public void start() {
        run();
    }

    @Override
    void run() {
        userInterface.displayWelcomeMessage();
        waitForEnterPress();
        gameLoop();
    }

    private void gameLoop() {
        String input;
        do {
            userInterface.promptUserAction();
            input = scannerGetInput();
        } while (!evaluate(input));
    }

    private boolean evaluate(String input) {
        switch (input)
        {
           case  "q" :
               quitGame();
               break;
            case "p" :
                playGame();
                return true;
           case "x" :
               switchUI();
               return true;
        }
        return false;
    }

    private void playGame() {
        userInterface.userName();
        String username = scanner.nextLine();
        System.out.println("Hello "+username);


        String charType = scanner.nextLine();

        playerConfigController player = new playerConfigController(username, charType);
    }

    @Override
    void switchUI() {
        AbstractInterfaceController controller =
                new GUIController();
        controller.run();
    }

    private void quitGame() {
        userInterface.displayGoodMessage();
        System.exit(0);
    }

    private String scannerGetInput() {
        return scanner.nextLine();
    }

    private void waitForEnterPress() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}