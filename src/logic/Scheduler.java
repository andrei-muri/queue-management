package logic;

import model.SelectionPolicy;
import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private final List<Thread> threads;
    private int peakHour = 0;
    private int peakNumberOfClients = 0;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        servers = new ArrayList<>(maxNoServers);
        threads = new ArrayList<>(maxNoServers);
        for(int i = 0; i < maxNoServers; i++) {
            Server server = new Server(maxTasksPerServer);
            servers.add(server);
            threads.add(new Thread(server));
        }
        strategy = new TimeStrategy();
    }

    public void setStrategy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        } else {
            strategy = new TimeStrategy();
        }
    }

    public void notifyServers() {
        for(Server server : servers) {
            server.timedExecute();
        }
    }

    public void computePeakHour(int time) {
        int totalSize = servers.stream().mapToInt(Server::getSize).sum();
        if(totalSize > peakNumberOfClients) {
            peakNumberOfClients = totalSize;
            peakHour = time - 1;
        }
    }

    public int getTotalWaitingTime() {
        int sum = 0;
        for(Server server : servers) {
            sum += server.getTasks().stream().mapToInt(Task::getTotalWaitingTime).sum();
        }
        return sum;
    }

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

    public void incrementWaitingTimes() {
        for(Server server : servers) {
            for(Task task : server.getCurrentTasks()) {
                task.incrementWaitingTime();
            }
        }
    }

    public void start() {
        for(Thread thread : threads) {
            thread.start();
        }
    }

    public void end() {
        for(Thread thread : threads) {
            thread.interrupt();
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public int getPeakHour() {
        return peakHour;
    }

    public int getPeakNumberOfClients() {
        return peakNumberOfClients;
    }
}
