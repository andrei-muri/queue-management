package logic;

import model.Server;
import model.Task;

import java.util.List;

interface Strategy {
    void addTask(List<Server> servers, Task task);
}
