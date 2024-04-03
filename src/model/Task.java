package model;

public class Task {
    private final int ID;
    private final int arrivalTime;
    private int serviceTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
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

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", ID, arrivalTime, serviceTime);
    }
}
