package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private double averageWaitingTime;
    private final AtomicInteger tasksNo;

    public Server(int maxTask) {
        this.tasks = new ArrayBlockingQueue<>(maxTask);
        this.waitingPeriod = new AtomicInteger(0);
        this.tasksNo = new AtomicInteger(0);
    }

    public void addTask(Task task) {
        try {
            tasks.put(task);
            tasksNo.getAndIncrement();
        } catch(InterruptedException e) {
            System.out.println("Thread interrupted while trying to put element in queue");
            Thread.currentThread().interrupt();
        }
        waitingPeriod.getAndAdd(task.getServiceTime());
    }

    @Override
    public void run() {
        List<Integer> waitingTimes = new ArrayList<>();
        while(!Thread.currentThread().isInterrupted()) {
            if(!tasks.isEmpty()) {
                Task peek = this.tasks.peek();
                int time = 0;
                while(peek.getServiceTime() != 0) {
                    peek.decrementServiceTime();
                    waitingPeriod.getAndDecrement();
                    time++;
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {
                        System.out.println("Thread interrupted while waiting");
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                if(Thread.currentThread().isInterrupted()) {
                    break;
                }
                tasks.poll();
                tasksNo.getAndDecrement();
                waitingTimes.add(time);

            }
        }
        System.out.println(Thread.currentThread().getName() + "waiting times");
        waitingTimes.stream().mapToDouble(Integer::doubleValue).forEach((t) -> System.out.print(" " + t));
        this.averageWaitingTime = waitingTimes.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
        System.out.println(averageWaitingTime);
    }

    public void akds() {
        System.out.println();
    }

    public int getQueueSize() {
        return (!tasks.isEmpty()) ? tasks.size() : 0;
    }

    public int getRemainingCapacity() {
        return tasks.remainingCapacity();
    }
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        boolean isFirst = true;

        for(Task task : tasks) {
            if(isFirst) {
                string.append(String.format("(%d, %d, %d); ", task.getID(), task.getArrivalTime(), task.getServiceTime() + 1));
                isFirst = false;
            } else {
                string.append(String.format("(%d, %d, %d); ", task.getID(), task.getArrivalTime(), task.getServiceTime()));
            }
        }

        return string.toString();
    }


    public AtomicInteger getWaitingPeriod() {
        return (tasks.isEmpty()) ? new AtomicInteger(0) : new AtomicInteger(waitingPeriod.intValue() + 1);
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }
}
