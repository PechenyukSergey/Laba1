package model;

import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;
import java.util.Locale;
import java.util.TimerTask;

/**
 * Class Task.
 *
 * @version 1.01 15 Oct 2017
 * @author Sergey Pechenyuk
 */

public class Task extends TimerTask implements Serializable, Cloneable {
    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;
    private boolean repeated;

    private static final Logger logger = Logger.getLogger(Task.class);
    SimpleDateFormat sdate = new SimpleDateFormat("[YYYY-MM-dd HH:mm:ss]", Locale.ENGLISH);


    /**
     * Constructor for task without interval
     * @param title - name of task
     * @param time - date of task
     * @throws IllegalArgumentException
     */
    public Task(String title, Date time) throws IllegalArgumentException {
        this.setTime(time);
        this.active = false;
        this.title = title;
        logger.info("Task \"" + this.title + "\" created. Start date: " +
                sdate.format(time) + ". Active: " + this.active);
    }

    /**
     * Constractor for repeated Task
     * @param title - name of Task
     * @param start - date of Task
     * @param end - end date of Task
     * @param interval - interval of Task in seconds
     * @throws IllegalArgumentException
     */
    public Task(String title, Date start, Date end, int interval) throws IllegalArgumentException{
        if (interval <0) {
            logger.error("The time or interval of " + this.title + " can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start.after(end)) {
            logger.error("The end date of " + this.title + " must not be earlier than start date");
            throw new IllegalArgumentException();
        }

        this.setTime(start, end, interval);
        this.active = false;
        this.title = title;

        logger.info("Task \"" + title + "\" created. Start date: " +
                sdate.format(start) + " End date: " +
                sdate.format(end) + ". Active: " + this.active);
    }


    /**
     * Method Set Name of Task
     * @param title - Name of Task
     */
    public void setTitle(String title) {
        if (title.equals(null)) {
            throw new IllegalArgumentException("Wrong title!!!! ");
        }
        logger.info("\"" + this.title + "\" changed for \"" + title + "\"");
        this.title = title;

    }

    /**
     * Method get Name of Task
     * @return title - return Name jf Task
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return active true - task is active, falls - task isn't active
     */
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        logger.info("\"" + this.title + "\" changed the status from: " + this.active + " to " + active);
        this.active = active;
    }

    /**
     * Method return time of not repeated Task
     * @return start РµСЃР»Рё Р·Р°РґР°С‡Р° РЅРµ РїРѕРІС‚РѕСЂСЏРµС‚СЃСЏ - РІРѕР·РІСЂР°С‰Р°РµС‚ start - РїСЂРё С‚РѕРј, С‡С‚Рѕ start = time (СЃРј РєРѕРЅСЃС‚СЂСѓРєС‚РѕСЂ)
     * @return start РµСЃР»Рё Р·Р°РґР°С‡Р° РїРѕРІС‚РѕСЂСЏРµС‚СЃСЏ, РїСЂРё СЌС‚РѕРј start = start (СЃРј РєРѕРЅСЃС‚СЂСѓРєС‚РѕСЂ)
     */
    public Date getTime() {
        if (isRepeated())
            return start;
        else
            return time;
    }

    public void setTime(Date time) throws IllegalArgumentException  {
        if (time == null) {
            logger.error("The time can not be null");
            throw new IllegalArgumentException("The time can not be null");
        } else {
            if (isRepeated()) {
                logger.info("\"" + this.title + "\" changed the start date from: " +
                        sdate.format(this.start) + " to: " +
                        sdate.format(time) + ". Not repeated");
                this.interval = 0;
                this.end = time;
                this.start = time;
                this.repeated = false;
            }
            this.time = time;
        }
    }

    public Date getStartTime() {
        if (isRepeated()) {
            return start;
        } else {
            return time;
        }
    }

    public Date getEndTime() {
        if (isRepeated()) {
            return end;
        } else {
            return time;
        }
    }

    public int getRepeatInterval() {
        if (isRepeated()) {
            return interval;
        } else {
            return 0;
        }
    }

    public void setInterval(int interval) throws IllegalArgumentException {
        if (isRepeated()) {
            if (interval < 0) {
                logger.error("The interval must be more than 0 sec");
                throw new IllegalArgumentException("The interval must be more than 0sec");
            } else /*if (interval > ((this.end.getTime() - this.start.getTime() ))) {
                logger.error("The interval must be between start and end");
                System.out.println(this.end.getTime() - this.start.getTime());
                throw new IllegalArgumentException("The interval must be between start and end");
            } else */{
                this.interval = interval;
            }
        } else {
            throw new IllegalArgumentException("The task is not repeated");
        }
    }

    public void setTime(Date start, Date end, int interval) throws IllegalArgumentException {
        if (start == null) {
            logger.error("The start of Task \"" + this.title + "\" can not be null");
            throw new IllegalArgumentException();
        }
        if (end.compareTo(start) < 0) {
            logger.error("The end date of \"" + this.title + "\" must not be earlier than start date");
            throw new IllegalArgumentException ("The end date must not be earlier than start date");
        }

        if (!isRepeated()) {
            this.repeated = true;
            this.time = start;
        }
        this.start = start;
        this.end = end;
        this.setInterval(interval);

        logger.info("\"" + this.title + "\" changed the start date from: " +
                sdate.format(this.start) + " to: " +
                sdate.format(start) + ". End date changed to: " +
                sdate.format(this.end) + ". Repeated");

    }

    public boolean isRepeated() {
        return repeated;//this.interval>0;
    }

    public Date nextTimeAfter(Date current) {
        if (current == null)
            throw new ArithmeticException("current must be pozitive");

        if (!isActive())
            return null;                          //not Active

        if (!isRepeated()) {                 //not Repeated
            if (current.compareTo(time) == -1) {
                return time;                //not yet started
            } else {
                return null;                  //time has passed
            }
        } else {                              //Repeated
            if (current.compareTo(end) == 1)
                return null;                  //time has passed
            if (current.compareTo(start) == -1)
                return start;           //not yet started
            //System.out.println(interval);
            long nextTime = interval * (((current.getTime() - start.getTime()) / (interval)) + 1) + start.getTime();
            if (nextTime > end.getTime()) {
                return null;
            } else {
                return new Date(nextTime);     //next time
            }
        }
    }


    @Override
    public String toString(){
        if (isRepeated())
            return "Repeated Task '"+getTitle()+"', started from "+sdate.format(getStartTime())+" to "+sdate.format(getEndTime())
                    +" every "+getRepeatInterval()+ ". Active is " + isActive() + "\n" ;
        else
            return "Not repeated Task '"+getTitle()+ "' started at "+sdate.format(getTime()) + ". Active is " + isActive() + "\n";
    }

    public Task clone() throws CloneNotSupportedException {
        /*Task task;
        if (this.interval ==0) {
            task = new Task(this.getTitle(),(Date)this.start.clone());
        }
        else
            task = new Task(this.getTitle(), (Date)this.start.clone(), (Date)this.end.clone(), this.interval);
        return task;*/
        Task copy =(Task)super.clone();
        return copy;
    }

    @Override
    public boolean equals (Object obj){
        if ((obj == null ) || !obj.getClass().equals(getClass())){
            return false;
        }
        Task task = (Task) obj;
        return (task.getTitle().equals(getTitle())) &&
                (task.getStartTime().equals(getStartTime())) &&
                (task.getEndTime().equals(getEndTime())) &&
                (task.getRepeatInterval() == getRepeatInterval()) &&
                (task.isActive() == isActive());
    }

    public int hashCode(){
        int hash = getTitle().hashCode() +  getStartTime().hashCode() + getEndTime().hashCode() + 3 * getRepeatInterval();
        return hash;
    }

    @Override
    public void run() {
        System.out.println("DING DING....... The task " + getTitle() + " is running.");
    }
}