package logic;

import model.SelectionPolicy;
import model.Server;
import model.Task;
import view.SimulationView;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.util.*;

public class SimulationManager implements Runnable {
    SimulationView view;
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy policy;

    private final Scheduler scheduler;
    private final List<Task> tasks;
    private final FileWriter file;

    private double averageServiceTime;

    public SimulationManager(int timeLimit,
                             int maxProcessingTime,
                             int minProcessingTime,
                             int maxArrivalTime,
                             int minArrivalTime,
                             int numberOfServers,
                             int numberOfClients,
                             SelectionPolicy policy,
                             SimulationView view) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.policy = policy;
        scheduler = new Scheduler(numberOfServers, 100);
        scheduler.setStrategy(policy);
        scheduler.start();
        tasks = new ArrayList<>();
        generateNRandomTasks();
        this.view = view;
        try {
            this.file = new FileWriter("simulation_results.txt", false);
            file.flush();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printStateFile(int time) {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.printf("\n<--- Time %d --->\n", time);
        printWriter.print("Waiting clients: ");
        for(Task task : tasks) {
            printWriter.print(task + "; ");
        }
        printWriter.print("\n");
        List<Server> servers = scheduler.getServers();
        int i = 1;
        for(Server server : servers) {
            if(server.getQueueSize() == 0) {
                printWriter.printf("Queue %d : closed\n", i++);
            } else {
                printWriter.printf("Queue %d: %s\n", i++, server);
            }
        }
        printWriter.flush();
    }

    private void printResultsToFile() {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.printf("\nThe average waiting time is: %.2f\n", (double) scheduler.getTotalWaitingTime() / (double) numberOfClients);
        printWriter.printf("The peak hour was at time: %d. The total number of clients at the peak hour was: %d\n", scheduler.getPeakHour(), scheduler.getPeakNumberOfClients());
        printWriter.printf("The average service time is: %.2f\n", this.averageServiceTime);
        printWriter.flush();
    }


    private String printStateGUI(int time) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        printWriter.printf("\n<--- Time %d --->\n", time);
        printWriter.print("Waiting clients: ");
        for(Task task : tasks) {
            printWriter.print(task + "; ");
        }
        printWriter.print("\n");
        List<Server> servers = scheduler.getServers();
        int i = 1;
        for(Server server : servers) {
            if(server.getQueueSize() == 0) {
                printWriter.printf("Queue %d : closed\n", i++);
            } else {
                printWriter.printf("Queue %d: %s\n", i++, server);
            }
        }

        printWriter.flush();
        return stringWriter.toString();
    }

    private String printResultsGUI() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        printWriter.printf("\nThe average waiting time is: %.2f\n", (double) scheduler.getTotalWaitingTime() / (double) numberOfClients);
        printWriter.printf("The peak hour was at time: %d. The total number of clients at the peak hour was: %d\n", scheduler.getPeakHour(), scheduler.getPeakNumberOfClients());
        printWriter.printf("The average service time is: %.2f\n", averageServiceTime);

        return stringWriter.toString();
    }


    private void generateNRandomTasks() {
        int id = 1;
        Random random = new Random(ZonedDateTime.now().getSecond());
        for(int i = 0; i < numberOfClients; i++) {
            Task task = new Task(
                    id++,
                    random.nextInt(minArrivalTime, maxArrivalTime + 1),
                    random.nextInt(minProcessingTime, maxProcessingTime + 1));
            tasks.add(task);
        }
        tasks.sort(Comparator.comparing(Task::getArrivalTime));
        this.averageServiceTime = tasks.stream().mapToDouble(Task::getServiceTime).average().orElse(0.0);
    }


    @Override
    public void run() {
        int currentTime = 0;
        while(currentTime < timeLimit) {
            //send the appropriate tasks to the queue
            if(!tasks.isEmpty()) {
                Iterator<Task> iterator = tasks.iterator();
                while(iterator.hasNext()) {
                    Task task = iterator.next();
                    if(task.getArrivalTime() > currentTime) {
                        break;
                    } else if(task.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(task);
                        iterator.remove();
                    }
                }
            }
            scheduler.incrementWaitingTimes();
            //print state
            this.view.updateSimulationArea(printStateGUI(currentTime));
            printStateFile(currentTime);
            currentTime++;
            //wake up the servers
            scheduler.notifyServers();
            scheduler.computePeakHour(currentTime);
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        scheduler.end();
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        //print results
        this.view.updateSimulationArea(printResultsGUI());
        printResultsToFile();
        Thread.currentThread().interrupt();
    }
}
