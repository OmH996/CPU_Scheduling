public class Metrics {

    // Turnaround Time = Completion Time - Arrival Time
    public static int calculateTurnaroundTime(Process p) {

        return p.getCompletionTime() - p.getArrivalTime();
    }

    // Waiting Time = Turnaround Time - Burst Time
    public static int calculateWaitingTime(Process p) {

        return p.getTurnaroundTime() - p.getBurstTime();
    }

    // Response Time = First Start Time - Arrival Time
    public static int calculateResponseTime(Process p) {

        return p.getFirstStartTime() - p.getArrivalTime();
    }

    // Calculate all metrics for one process
    public static void calculateProcessMetrics(Process p) {

        p.setTurnaroundTime(calculateTurnaroundTime(p));

        p.setWaitingTime(calculateWaitingTime(p));

        p.setResponseTime(calculateResponseTime(p));
    }

    // Calculate average of any metric
    public static double calculateAverage(int total, int count) {

        if (count == 0) {
            return 0;
        }

        return (double) total / count;
    }

    // Average Waiting Time
    public static double calculateAverageWaitingTime(
            java.util.List<Process> processes) {

        int total = 0;

        for (Process p : processes) {
            total += p.getWaitingTime();
        }

        return calculateAverage(total, processes.size());
    }

    // Average Turnaround Time
    public static double calculateAverageTurnaroundTime(
            java.util.List<Process> processes) {

        int total = 0;

        for (Process p : processes) {
            total += p.getTurnaroundTime();
        }

        return calculateAverage(total, processes.size());
    }

    // Average Response Time
    public static double calculateAverageResponseTime(
            java.util.List<Process> processes) {

        int total = 0;

        for (Process p : processes) {
            total += p.getResponseTime();
        }

        return calculateAverage(total, processes.size());
    }
}