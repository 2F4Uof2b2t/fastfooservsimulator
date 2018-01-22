package com.sasha.mcdonalds_sim;

public class Employee {
    public ExperienceLevel experienceLevel;
    public String name;
    public WorkRole role;

    private int timeForTask;

    public Employee(String name, ExperienceLevel lvl, WorkRole role) {
        this.experienceLevel = lvl;
        this.name = name;
        this.role = role;
        timeForTask = Main.estTimeForEmployee(this);
    }

    public int getTimeForTask() {
        return timeForTask;
    }
}
