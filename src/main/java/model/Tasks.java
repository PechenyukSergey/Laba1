package model;
import java.util.*;
/**
 * Class Tasks.
 * @version 1.01 15 Des 2017
 * @author Sergey Pechenyuk
 */
public class Tasks {

    /*public static Set<Task> incoming(Iterable<Task> tasks, Date start, Date end){
        if (start == null || end == null || end.before(start) )
            throw new IllegalArgumentException();

        Set<Task> incomTaskSet = new HashSet<Task>();
        for(Task task: tasks)
            if(task.nextTimeAfter(start)!= null && task.nextTimeAfter(start).compareTo(end)<= 0){
                incomTaskSet.add(task);
            }
        return incomTaskSet;
    }*/


    public static SortedMap <Date, Set<Task>> calendar (Iterable<Task> tasks, Date start, Date end){
        SortedMap<Date, Set<Task>> calendarTask = new TreeMap<Date, Set<Task>>() ;

        for(Task task: tasks){
            Date timeTask = task.nextTimeAfter(start);
            while (timeTask !=null && timeTask.compareTo(end)<=0){
                if(calendarTask.containsKey(timeTask)){
                    calendarTask.get(timeTask).add(task);
                }
                else{
                    Set <Task> first = new HashSet<Task>();
                    first.add(task);
                    calendarTask.put(timeTask, first);
                }
                timeTask = task.nextTimeAfter(timeTask);
            }
        }
        return calendarTask;
    }

}
