package com.sasha.mcdonalds_sim;

import com.sasha.mcdonalds_sim.RoleThreads.*;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

import static com.sasha.mcdonalds_sim.Customer.timePer;

public class Main {

    public final static String[] names = {"Sasha", "Melissa", "Cass", "Alex", "Andrew", "Marisa", "John", "Dan", "Maranda", "Charlotte", "AJ", "DJ", "Taylor", "Grant", "Austin", "Justin", "Lisa", "Summer", "Chang", "Shiloh"};
    public static int crewMembers;
    public static int incomingCustomersPerHour;
    private static ArrayList<String> usedNames = new ArrayList<>();

    public static Timer mainTimer = new Timer();

    public static ArrayList<Employee> employees = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("McD Food Service Speed Simulator (analytical)");
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nCrew Members Amt > ");
        crewMembers = scanner.nextInt();
        if (crewMembers > 20) {
            System.exit(2);
        }
        System.out.print("\nIncoming Customers per Hour > ");
        incomingCustomersPerHour = scanner.nextInt();
        // 20 names
        for (int i = 1; i <= crewMembers; i++) {
            String name = getRandName();
            ExperienceLevel experienceLevel;
            WorkRole workRole;
            System.out.printf("\nWhat is %s's experience level (0-3) > ", name);
            switch (scanner.nextInt()) {
                case 0:
                    experienceLevel = ExperienceLevel.TRAINEE;
                    break;
                case 1:
                    experienceLevel = ExperienceLevel.NOVICE;
                    break;
                case 2:
                    experienceLevel = ExperienceLevel.REGULAR;
                    break;
                case 3:
                    experienceLevel = ExperienceLevel.CAREER;
                    break;
                default:
                    experienceLevel = null;
                    System.exit(1);
            }
            System.out.println("\n");
            System.out.printf("Directory:\n0: Grill\n1: DriveThru Order Taker\n2: DriveThru Cashier\n3: DriveThru Distributer\n4: Front Counter\n5: Table\n6: DriveThru runner\n7: FrontCounter Runner\n\n");
            workRole = getRole(name, scanner);
            employees.add(new Employee(name, experienceLevel, workRole));
        }
        for (Employee e : Main.employees) {
            if (e.role == WorkRole.GRILL) {
                Grill.workers.add(e);
                System.out.printf("\n%s is on grill.", e.name);
            }
            if (e.role == WorkRole.DT_OT) {
                DT_OT.workers.add(e);
                System.out.printf("\n%s is taking orders in drive-thru.", e.name);
            }
            if (e.role == WorkRole.DT_CS) {
                DT_CS.workers.add(e);
                System.out.printf("\n%s is tendering payments for drive-thru.", e.name);
            }
            if (e.role == WorkRole.DT_DS) {
                DT_DS.workers.add(e);
                System.out.printf("\n%s is distributing food for drive-thru.", e.name);
            }
            if (e.role == WorkRole.FC) {
                FC.workers.add(e);
                System.out.printf("\n%s is on frontcounter.", e.name);
            }
            if (e.role == WorkRole.TBL) {
                TBL.workers.add(e);
                System.out.printf("\n%s is on table.", e.name);
            }
            if (e.role == WorkRole.DT_RUN) {
                DT_RUN.workers.add(e);
                System.out.printf("\n%s is running for drive-thru.", e.name);
            }
            if (e.role == WorkRole.FC_RUN) {
                FC_RUN.workers.add(e);
                System.out.printf("\n%s is running for front counter.", e.name);
            }
        }
        mainTimer.schedule(new Clock(), 0);
        System.out.println("\n------- STORE IS NOW OPEN -------\n");
        for (Employee e : Main.employees) {
            if (e.role == WorkRole.GRILL) {
                Timer timer = new Timer();
                timer.schedule(new Grill(e), new Random().nextInt(10000));
            }
            if (e.role == WorkRole.DT_OT) {
                Timer timer = new Timer();
                timer.schedule(new DT_OT(e), new Random().nextInt(10000));
            }
            if (e.role == WorkRole.DT_CS) {
                Timer timer = new Timer();
                timer.schedule(new DT_CS(e), new Random().nextInt(10000));
            }
            if (e.role == WorkRole.DT_DS) {
                Timer timer = new Timer();
                timer.schedule(new DT_DS(e), new Random().nextInt(10000));
            }
            if (e.role == WorkRole.FC) {
                Timer timer = new Timer();
                timer.schedule(new FC(e), new Random().nextInt(10000));
            }
            if (e.role == WorkRole.TBL) {
                Timer timer = new Timer();
                timer.schedule(new TBL(e), new Random().nextInt(10000));
            }
            if (e.role == WorkRole.DT_RUN) {
                Timer timer = new Timer();
                timer.schedule(new Grill(e), new Random().nextInt(10000));
            }
            if (e.role == WorkRole.FC_RUN) {
                Timer timer = new Timer();
                timer.schedule(new Grill(e), new Random().nextInt(10000));
            }
        }
        timePer = 3600 / incomingCustomersPerHour;
        Timer scheduleTimer = new Timer();
        scheduleTimer.schedule(new CustomerArrive(), 1000);
        while (true) {
            if (scanner.nextLine().equalsIgnoreCase("!report")) {
                System.out.println("----STATUS REPORT----");
                System.out.println("Drive Thru: " + DT_OT.queue.size() + " in line.");
                System.out.println("Table: " + TBL.queue.size() + " orders queued.");
            }
        }
    }
    public static void printlTime(String str) {
        System.out.println("[" + Clock.seconds + "] " + str);
    }
    private static String getRandName() {
        String name = names[new Random().nextInt(names.length)];
        if (usedNames.contains(name)) {
            return getRandName();
        }
        usedNames.add(name);
        return name;
    }
    public static WorkRole getRole(String name, Scanner scanner) {
        WorkRole workRole;
        System.out.print("Select a role for " + name + " > ");
        switch (scanner.nextInt()) {
            case 0:
                if (Values.current_Grill >= Values.max_Grill) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.GRILL;
                Values.current_Grill++;
                return workRole;
            case 1:
                if (Values.current_dtot >= Values.max_dtot) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.DT_OT;
                Values.current_dtot++;
                return workRole;
            case 2:
                if (Values.current_dtcs >= Values.max_dtcs) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.DT_CS;
                Values.current_dtcs++;
                return workRole;
            case 3:
                if (Values.current_dtds >= Values.max_dtds) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.DT_DS;
                Values.current_dtds++;
                return workRole;
            case 4:
                if (Values.current_fc >= Values.max_fc) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.FC;
                Values.current_fc++;
                return workRole;
            case 5:
                if (Values.current_tbl >= Values.max_tbl) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.TBL;
                Values.current_tbl++;
                return workRole;
            case 6:
                if (Values.current_dtrun >= Values.max_dtrun) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.DT_RUN;
                Values.current_dtrun++;
                return workRole;
            case 7:
                if (Values.current_fcrun >= Values.max_fcrun) {
                    System.out.println("\nCannot have more of these. try again\n");
                    return getRole(name, scanner);
                }
                workRole = WorkRole.FC_RUN;
                Values.current_fcrun++;
                return workRole;
            default:
                return getRole(name, scanner);
        }
    }
    public static int estTimeForEmployee(Employee e) {
        if (e.role == WorkRole.GRILL) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 45;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 35;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 20;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 13;
            }
        }
        if (e.role == WorkRole.DT_OT) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 25;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 14;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 6;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 3;
            }
        }
        if (e.role == WorkRole.DT_CS) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 30;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 20;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 15;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 10;
            }
        }
        if (e.role == WorkRole.DT_DS) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 20;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 15;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 5;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 5;
            }
        }
        if (e.role == WorkRole.FC) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 30;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 19;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 8;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 6;
            }
        }
        if (e.role == WorkRole.TBL) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 60;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 45;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 20;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 15;
            }
        }
        if (e.role == WorkRole.DT_RUN) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 3;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 2;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 1;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 1;
            }
        }
        if (e.role == WorkRole.FC_RUN) {
            if (e.experienceLevel == ExperienceLevel.TRAINEE) {
                return 3;
            }
            if (e.experienceLevel == ExperienceLevel.NOVICE) {
                return 2;
            }
            if (e.experienceLevel == ExperienceLevel.REGULAR) {
                return 1;
            }
            if (e.experienceLevel == ExperienceLevel.CAREER) {
                return 1;
            }
        }
        return 0;
    }
    public static boolean isDriveThruFull() {
        return DT_OT.queue.size() > 6;
    }
}
