import controller.Controller;
import controller.Daemon;
import controller.TaskManagerController;
import view.ConsoleView;

import java.io.File;
import java.text.ParseException;

public class Main
{
    public static void main( String[] args ) throws ParseException {

        ConsoleView view = new ConsoleView();
        File file = new File ("taskList.txt");
        Controller controller = new TaskManagerController(view, file);

        Daemon notify = new Daemon(controller.getModelList(), file);
        controller.run();
        notify.run();
    }
}

