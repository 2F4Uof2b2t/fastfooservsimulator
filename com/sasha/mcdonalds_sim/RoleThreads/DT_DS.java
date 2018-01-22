package com.sasha.mcdonalds_sim.RoleThreads;

import com.sasha.mcdonalds_sim.Customer;
import com.sasha.mcdonalds_sim.Employee;
import com.sasha.mcdonalds_sim.Main;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DT_DS extends TimerTask {

    public static ArrayList<Employee> workers = new ArrayList<>();
    public Employee theWorker;

    public static ArrayList<Customer> queue = new ArrayList<>();

    public DT_DS(Employee theWorker) {
        this.theWorker = theWorker;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        int index = -1;
        if (DT_OT.queue.size() > 0) {
            Customer customer = null;
            for (Pair<Customer, Integer> pair : DT_OT.queue) {
                if (pair.getValue() == 2) {
                    customer = pair.getKey();
                    index = DT_OT.queue.indexOf(pair);
                    break;
                }
            }
            if (customer == null) {
                timer.schedule(new DT_DS(theWorker), 5000);
                return;
            }
            while(TBL.queue.contains(customer)) {
                Main.printlTime(">>> DISTRIBUTION WAITING ON TABLE <<<");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Main.printlTime(theWorker.name + " gives food to the customer.");
            DT_OT.queue.remove(index);
            try {
                Thread.sleep((15 * customer.orderSize_drink + (theWorker.getTimeForTask()) * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
                timer.schedule(new DT_DS(theWorker), 0);
                return;
            }
            timer.schedule(new DT_DS(theWorker), 5000);
            return;
        }
        timer.schedule(new DT_DS(theWorker), 5000);
    }
}
