package model;

public class Task {
    private final int ID;
    private final int arrivalTime;
    private int serviceTime;
    private int totalWaitingTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.totalWaitingTime = 0;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getID() {
        return ID;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime() {
        if(this.serviceTime != 0) {
            this.serviceTime--;
        }
    }

    public void incrementWaitingTime() {
        this.totalWaitingTime++;
    }

    public int getTotalWaitingTime() {
        return this.totalWaitingTime;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", ID, arrivalTime, serviceTime);
    }
}
