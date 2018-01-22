package com.sasha.mcdonalds_sim.RoleThreads;

import com.sasha.mcdonalds_sim.Customer;
import com.sasha.mcdonalds_sim.Employee;
import com.sasha.mcdonalds_sim.Main;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DT_CS extends TimerTask {

    public static ArrayList<Employee> workers = new ArrayList<>();
    public Employee theWorker;
    // use queue from DT_CS

    public DT_CS(Employee theWorker) {
        this.theWorker = theWorker;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        int index = -1;
        if (DT_OT.queue.size() > 0) {
            Customer customer = null;
            for (Pair<Customer, Integer> pair : DT_OT.queue) {
                if (pair.getValue() == 1) {
                    customer = pair.getKey();
                    index = DT_OT.queue.indexOf(pair);
                    break;
                }
            }
            if (customer == null) {
                timer.schedule(new DT_CS(theWorker), 5000);
                return;
            }
            Main.printlTime(theWorker.name + " tenders payment in DT.");
            Pair<Customer, Integer> tempPair = DT_OT.queue.get(index);
            Pair<Customer, Integer> newPair = new Pair<>(tempPair.getKey(), 2);
            DT_OT.queue.remove(index);
            DT_OT.queue.add(index, newPair);
            TBL.queue.add(customer);
            tempPair = null;
            try {
                Thread.sleep(theWorker.getTimeForTask() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                timer.schedule(new DT_CS(theWorker), 0);
            }
            timer.schedule(new DT_CS(theWorker), 5000);
            return;
        }
        timer.schedule(new DT_CS(theWorker), 5000);
    }
}
