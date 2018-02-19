package controller;

import model.TaskList;
import java.text.ParseException;

public interface Controller extends Runnable {
    void updateView();
    void runTaskMan() throws ParseException;
    void run();
    TaskList updateModel();
    TaskList getModelList();
}
