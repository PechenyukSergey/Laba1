package view;

import model.Task;
import model.TaskList;

import java.text.SimpleDateFormat;
import java.util.*;

public class ConsoleView implements View {

    @Override
    public void printCalendar(SortedMap<Date, Set<Task>> map) {
        SimpleDateFormat sdate = new SimpleDateFormat("[YYYY-MM-dd HH:mm:ss]", Locale.ENGLISH);
        for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
            System.out.println(sdate.format(pair.getKey()));
            for (Task task : pair.getValue()) {
                System.out.println(task.getTitle());
            }
        }
    }

    @Override
    public void printMenu() {
        System.out.println("-------------------MENU----------------");
        System.out.println("Press 1: To see all Tasks");
        System.out.println("Press 2: To see the calendar of Tasks");
        System.out.println("Press 3: Add new Task");
        System.out.println("Press 4: Change the Task");
        System.out.println("Press 5: Delete the Task");
        System.out.println("Press 6: Exit");
        System.out.println("-------------------MENU----------------");
    }

    @Override
    public void displayList(TaskList list) {
        if (list.size() == 0) {
            System.out.println("The List of Tasks is empty");
        } else {
            System.out.println("Size list: " + list.size());
            for (int i = 0; i < list.size(); i++) {
                System.out.print(i + ": " + list.getTask(i));
            }
        }
    }
}
