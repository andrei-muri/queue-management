package logic;

import model.SelectionPolicy;
import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimulationManager implements Runnable{
    public int timeLimit = 30;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int numberOfServers = 2;
    public int numberOfClients = 4;
    public SelectionPolicy policy = SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    private List<Task> tasks;

    public SimulationManager() {
        scheduler = new Scheduler(2, 5);
        scheduler.setStrategy(policy);
        scheduler.start();
        tasks = new ArrayList<>(List.of(
                new Task(1, 2, 9),
                new Task(2, 2, 1),
                new Task(3, 2, 2),
                new Task(4, 3, 7),
                new Task(5, 3, 9),
                new Task(6, 4, 1),
                new Task(7, 5, 2),
                new Task(8, 5, 3),
                new Task(9, 5, 8),
                new Task(10, 6, 8),
                new Task(11, 7, 8),
                new Task(12, 8, 8)

//                new Task(1, 2, 2),
//                new Task(2, 3, 3),
//                new Task(3, 4, 3),
//                new Task(4, 10, 2)

//                new Task(1, 5, 2),
//                new Task(2, 6, 4),
//                new Task(3, 7, 2),
//                new Task(4, 8, 3)

//                new Task(1, 3, 3),
//                new Task(2, 4, 2),
//                new Task(3, 5, 4),
//                new Task(4, 9, 2)
        ));
    }

    @Override
    public void run() {
        int currentTime = 0;
        while(currentTime < timeLimit) {
            if(!tasks.isEmpty()) {
                Iterator<Task> iterator = tasks.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getArrivalTime() > currentTime) {
                        break;
                    } else if (task.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(task);
                        iterator.remove();
                    }
                }
            }
            printState(currentTime);
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        scheduler.end();
        Thread.currentThread().interrupt();
    }

    private void printState(int time) {
        System.out.printf("Time %d\n", time);
        System.out.print("Waiting clients: ");
        for(Task task : tasks) {
            System.out.print(task + "; ");
        }
        System.out.print("\n");
        List<Server> servers = scheduler.getServers();
        int i = 1;
        for(Server server : servers) {
            if(server.getQueueSize() == 0) {
                System.out.printf("Queue %d : closed\n", i++);
            } else {
                System.out.printf("Queue %d: %s\n", i++, server);
            }
        }
    }

    public static void main(String[] args) {
        SimulationManager simulationManager = new SimulationManager();
        Thread t = new Thread(simulationManager);
        t.start();
    }
}
