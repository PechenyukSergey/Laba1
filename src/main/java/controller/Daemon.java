package controller;

import model.Task;
import model.TaskIO;
import model.TaskList;
import model.Tasks;
import org.apache.log4j.Logger;
import view.ConsoleView;
import view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class describes thread, which is running all time during
 * the user works with main app. The class sends notifications
 * to user about tasks which should be finished at the certain time.
 * Also the class overwrite file where is tasks stores and
 * if tasks are finished it will remove it from the file.
 * */

public class Daemon implements Runnable {
    private TaskList list;
    private Thread thread;
    private View console = new ConsoleView();

    private File file;
    private static final Logger logger = Logger.getLogger(Task.class);

    public Daemon(TaskList model, File file) {
        list = model;
        this.file = file;
        thread = new Thread(this, "Task notification");
        thread.setDaemon(true);
        thread.start();
    }

    private void notifyNtf() {
        SortedMap<Date, Set<Task>> repeatedTasks;
        int oneMinute = 60000;
        long prev = new Date().getTime() - oneMinute;
        long next = new Date().getTime();
        Date pre = new Date();
        Date nex = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("[YYYY-MM-dd HH:mm:ss]", Locale.ENGLISH);
        nex.setTime(next);
        pre.setTime(prev);

        repeatedTasks = Tasks.calendar(list, pre, nex);
        for (Map.Entry<Date, Set<Task>> item : repeatedTasks.entrySet()) {
            System.out.print("\n-----------------------------------\n");
            System.out.print("Don't forget ! " +
                    sdate.format(item.getKey()) + "\nTasks: ");
            for (Task task : item.getValue()) {
                System.out.println(task.getTitle());
            }
            System.out.print("\n-----------------------------------\n");
        }
        checkForFinished(list);
    }


    @Override
    synchronized public void run() {
        try {
            TaskIO.readText(list, file);
        } catch (FileNotFoundException e) {
            logger.error("File not found");
            System.out.println("File not found. Created a new File: " + file.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            notifyNtf();
            try {
                thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void checkForFinished(TaskList taskList) {
        TaskList listTask = taskList.clone();
        for (Task i : listTask) {
            if (listTask.size() > 1) {
                if (!i.isRepeated() && new Date().compareTo(i.getTime()) >= 0) {
                    System.out.print("\n-----------------------------------\n");
                    System.out.print("Don't forget ! " +
                            i.getTime() + "\nTasks: ");
                    System.out.print(i.getTitle() + "\' ");
                    taskList.remove(i);
                }
                if (i.isRepeated() && new Date().compareTo(i.getEndTime()) > 0) {
                    taskList.remove(i);
                }
            }
        }
        try {
            TaskIO.writeText(list, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
