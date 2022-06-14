import controller.client.ClientController;
import javafx.application.Application;
import util.Logger;
import view.View;
import view.cli.CliView;
import view.gui.GuiApplication;

public class ClientApp {
    static public void main(String[] args) {
        Logger.setLevel(Logger.LoggerLevel.DISABLED);

        boolean run = true;
        boolean cliEnabled = false;

        for (String arg : args) {
            switch (arg) {
                case "--help", "-h" -> {
                    System.out.println("Eriantys!");
                    System.out.println("Syntax: executable [--help | -h || --cli | -c]");
                    System.out.println("Options:");
                    System.out.println("\thelp | h\t\tShows this menu.");
                    System.out.println("\tcli  | c\t\tStarts game in terminal.");
                    run = false;
                }
                case "--cli", "-c" -> cliEnabled = true;
            }
        }

        if (run) {
            if (cliEnabled) {
                View view = new CliView();
                ClientController controller = new ClientController(view);
                view.run();
            } else {
                Logger.setLevel(Logger.LoggerLevel.INFO);
                Application.launch(GuiApplication.class);
            }
        }
    }
}
