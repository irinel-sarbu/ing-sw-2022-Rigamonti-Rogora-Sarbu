package view.cli;

import events.Event;
import events.types.SimpleMessageEvent;
import view.View;

import java.util.Scanner;

public class CliView extends View {
    @Override
    public void run() {
        System.out.print("Insert a message: ");
        Scanner in = new Scanner(System.in);

        String s = in.nextLine();
        notifyListeners(new SimpleMessageEvent(s));
    }

    @Override
    public void onEvent(Event event) {

    }
}
