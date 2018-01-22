package com.sasha.mcdonalds_sim.RoleThreads;

import com.sasha.mcdonalds_sim.Customer;
import com.sasha.mcdonalds_sim.Employee;
import com.sasha.mcdonalds_sim.Main;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FC extends TimerTask {

    public static ArrayList<Employee> workers = new ArrayList<>();
    public static ArrayList<Customer> queue = new ArrayList<>();

    public Employee theWorker;

    public FC(Employee e) {
        theWorker = e;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        if (queue.size() > 0) {
            Customer customer = queue.get(0);
            Main.printlTime(theWorker.name + " begins taking a customer's order at FC (size: " + customer.orderSize_food + " - speed: " + customer.orderSpeed + ")");
            queue.remove(0);
            try {
                Thread.sleep((customer.orderSize_food * (customer.orderSpeed + theWorker.getTimeForTask())) * 100);
            } catch (InterruptedException e) {
                timer.schedule(new FC(theWorker), 0);
                e.printStackTrace();
            }
            TBL.queue.add(customer);
            timer.schedule(new FC(theWorker), 0);
            Main.printlTime(theWorker.name + " finishes taking the order");
            return;
        }
        timer.schedule(new FC(theWorker), 5000);
    }
}
