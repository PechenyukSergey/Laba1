package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by veld on 31.10.2017.
 */
public abstract class TaskList implements Iterable<Task>, Cloneable,Serializable{
    private int size = 0;
    public abstract Task getTask(int index);
    public abstract void add(Task task);
    public abstract boolean remove(Task task);

    public int size() {
        return size;
    }

    public TaskList incoming(Date from, Date to) throws IllegalAccessException, InstantiationException {
        TaskList incoming = this.getClass().newInstance();
        for ( int i=0; i < this.size();i++) {
            if (this.getTask(i) != null && this.getTask(i).isActive() && this.getTask(i).nextTimeAfter(from) != null
                    && this.getTask(i).nextTimeAfter(from).compareTo(to) <= 0) {
                incoming.add(this.getTask(i));
            }
        }
        return incoming;
    }

    public TaskList clone(){
        try {
            TaskList copy =(TaskList)super.clone();
            return copy;
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
            throw new AssertionError("");
        }
    }

    public String toString(){
        String a = "TaskListType [";
        for (int i = 1;i < size()+1;i++){
            a = a + "Task" + i +" ";
        }
        a = a +"]";
        return a;
    }

}
