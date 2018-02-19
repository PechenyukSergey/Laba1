package model;

import java.util.Iterator;

/**
 * Created by veld on 31.10.2017.
 */

public class LinkedTaskList extends TaskList {
    private Node header;
    private int size = 0;

    public LinkedTaskList( ) {
        header = new Node( null );
    }

    public boolean isEmpty( ) {
        return header == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Task getTask(int index) {
        Node itr = header;
        int i=0;
        while (itr.getNext()!= null && i != index) {
            itr = itr.getNext();
            i++;
        }
        return itr.getNext().getItem();
    }

    @Override
    public void add(Task task) {
        if ( task == null)
            throw new NullPointerException();

        Node taskNode = new Node(task);
        if ( header == null){
            header = taskNode;
        }
        else{
            Node t = this.header;
            while (t.getNext()!= null)
                t = t.getNext();
            t.setNext(taskNode);
        }
        size++;
    }

    @Override
    public boolean remove(Task task ) {
        boolean removed = false;

        if ( task == null || this.header == null)
            throw new NullPointerException();

        if (header.getItem() != null && header.getItem().equals(task)){
            header = header.getNext();
            removed = true;
            size--;
        }
        else {
            Node current = this.header;
            while(current.getNext() != null && !current.getNext().getItem().equals(task)){
                current = current.getNext();
            }
            if(current.getNext() != null ){
                current.setNext(current.getNext().getNext());
                removed = true;
                size--;
            }
        }
        return removed;

    }


    // Simple print method
    public void printTaskList(  ) {
        if( this.isEmpty() )
            System.out.print( "Empty list" );
        else {
            Iterator<Task> t = iterator();
            while (t.hasNext())
                System.out.println(t.next().getTitle());
        }
    }


    @Override
    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            private Node current = header;
            private Node next = header.getNext();
            private boolean removed = false;

            @Override
            public boolean hasNext() {
                return next!= null;
            }

            @Override
            public Task next() throws IndexOutOfBoundsException {

                if (!hasNext()) throw new IndexOutOfBoundsException("End of list.");
                Task result = next.getItem();
                current = next;
                next = next.getNext();
                removed = false;
                return result;
            }

            @Override
            public void remove(){
                if(current.getItem() == null || removed)
                    throw new IllegalStateException();

                LinkedTaskList.this.remove (current.getItem());
                removed = true ;
            }
        };
    }

    //@Override
    public LinkedTaskList clone() {
        LinkedTaskList copy =(LinkedTaskList)super.clone();
        copy.header = copy.header.clone();

        return copy;
    }

    @Override
    public boolean equals (Object obj){

        if ((obj == null ) || !obj.getClass().equals(this.getClass())){
            return false;
        }
        LinkedTaskList list = (LinkedTaskList) obj;
        if(list==(obj) || (list.size() == size())){

            Node f = this.header;
            Node s = list.header;
            while (f.getNext()!= null) {
                if (!s.getNext().getItem().equals(f.getNext().getItem()))
                    return false;
                f = f.getNext();
                s = s.getNext();
            }
            return true;
        }  else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        int listHash = 0;

        Node t = header;
        while (t.getNext()!= null) {
            listHash += t.getNext().getItem().hashCode();
            t = t.getNext();
        }
        return listHash;
    }

    @Override
    public String toString(){
        String a = "LinkedTaskList \n";
        if( this.isEmpty() )
            System.out.print( "Empty list" );
        else {
            Iterator<Task> t = iterator();
            while (t.hasNext())
                a = a + t.next().toString() + "\n";
        }
        System.out.println(a);
        return a;
    }
}

class Node implements Cloneable{
    private Task   item;
    private Node next;

    public Node( Task item ) {
        this.item = item;
        this.next    = null;
    }
    public Node( Task item, Node next ) {
        this.item = item;
        this.next = next;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Task getItem() {
        return item;
    }

    public void setItem(Task item) {
        this.item = item;
    }

    public Node clone(){
        try{
            Node copy = (Node)super.clone();
            if (copy.getNext() != null) {
                copy.setNext(next.clone());
            }
            return copy;
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError("");
        }
    }
}



