public class Process {

    private String processId;

    private int arrivalTime;
    private int burstTime;
    private int remainingTime;

    private int completionTime;
    private int firstStartTime = -1;

    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;

    private int priority;

    public Process(String processId, int arrivalTime, int burstTime, int priority) {

        if (processId == null || processId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Process ID cannot be null or empty"
            );
        }

        if (arrivalTime < 0) {
            throw new IllegalArgumentException(
                "Arrival Time cannot be negative"
            );
        }

        if (burstTime <= 0) {
            throw new IllegalArgumentException(
                "Burst Time must be greater than zero"
            );
        }

        if (priority <= 0) {
            throw new IllegalArgumentException("Priority must be greater than zero");
        }

        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
    }

    // Getters
    public String getProcessId() {
        return processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getFirstStartTime() {
        return firstStartTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public int getPriority() { return priority; }

    // Setters
    public void setRemainingTime(int remainingTime) {

        if (remainingTime < 0 || remainingTime > burstTime) {
            throw new IllegalArgumentException(
                "Remaining Time must be between 0 and Burst Time"
            );
        }

        this.remainingTime = remainingTime;
    }

    public void setCompletionTime(int completionTime) {

        if (completionTime < 0) {
            throw new IllegalArgumentException(
                "Completion Time cannot be negative"
            );
        }

        this.completionTime = completionTime;
    }

    public void setFirstStartTime(int firstStartTime) {

        if (firstStartTime < -1) {
            throw new IllegalArgumentException(
                "First Start Time must be -1 or greater"
            );
        }

        this.firstStartTime = firstStartTime;
    }

    public void setWaitingTime(int waitingTime) {

        if (waitingTime < 0) {
            throw new IllegalArgumentException(
                "Waiting Time cannot be negative"
            );
        }

        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {

        if (turnaroundTime < 0) {
            throw new IllegalArgumentException(
                "Turnaround Time cannot be negative"
            );
        }

        this.turnaroundTime = turnaroundTime;
    }

    public void setResponseTime(int responseTime) {

        if (responseTime < 0) {
            throw new IllegalArgumentException(
                "Response Time cannot be negative"
            );
        }

        this.responseTime = responseTime;
    }

    public void setPriority(int priority) {
        if (priority <= 0) {
            throw new IllegalArgumentException(
            "Priority must be greater than zero");
        }

        this.priority = priority;
    }

    // Reset process before running a new simulation
    public void reset() {

        remainingTime = burstTime;
        completionTime = 0;
        firstStartTime = -1;

        waitingTime = 0;
        turnaroundTime = 0;
        responseTime = 0;
    }
}
