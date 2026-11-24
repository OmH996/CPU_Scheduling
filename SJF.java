import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SJF {

    public enum SegmentType {
        RUNNING,
        CONTEXT_SWITCH,
        IDLE
    }

    public static class Segment {

        private String processId;
        private int startTime;
        private int endTime;
        private SegmentType type;

        public Segment(String processId, int startTime,int endTime, SegmentType type) {
            this.processId = processId;
            this.startTime = startTime;
            this.endTime = endTime;
            this.type = type;
        }

        public String getProcessId() {
            return processId;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public SegmentType getType() {
            return type;
        }

        public int getDuration() {
            return endTime - startTime;
        }
    }

    private final List<Process> processes;
    private final List<Segment> ganttChart;

    private int currentTime;
    private final int contextSwitchTime;

    private Process lastExecutedProcess;

    public SJF(List<Process> processes, int contextSwitchTime) {
        if (processes == null) {
            throw new IllegalArgumentException(
                "Process list cannot be null"
            );
        }

        if (contextSwitchTime < 0) {
            throw new IllegalArgumentException(
                "Context Switching time cannot be negative"
            );
        }

        this.processes = new ArrayList<>(processes);

        Set<String> processIds = new HashSet<>();
        Set<Process> processObjects =
                java.util.Collections.newSetFromMap(
                    new java.util.IdentityHashMap<>()
                );

        for (Process p : this.processes) {

            if (p == null) {
                throw new IllegalArgumentException(
                    "Process list cannot contain null"
                );
            }

            if (!processObjects.add(p)) {
                throw new IllegalArgumentException(
                    "The same Process object cannot appear twice"
                );
            }

            if (!processIds.add(p.getProcessId())) {
                throw new IllegalArgumentException(
                    "Process IDs must be unique: " + p.getProcessId()
                );
            }
        }

        this.contextSwitchTime = contextSwitchTime;
        this.ganttChart = new ArrayList<>();

        this.currentTime = 0;
        this.lastExecutedProcess = null;
    }

    private Process getShortestRemainingProcess(Process preferredProcess) {

        Process shortest = null;

        for (Process p : processes) {

            if (p.getArrivalTime() <= currentTime
                    && p.getRemainingTime() > 0) {

                if (shortest == null
                        || p.getRemainingTime()
                        < shortest.getRemainingTime()) {

                    shortest = p;
                }
            }
        }

        if (preferredProcess != null
                && preferredProcess.getArrivalTime() <= currentTime
                && preferredProcess.getRemainingTime() > 0
                && shortest != null
                && preferredProcess.getRemainingTime()
                    == shortest.getRemainingTime()) {

            return preferredProcess;
        }

        return shortest;
    }

    private boolean allProcessesFinished() {

        for (Process p : processes) {

            if (p.getRemainingTime() > 0) {
                return false;
            }
        }

        return true;
    }

    private void addSegment(String processId,int startTime,int endTime,SegmentType type) {

        if (startTime >= endTime) {
            return;
        }

        if (!ganttChart.isEmpty()) {
            Segment last = ganttChart.get(ganttChart.size() - 1);

            if (last.getType() == type && last.getProcessId().equals(processId) && last.getEndTime() == startTime) {

                ganttChart.set(
                    ganttChart.size() - 1,
                    new Segment(
                        processId,
                        last.getStartTime(),
                        endTime,
                        type
                    )
                );

                return;
            }
        }

        ganttChart.add(new Segment(processId, startTime, endTime, type));
    }

    public void schedule() {

        for (Process p : processes) {
            p.reset();
        }

        currentTime = 0;
        lastExecutedProcess = null;
        ganttChart.clear();

        while (!allProcessesFinished()) {
            Process currentProcess = getShortestRemainingProcess(lastExecutedProcess);

            if (currentProcess == null) {

                int startTime = currentTime;
                currentTime++;

                addSegment(
                    "IDLE",
                    startTime,
                    currentTime,
                    SegmentType.IDLE
                );

                lastExecutedProcess = null;
                continue;
            }

            if (lastExecutedProcess != null && lastExecutedProcess != currentProcess && contextSwitchTime > 0) {
                int startTime = currentTime;

                currentTime += contextSwitchTime;

                addSegment("CS",startTime,currentTime,SegmentType.CONTEXT_SWITCH);

                currentProcess = getShortestRemainingProcess(currentProcess);
            }

            if (currentProcess.getFirstStartTime() == -1) {

                currentProcess.setFirstStartTime(currentTime);
            }

            int startTime = currentTime;

            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

            currentTime++;

            addSegment(currentProcess.getProcessId(),startTime,currentTime,SegmentType.RUNNING);

            if (currentProcess.getRemainingTime() == 0) {

                currentProcess.setCompletionTime(currentTime);
            }

            lastExecutedProcess = currentProcess;
        }

        calculateMetrics();
    }

    private void calculateMetrics() {

        for (Process p : processes) {
            Metrics.calculateProcessMetrics(p);
        }
    }

    public void printGanttChart() {

        System.out.println("\nGantt Chart:");

        if (ganttChart.isEmpty()) {
            System.out.println("No execution segments.");
            return;
        }

        System.out.print(ganttChart.get(0).getStartTime());

        for (Segment segment : ganttChart) {

            System.out.printf(
                " | %-4s | %d",
                segment.getProcessId(),
                segment.getEndTime()
            );
        }

        System.out.println();

        System.out.println("\nExecution Segments:");

        System.out.printf(
            "%-15s %-15s %-15s %-20s%n",
            "Process", "Start Time", "End Time", "Type"
        );

        for (Segment segment : ganttChart) {

            System.out.printf(
                "%-15s %-15d %-15d %-20s%n",
                segment.getProcessId(),
                segment.getStartTime(),
                segment.getEndTime(),
                segment.getType()
            );
        }
    }

    public void printResults() {

        System.out.println("\nProcess Results:");

        System.out.printf(
            "%-10s %-10s %-10s %-10s %-10s %-10s %-10s%n",
            "Process", "AT", "BT", "CT", "WT", "TAT", "RT"
        );

        for (Process p : processes) {

            System.out.printf(
                "%-10s %-10d %-10d %-10d %-10d %-10d %-10d%n",
                p.getProcessId(),
                p.getArrivalTime(),
                p.getBurstTime(),
                p.getCompletionTime(),
                p.getWaitingTime(),
                p.getTurnaroundTime(),
                p.getResponseTime()
            );
        }

        System.out.println(
            "\nAverage Waiting Time: "
            + Metrics.calculateAverageWaitingTime(processes)
        );

        System.out.println(
            "Average Turnaround Time: "
            + Metrics.calculateAverageTurnaroundTime(processes)
        );

        System.out.println(
            "Average Response Time: "
            + Metrics.calculateAverageResponseTime(processes)
        );
    }

    public List<Segment> getGanttChart() {
        return new ArrayList<>(ganttChart);
    }
}
