package model;

import org.apache.log4j.Logger;

import java.io.*;
//import java.util.Iterator;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * Class Task.
 *
 * @version 1.01 20 Jan 2018
 * @author Sergey Pechenyuk
 */
public class TaskIO {
    private static final Logger logger = Logger.getLogger(TaskIO.class);
    private static SimpleDateFormat convert = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss:SSS");

    /**
     * Binary writing task to stream from task
     * @param tasks
     * @param out
     */
    public static void write(TaskList tasks, OutputStream out) throws IOException{
        DataOutputStream first = new DataOutputStream(out);
        try{
            int size = tasks.size();
            first.writeInt(size);
            for(Object currTask: tasks){
                Task task = (Task) currTask;
                String title = task.getTitle();
                long start = task.getStartTime().getTime();
                long end = task.getEndTime().getTime();
                int rep = task.getRepeatInterval();
                boolean act = task.isActive();
                first.writeUTF(title);
                first.writeLong(start);
                first.writeLong(end);
                first.writeInt(rep);
                first.writeBoolean(act);
                logger.info("Binary writing task SUCCESSFUL");
            }
        } catch (IOException e) {
            logger.error("Binary writing task in stream ERROR");
        } finally {
            first.flush();
        }
    }

    /**
     * Binary writing task to file from task
     * @param tasks
     * @param filename
     */
    public static void writeBinary(TaskList tasks, File filename) throws IOException {
        FileOutputStream outfile = new FileOutputStream (filename);
        try {
            write(tasks, outfile);
        } catch (IOException e) {
            logger.error("Binary writing task in file ERROR");
        } finally {
            outfile.close();
        }
    }

    /**
     * Binary reading task from stream to tasklist
     * @param tasks
     * @param in
     */
    public static void read(TaskList tasks, InputStream in)throws IOException{
        /*try (ObjectInputStream in2 = new ObjectInputStream(in)) {
            int num = in2.readInt();
            for (int i=0; i<num; i++) {
                Task task = null;
                try {
                    task = (Task) in2.readObject();
                } catch (ClassNotFoundException e) {
                    logger.error("Class not found");
                }
                tasks.add(task);
                logger.info("Binary reading task SUCCESSFUL");
            }
        } catch (IOException e) {
            logger.error("Binary reading task from stream ERROR");
        }*/

        DataInputStream second = new DataInputStream(in);
        int nSize = second.readInt();
        try {
            for(int i = 0; i < nSize; i++){
                String newTitle = second.readUTF();
                Date nStart = new Date( second.readLong());
                Date nEnd = new Date (second.readLong());
                int nRep = second.readInt();
                boolean act = second.readBoolean();
                Task nTask = new Task (newTitle,nStart,nEnd, nRep);
                nTask.setActive(act);
                tasks.add(nTask);
            }
            logger.info("Binary reading task SUCCESSFUL");
        } catch (IOException e) {
            logger.error("Binary reading task from stream ERROR");
        } finally {
            second.close();
        }
    }

    /**
     * Binary reading task from file to tasklist
     * @param tasks
     * @param filename
     */
    public static void readBinary(TaskList tasks, File filename)throws IOException {
        try (FileInputStream inFile = new FileInputStream(filename)) {
            read(tasks, inFile);
        } catch (IOException e) {
            logger.error("Binary reading task from file ERROR");
        }
    }

    /**
     * Text writing task in stream from task
     * @param tasks
     * @param out
     */
    public static void write(TaskList tasks, Writer out)throws IOException {
        try (BufferedWriter outw = new BufferedWriter(out)) {
            for(Object currTask: tasks){
                Task task = (Task) currTask;
                Date nStart  = task.getStartTime();
                Date nEnd = task.getEndTime();
                Boolean act = task.isActive();
                outw.write(task.getTitle());
                outw.write(" && ");
                outw.write(convert.format(nStart));
                outw.write(" && ");
                outw.write(convert.format(nEnd));
                outw.write(" && ");
                outw.write(String.valueOf(task.getRepeatInterval()));
                outw.write(" && ");
                outw.write(String.valueOf(act));
                outw.write(" && ");
                outw.append("\n");
            }
            logger.info("Text writing task SUCCESSFUL");
        } catch (IOException e) {
            logger.error("Text writing task to stream ERROR");
        } finally {
            out.close();
        }
    }

    /**
     * Text writing task to file from task
     * @param tasks
     * @param filename
     */
    public static void writeText(TaskList tasks, File filename)throws IOException {

        FileWriter inFile = new FileWriter(filename);
        try{
            write(tasks, inFile);
        } catch (IOException e) {
            logger.error("Text writing task in file ERROR");
        } finally {
            inFile.close();
        }
    }


    /**
     * Text reading task from stream in tasklist
     * @param tasks
     * @param in
     * @throws ParseException
     */
    public static void read(TaskList tasks, Reader in) throws IOException, ParseException{
        Scanner inps = new Scanner(in).useDelimiter("\\s* && \\s*");

        try{
            while(inps.hasNext()){
                String newTitle = inps.next();
                String start = inps.next();
                Date nStart = convert.parse(start);
                String end = inps.next();
                Date nEnd = convert.parse(end);
                String nRep = inps.next();
                int newRep = Integer.parseInt(nRep);

                Task nTask = new Task (newTitle, nStart,nEnd, newRep);
                nTask.setActive(Boolean.parseBoolean(inps.next()));
                tasks.add(nTask);
            }
            logger.info("Text reading task SUCCESSFUL");
        } catch (ParseException e) {
            logger.error("Text reading task from stream ERROR");
        } finally {
            inps.close();
        }
    }

    /**
     * Text reading task from file to tasklist
     * @param tasks
     * @param filename
     * @throws ParseException
     */
    public static void readText(TaskList tasks, File filename)throws IOException, ParseException{
        FileReader inFile = new FileReader(filename);
        try {
            read(tasks, inFile);
        } catch (FileNotFoundException e) {
            logger.error("File not found");
            System.out.println("File not found");
        } finally {
            inFile.close();
        }
    }
}