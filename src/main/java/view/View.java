package view;

import model.Task;
import model.TaskList;

import java.util.Date;
import java.util.Set;
import java.util.SortedMap;

public interface View {
    void printMenu();
    void printCalendar(SortedMap<Date, Set<Task>> args);
    void displayList(TaskList taskList);
}
