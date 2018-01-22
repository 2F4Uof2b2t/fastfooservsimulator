package com.sasha.mcdonalds_sim.RoleThreads;

import com.sasha.mcdonalds_sim.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TBL extends TimerTask {

    public static ArrayList<Employee> workers = new ArrayList<>();
    public static ArrayList<Customer> queue = new ArrayList<>();
    public static ArrayList<Customer> workingqueue = new ArrayList<>();


    public Employee theWorker;

    public TBL(Employee theWorker) {
        this.theWorker = theWorker;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        if (queue.size() > 0) {
            Customer pair = queue.get(0);
            int i = 0;
            try {
                while (workingqueue.contains(pair)) {
                    ++i;
                    pair = queue.get(i);
                }
            }
            catch (Exception e) {
                timer.schedule(new TBL(theWorker), 5000);
                return;
            }
            workingqueue.add(pair);
            Main.printlTime(theWorker.name + " is making " + pair.orderSize_food + " food items for " + ((pair.orderKind == OrderKind.DRIVE_THRU) ? "DT" : "FC"));
            while (Values.burgerStock < pair.orderSize_food) {
                Main.printlTime(">>> NOT ENOUGH BURGER MEAT <<<");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep((pair.orderSize_food * theWorker.getTimeForTask())*100);
            }
            catch (InterruptedException e) {
                timer.schedule(new TBL(theWorker), 0);
                e.printStackTrace();
                return;
            }
            Values.burgerStock -= pair.orderSize_food;
            Main.printlTime(theWorker.name + " finished making " + pair.orderSize_food + " food items.");
            queue.remove(pair);
            workingqueue.remove(pair);
            timer.schedule(new TBL(theWorker), 5000);
            return;
        }
        timer.schedule(new TBL(theWorker), 5000);
    }
}
