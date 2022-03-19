import model.board.Assistant;
import util.Wizard;

import java.util.List;

public class Client {
    static public void main(String[] args) {
        boolean cliEnabled = false;

        for (String arg : args) {
            switch (arg) {
                case "--help", "-h" -> {
                    System.out.println("Eriantys!");
                    System.out.println("Syntax: executable [--help | -h || --cli | -c]");
                    System.out.println("Options:");
                    System.out.println("\thelp | h\t\tShows this menu.");
                    System.out.println("\tcli  | c\t\tStarts game in terminal.");
                }
                case "--cli", "-c" -> cliEnabled = true;
            }
        }
        List<Assistant> deck = Assistant.getWizardDeck(Wizard.WIZARD_1);
        if (cliEnabled) {
            // Start game using terminal
        } else {
            // Start game using ui
        }
    }
}
