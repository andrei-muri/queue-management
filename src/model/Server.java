package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private final List<Task> discardedTasks;
    private AtomicInteger waitingPeriod;
    private double averageWaitingTime;
    private AtomicInteger tasksNo;

    private boolean execute = false;

    public Server(int maxTask) {
        this.tasks = new ArrayBlockingQueue<>(maxTask);
        this.waitingPeriod = new AtomicInteger(0);
        this.tasksNo = new AtomicInteger(0);;
        this.discardedTasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        try {
            tasks.put(task);
            waitingPeriod.getAndAdd(task.getServiceTime());
            //System.out.println("Task successfully added: " + task);
            tasksNo.getAndIncrement();
        } catch(InterruptedException e) {
            System.out.println("Thread interrupted while trying to put element in queue");
            Thread.currentThread().interrupt();
        }

    }

    public void timedExecute() {
        synchronized (this.tasks) {
            execute = true;
            this.tasks.notifyAll();
        }
    }

    @Override
    public void run() {
        List<Integer> waitingTimes = new ArrayList<>();
        int waitingTime = 1;
        while(!Thread.currentThread().isInterrupted()) {
            synchronized(this.tasks) {
                while(!execute) {
                    try {
                        this.tasks.wait();
                    } catch(InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if(Thread.currentThread().isInterrupted()) {
                        break;
                    }
                }
                execute = false;
            }
            if(Thread.currentThread().isInterrupted()) {
                break;
            }
            if(!tasks.isEmpty()) {
                Task peek = this.tasks.peek();
                if(peek != null && peek.getServiceTime() != 1) {
                    peek.decrementServiceTime();
                    waitingPeriod.getAndDecrement();
                    waitingTime++;
                }  else {
                    //System.out.println(Thread.currentThread().getName() + " id: " + peek.getID() + " " + waitingTime);
                    discardedTasks.add(tasks.poll());
                    tasksNo.getAndDecrement();
                    waitingTimes.add(waitingTime);

                    waitingTime = 1;
                }
            }
        }
    }

    //    @Override
//    public void run() {
//        List<Integer> waitingTimes = new ArrayList<>();
//
//        while(!Thread.currentThread().isInterrupted()) {
//            if(!tasks.isEmpty()) {
//                Task peek = this.tasks.peek();
//                int time = 0;
//                while(peek.getServiceTime() != 0) {
//                    peek.decrementServiceTime();
//                    waitingPeriod.getAndDecrement();
//                    time++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch(InterruptedException e) {
//                        System.out.println("Thread interrupted while serving");
//                        Thread.currentThread().interrupt();
//                        break;
//                    }
//                }
//                if(Thread.currentThread().isInterrupted()) {
//                    break;
//                }
//                tasks.poll();
//                tasksNo.getAndDecrement();
//                waitingTimes.add(time);
//            }
//        }
//        this.averageWaitingTime = waitingTimes.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
//        //System.out.println(averageWaitingTime);
//    }

    public int getQueueSize() {
        return (tasks != null) ? tasks.size() : 0;
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
                string.append(String.format("(%d, %d, %d); ", task.getID(), task.getArrivalTime(), task.getServiceTime()));
                isFirst = false;
            } else {
                string.append(String.format("(%d, %d, %d); ", task.getID(), task.getArrivalTime(), task.getServiceTime()));
            }
        }
        //string.append(String.format(" Waiting period: %d", waitingPeriod.intValue() + 1));

        return string.toString();
    }


    public AtomicInteger getWaitingPeriod() {
        return (tasks.isEmpty()) ? new AtomicInteger(0) : new AtomicInteger(waitingPeriod.intValue() + 1);
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public int getSize() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return discardedTasks;
    }

    public BlockingQueue<Task> getCurrentTasks() {
        return tasks;
    }
}
