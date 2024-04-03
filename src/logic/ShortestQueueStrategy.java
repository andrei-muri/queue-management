package logic;

import model.Server;
import model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = servers.getFirst();
        int smallestSize = server.getQueueSize();

        for(Server serverComp : servers) {
            int currentQueueSize = serverComp.getQueueSize();
            if(currentQueueSize < smallestSize) {
                smallestSize = currentQueueSize;
                server = serverComp;
            }
        }

        server.addTask(task);
    }
}
