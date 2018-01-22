package com.sasha.mcdonalds_sim.RoleThreads;

import com.sasha.mcdonalds_sim.Employee;
import com.sasha.mcdonalds_sim.Main;
import com.sasha.mcdonalds_sim.Values;
import com.sasha.mcdonalds_sim.WorkRole;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Grill extends TimerTask {

    public static ArrayList<Employee> workers = new ArrayList<>();
    public Employee theWorker;

    public Grill(Employee e) {
        theWorker = e;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        if (Values.burgerStock <= 100) {
            Main.printlTime(theWorker.name + " is cooking 8 burgers...");
            // burgers cook in batches of 8 per worker, for now
            try {
                Thread.sleep((60 + theWorker.getTimeForTask()) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                timer.schedule(new Grill(theWorker), 0);
            }
            Values.burgerStock = Values.burgerStock + 8;
            Main.printlTime(theWorker.name + " cooked 8 burgers, raising stock to " + Values.burgerStock);
            timer.schedule(new Grill(theWorker), 0);
            return;
        }
        timer.schedule(new Grill(theWorker), 10000);
    }
}
