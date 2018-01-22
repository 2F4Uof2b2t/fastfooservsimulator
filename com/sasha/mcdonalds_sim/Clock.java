package com.sasha.mcdonalds_sim;

import java.util.Timer;
import java.util.TimerTask;

public class Clock extends TimerTask {

    public static int seconds = 0;
    public final static int day = 3600 * 24;

    //
    public static Timer grillTimer = new Timer();
    public static Timer customerArriveTimer = new Timer();
    public static Timer dtotTimer = new Timer();
    public static Timer dtcsTimer = new Timer();
    public static Timer fcTimer = new Timer();
    public static Timer tblTimer = new Timer();
    public static Timer dtrunTimer = new Timer();
    public static Timer fcrunTimer = new Timer();
    //


    @Override
    public void run() {
        seconds++;
        Main.mainTimer.schedule(new Clock(), 1000);
    }
}
