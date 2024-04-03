package logic;

import model.SelectionPolicy;
import model.Server;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private final List<Thread> threads;
    private int maxNoServers;
    private int maxTasksPerServer;
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

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
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
}
