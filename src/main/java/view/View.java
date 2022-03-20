package view;

import java.util.Observable;
import java.util.Observer;

public abstract class View extends Observable implements Runnable, Observer {

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void run() {

    }
}
