package com.sasha.mcdonalds_sim.RoleThreads;

import com.sasha.mcdonalds_sim.Customer;
import com.sasha.mcdonalds_sim.Employee;
import com.sasha.mcdonalds_sim.Main;
import javafx.util.Pair;

import java.util.*;

public class DT_OT extends TimerTask {

    public static ArrayList<Employee> workers = new ArrayList<>();
    public Employee theWorker;

    public static ArrayList<Pair<Customer, Integer>> queue = new ArrayList<>();

    public DT_OT(Employee theWorker) {
        this.theWorker = theWorker;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        if (queue.size() > 0) {
            Customer customer = null;// = queue.get(0).getKey();
            for (Pair<Customer, Integer> pair : queue) {
                if (pair.getValue() == 0) {
                    customer = pair.getKey();
                    break;
                }
            }
            if (customer == null) {
                timer.schedule(new DT_OT(theWorker), 5000);
                return;
            }
            while (Main.isDriveThruFull()) {
                Main.printlTime(">>> DRIVE THRU IS FULL <<<");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Main.printlTime(theWorker.name + " begins taking a customer's order in DT (size: " + customer.orderSize_food + " - drinks: " + customer.orderSize_drink + " - speed: " + customer.orderSpeed + ")");
            try {
                Thread.sleep((customer.orderSize_food * (customer.orderSpeed + theWorker.getTimeForTask())) * 100);
            } catch (InterruptedException e) {
                timer.schedule(new DT_OT(theWorker), 0);
                e.printStackTrace();
            }
            queue.remove(0);
            queue.add(0, new Pair<>(customer, 1));
            timer.schedule(new DT_OT(theWorker), 0);
            Main.printlTime(theWorker.name + " finishes taking the order");
            return;
        }
        timer.schedule(new DT_OT(theWorker), 5000);
    }
}
