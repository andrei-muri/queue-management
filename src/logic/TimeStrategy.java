package logic;

import model.Server;
import model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = servers.getFirst();
        int smallestWaitingTime = server.getWaitingPeriod().intValue();

        for(int i = 1; i < servers.size(); i++) {
            int currentWaitingTime = servers.get(i).getWaitingPeriod().intValue();
            if(currentWaitingTime < smallestWaitingTime) {
                smallestWaitingTime = currentWaitingTime;
                server = servers.get(i);
            }
        }
//        for(Server serverComp : servers) {
//            int currentWaitingTime = serverComp.getWaitingPeriod().intValue();
//            if(currentWaitingTime < smallestWaitingTime) {
//                smallestWaitingTime = currentWaitingTime;
//                server = serverComp;
//            }
//        }

        server.addTask(task);
    }
}
