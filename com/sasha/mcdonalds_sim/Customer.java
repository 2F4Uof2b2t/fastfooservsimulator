package com.sasha.mcdonalds_sim;

import com.sasha.mcdonalds_sim.RoleThreads.DT_OT;
import com.sasha.mcdonalds_sim.RoleThreads.FC;
import javafx.util.Pair;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.sasha.mcdonalds_sim.Customer.timePer;

public class Customer {

    public int orderSize_food;
    public int orderSize_drink; //dt use only
    public int orderSpeed;
    public OrderKind orderKind;

    public static long timePer;


    /** times are always in seconds **/
    public Customer(int orderSize_food, int orderSize_drink, int orderSpeed, OrderKind orderKind) {
        this.orderSize_food = orderSize_food;
        this.orderSize_drink = orderSize_drink;
        this.orderSpeed = orderSpeed;
        this.orderKind = orderKind;
    }
}
class CustomerArrive extends TimerTask {

    @Override
    public void run() {
        Timer timer = new Timer();
        Random random = new Random();
        if (random.nextBoolean()) {
            FC.queue.add(new Customer(random.nextInt(50), 0, random.nextInt(30), OrderKind.WINDOW));
            timer.schedule(new CustomerArrive(), timePer * 1000);
            Main.printlTime("Customer arrives at front counter");
            return;
        }
        DT_OT.queue.add(new Pair<>(new Customer(random.nextInt(50), random.nextInt(10), random.nextInt(30), OrderKind.DRIVE_THRU), 0));
        Main.printlTime("Customer arrives in DT");
        timer.schedule(new CustomerArrive(), timePer * 1000);
    }
}
