package model;

import java.util.Iterator;

/**
 * Class ArrayTaskList.
 *
 * @version 1.01 15 Oct 2
 * @author Sergey Pechenyuk
 */
public class ArrayTaskList extends TaskList {
    private final int INIT_SIZE = 4;
    private final float CUT_RATE = 2.0f;
    private int size = 0;
    private Task[] listTask;

    public ArrayTaskList(){
        listTask= new Task[INIT_SIZE];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Task getTask(int index) { return listTask[index]; }

    @Override
    public void add(Task task) {
        if(size == listTask.length-1) {
            resize ((int) (listTask.length * CUT_RATE));
        }
        listTask[size] = task;
        size++;
    }

    @Override
    public boolean remove(Task task) {
        boolean removed = false;
        int index = 0;                              //index of the task to be delete
        //search for the index of the element to be deleted
        for (int i = 0; i < size; i++) {
            if (listTask[i].equals(task)) {        //looking for the first match
                index = i;
                removed = true;
                break;
            }
        }

        if (removed) {
            for (int i = index; i < size; i++) {
                listTask[i] = listTask[i + 1];
            }
            listTask[size] = null;
            size--;

            if (listTask.length > INIT_SIZE && size < listTask.length / CUT_RATE)
                resize ((int) ((listTask.length / CUT_RATE) + 1));
        }
        return removed;
    }

    private void resize(int newLength) {
        Task[] newArrTask = new Task[newLength];
        System.arraycopy(listTask, 0, newArrTask, 0, size);
        listTask = newArrTask;
    }

    public void printTaskList() {
        if( this.listTask[0]== null )
            System.out.print( "Empty list" );
        else {
            Iterator<Task> t = this.iterator();
            while (t.hasNext())
                System.out.println(t.next().getTitle());
        }
        System.out.println( );
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            private Task current = listTask[0];
            private int i = 0;
            private boolean removed = false;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public Task next() throws IndexOutOfBoundsException {
                Task result = current;
                if (!hasNext()) throw new IndexOutOfBoundsException("End of list.");
                if (current != null) {
                    removed = true;
                    current = listTask[++i];
                }
                return result;
            }

            @Override
            public void remove(){
                if (removed == false)
                    throw new IllegalStateException();
                else
                    ArrayTaskList.this.remove(listTask[--i]);
                removed = false;
            }

        };
    }

    /*public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList copy =(ArrayTaskList) super.clone();
        copy.listTask = this.listTask.clone();
        return copy;
    }*/


    public boolean equals (Object obj){
        if ((obj == null ) || !obj.getClass().equals(getClass()))
            return false;

        ArrayTaskList arr = (ArrayTaskList) obj;
        if (arr == (obj)){
            Iterator first = this.iterator();
            Iterator second = arr.iterator();
            while(first.hasNext()){
                if (!second.next().equals(first.next()))
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public int hashCode(){
        int ArrayHash = 0;

        Iterator<Task> t = this.iterator();
        Task k;
        while (t.hasNext()){
            k = t.next();
            ArrayHash += k.hashCode();
        }
        return ArrayHash;
    }
}