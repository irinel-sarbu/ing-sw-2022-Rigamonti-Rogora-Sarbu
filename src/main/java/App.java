import controller.GameController;
import model.Game;
import view.View;
import view.cli.CliView;
import view.gui.GuiView;

public class App {
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
            Game model = Game.getInstance();
            View view = cliEnabled ? new CliView() : new GuiView();
            GameController controller = new GameController(model, view);
            view.registerListener(controller);
            model.registerListener(view);
            view.run();
        }
    }
}