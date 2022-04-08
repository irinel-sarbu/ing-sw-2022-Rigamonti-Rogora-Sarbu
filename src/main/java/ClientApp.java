import controller.client.ClientController;
import view.View;
import view.cli.CliView;
import view.gui.GuiView;

public class ClientApp {
    static public void main(String[] args) {
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
            View view = cliEnabled ? new CliView() : new GuiView();

            ClientController controller = new ClientController(view);

            view.registerListener(controller);

            view.run();
        }
    }
}
