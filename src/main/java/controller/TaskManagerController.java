package controller;

import model.*;
import view.View;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class TaskManagerController implements Controller, Runnable {
    private static final Logger logger = LogManager.getLogger(TaskManagerController.class);
    private Task model;
    private View view;
    private TaskList taskList = new LinkedTaskList();
    private TaskList taskListClone = taskList.clone();
    private File file;
    private final String FORMATT = "yy.MM.dd HH:mm:ss";
    private SimpleDateFormat format = new SimpleDateFormat(FORMATT);

    public TaskManagerController(View view, File file) {
        this.view = view;
        model = new Task(" ", new Date());
        this.file = file;
    }

    @Override
    public TaskList updateModel() {
        logger.info("Model was updated");
        return taskList;
    }

    @Override
    public void updateView() {
        view.displayList(taskList);
        logger.info("View was updated");
    }


    /**
     * Method for working with menu.
     * Also contains the logic of interaction with the task and task list.
     */
    public void runTaskMan() throws ParseException {
        String a;
        Date start = new Date();
        Date end = new Date();

        view.printMenu();
        a = readLineFromConsol();

        switch (a) {
            case "1":
                if (isTaskInList(taskListClone)) {
                    view.displayList(taskListClone);
                } else {
                    System.out.println("You don't have any Task");
                    createTask();
                }
                runTaskMan();
                break;
            case "2":
                String str;
                System.out.print("Enter your Start date (yy.MM.dd HH:mm:ss): ");
                str = readLineFromConsol();
                try {
                    start.setTime(format.parse(str).getTime());

                } catch (ParseException e) {
                    System.out.println("Date could not be parsed! Enter correct  date, pls.");
                    runTaskMan();
                }
                System.out.println("Enter your End date (yy.MM.dd HH:mm:ss): ");
                str = readLineFromConsol();
                try {
                    end.setTime(format.parse(str).getTime());

                } catch (ParseException e) {
                    System.out.println("Date could not be parsed! Enter correct  date, pls.");
                    runTaskMan();
                }

                makeCalendar(start, end);
                runTaskMan();
                break;
            case "3":
                createTask();
                runTaskMan();
                break;
            case "4":
                changeTask();
                runTaskMan();
                break;
            case "5":
                removeTask();
                runTaskMan();
                break;
            case "6":
                exitTaskManager();
                break;
            default:
                System.out.println("ENTER A NUMBER !");
                run();
        }

    }

    /**Method for getting the whole line of input*/
    private String readLineFromConsol() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    /**The method checks if there is a task in the list*/
    private boolean isTaskInList(TaskList list) {
        return list.size() != 0;
    }

    /**Method for creating a task*/
    private void createTask() {
        String str;
        String strTitle;
        String strDate;
        Task modelClone = new Task(" ", new Date());

        System.out.println();
        System.out.print(String.format("%25s", "Creating a task..."));
        System.out.print("\nEnter task title: ");

        strTitle = readLineFromConsol();
        modelClone.setTitle(strTitle);

        System.out.print("\nEnter task date executing (yy.MM.dd HH:mm:ss): ");
        strDate = readLineFromConsol();

        try {
            modelClone.setTime(format.parse(strDate));
        } catch (ParseException e) {
            System.out.println("ENTER CORRECT DATE ! TRY AGAIN TO MAKE A TASK !");
            run();
        }
        System.out.print("\nDo you want to make the task repeatable (Y/N) ? ");
        str = readLineFromConsol();

        if (str.toLowerCase().equals("y")) {
            modelClone.setActive(true);
            System.out.print("\nEnter end task time point (yy.MM.dd HH:mm:ss): ");
            strDate = readLineFromConsol();
            makeInterval(modelClone);
            try {
                modelClone.setTime(modelClone.getStartTime(), format.parse(strDate), modelClone.getRepeatInterval());
            } catch (ParseException e) {
                System.out.println("Date could not be parsed! Enter correct  date, pls.");
                run();
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                run();
            }
        }
        model = modelClone;
        try {
            taskListClone.add(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        acceptChanges();
        logger.info("Task \'" + modelClone.getTitle() + "\' was created");
    }

    /**Method for editing a certain task*/
    private void changeTask() {
        String str;
        String strTitle;
        String strDate;

        view.displayList(taskListClone);
        System.out.println();
        System.out.println("What Task do you want to change (enter it's number)? ");
        int numberOfTask = Integer.parseInt(readLineFromConsol());
        System.out.println("Changed Task is ");
        Task task = taskListClone.getTask(numberOfTask);
        System.out.println(task.toString());

        System.out.println("What you want change:");
        System.out.println("1. Title");
        System.out.println("2. Time");
        System.out.println("3. Start time");
        System.out.println("4. End time");
        System.out.println("5. Interval");
        System.out.println("6. Make it active/inactive");
        System.out.print("Select your variant: ");

        str = readLineFromConsol();
        switch (str) {
            case "1":
                System.out.print("Enter task title: ");
                strTitle = readLineFromConsol();
                task.setTitle(strTitle);
                break;
            case "2":
                System.out.print("\nEnter task date executing (yy.MM.dd HH:mm:ss): ");
                strDate = readLineFromConsol();
                try {
                    task.setTime(format.parse(strDate));
                    task.setActive(task.isActive());
                } catch (ParseException e) {
                    System.out.println("Date could not be parsed! Enter correct  date, pls.");
                    changeTask();
                }
                break;
            case "3":
                System.out.print("\nEnter start task time point (yy.MM.dd HH:mm:ss):");
                strDate = readLineFromConsol();
                try {
                    task.setTime(format.parse(strDate), task.getEndTime(), task.getRepeatInterval());
                } catch (ParseException e) {
                    System.out.println("Date could not be parsed! Enter correct  date, pls.");
                    changeTask();
                }
                break;
            case "4":
                System.out.print("\nEnter end task time point (yy.MM.dd HH:mm:ss): ");
                strDate = readLineFromConsol();
                try {
                    task.setTime(task.getStartTime(), format.parse(strDate), task.getRepeatInterval());
                } catch (ParseException e) {
                    System.out.println("Date could not be parsed! Enter correct  date, pls.");
                    changeTask();
                }
                break;
            case "5":
                makeInterval(task);
                break;
            case "6":
                System.out.println("Enter 1/0 (activate = 1, deactivate = 0): ");
                str = readLineFromConsol();
                if (str.equals("1"))
                    task.setActive(true);
                else
                    task.setActive(false);
                break;
        }
        model = task;

        acceptChanges();
        logger.info("Task was modified");
    }

    /**Method for removing a certain task*/
    private void removeTask() {
        String str;
        view.displayList(taskListClone);
        System.out.print("What task do you want to remove (enter it's number)? ");
        str = readLineFromConsol();
        TaskList listClone = taskListClone.clone();

        int numberOfTask = Integer.parseInt(readLineFromConsol());
        System.out.println("Deleted Task is ");
        Task task = taskListClone.getTask(numberOfTask);
        System.out.println(task.toString());

        for (Task i : taskListClone) {
            if (str.toLowerCase().equals(i.getTitle().toLowerCase())) {
                logger.info("Task \'" + i.getTitle() + "\' was removed");
                listClone.remove(i);
            }
        }

        taskListClone = listClone;
        try {
            TaskIO.writeText(taskListClone, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateModel();
        updateView();
    }

    /**Method for going out from the app*/
    private void exitTaskManager() {
        System.exit(0);
    }

    /**Method for accepting changes after editing/creating task or task list*/
    private void acceptChanges() {
        taskList = taskListClone;
        try {
            TaskIO.writeText(taskList, file);
        } catch (Exception e) {
            System.out.println("Smth wrong with removing existing tasks");
        }
        updateModel();
        updateView();
    }

    private void makeCalendar(Date star, Date end) {
        SortedMap<Date, Set<Task>> calendar = Tasks.calendar(taskListClone, star, end);
        view.printCalendar(calendar);
    }

    /**Method which responds for returning the whole task list. @return Task list*/
    public TaskList getModelList() {
        return taskListClone;
    }

    /**The main class method for starting a program*/
    @Override
    synchronized public void run() {
        try {
            runTaskMan();
        } catch (ParseException e) {
            System.out.println("Something wrong with your input");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            //System.out.println("Date could not be parsed! Enter correct  date, pls.545454");
            run();
        }
    }

    /**Method for a getting time in days/hours/minutes/seconds*/
    private void makeInterval(Task task) {
        int interval = 0;

        System.out.print("\nEnter interval (value & d-day/h-hour/m-minutes/s-seconds): ");
        String str = readLineFromConsol();
        String[] dateArr = str.split(" ");

        char tmp = dateArr[1].charAt(0);
        if (tmp == 'd') {
            interval = Integer.parseInt(dateArr[0]) * 86400000;
        } else if (tmp == 'h') {
            interval = Integer.parseInt(dateArr[0]) * 3600000;
        } else if (tmp == 'm') {
            interval = Integer.parseInt(dateArr[0]) * 60000;
        } else if (tmp == 's') {
            interval = Integer.parseInt(dateArr[0]) * 1000;
        }
        task.setTime(task.getStartTime(), task.getEndTime(), interval);
    }
}
