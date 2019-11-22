package dm222is;

/*
 * File:	Process.java
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti
 * Date: 	November, 2018
 */

import java.util.ArrayList;

public class FCFS {

	// The list of processes to be scheduled
	public ArrayList<Process> processes;

	// Class constructor
	public FCFS(ArrayList<Process> processes) {
		this.processes = processes;

		/*
		 * Sorting the collection. Argument to sort is a lambda expression for a typical
		 * compareTo-method where 1 is returned if a's arrival time is greater than b's,
		 * 0 if a's arrival time is equal to b's and -1 if a's arrival time is lesser
		 * than b's
		 */
		processes.sort((a, b) -> {
			return a.getArrivalTime() - b.getArrivalTime();
		});
	}

	public void run() {

		/*
		 * Extracting the first process to later use it as a container for last process
		 * before current process later on
		 */
		Process previousProcess = processes.get(0);
		previousProcess.setCompletedTime(previousProcess.getArrivalTime() + previousProcess.getBurstTime());
		previousProcess.setTurnaroundTime(previousProcess.getCompletedTime() - previousProcess.getArrivalTime());
		previousProcess.setWaitingTime(previousProcess.getTurnaroundTime() - previousProcess.getBurstTime());

		/*
		 * Starting with the second process as the first one was saved for earlier
		 * mentioned purpose.
		 */
		for (Process process : processes.subList(1, processes.size())) {
			if (process.getArrivalTime() > previousProcess.getCompletedTime()) {
				// CPU is idle here
				process.setCompletedTime(process.getArrivalTime() + process.getBurstTime());
			} else {
				process.setCompletedTime(previousProcess.getCompletedTime() + process.getBurstTime());
			}
			process.setTurnaroundTime(process.getCompletedTime() - process.getArrivalTime());
			process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
			previousProcess = process;

		}
		// Uncomment the two following lines to see representations of the processes.
		// printTable();
		// printGanttChart();
	}

	public void printTable() {
		char[] tableHeading = "PID\tAT\tBT\tCT\tTAT\tWT".toCharArray();
		String line = "";
		for (int i = 0; i < tableHeading.length; i++) {
			if (tableHeading[i] == '\t') {
				line += "------";
			} else {
				line += "-";
			}
		}
		System.out.println(line);
		System.out.println(tableHeading);
		for (Process process : processes) {
			System.out.println(process.getProcessId() + "\t" + process.getArrivalTime() + "\t" + process.getBurstTime()
					+ "\t" + process.getCompletedTime() + "\t" + process.getTurnaroundTime() + "\t"
					+ process.getWaitingTime());
		}
		System.out.println(line);

	}

	public void printGanttChart() {
		System.out.println();
		System.out.println("%%%%%%%%%%%% GANTT CHART %%%%%%%%%%%%");
		String str = "";
		Process previousProcess = processes.get(0);
		str += "|";

		if (previousProcess.getArrivalTime() > 0) {
			for (int i = 0; i < previousProcess.getArrivalTime(); i++) {
				str += "¤";
			}
			str += "|";
			str += "|";

		}

		for (int i = 0; i < previousProcess.getBurstTime(); i++) {
			if (i == previousProcess.getBurstTime() / 2) {

				str += "p" + previousProcess.getProcessId();
			} else {

				str += " ";
			}
		}

		str += "|";

		for (Process process : processes.subList(1, processes.size())) {
			str += "|";
			if (process.getArrivalTime() > previousProcess.getCompletedTime()) {
				for (int i = previousProcess.getCompletedTime(); i < process.getArrivalTime(); i++) {
					str += "¤";
				}
				str += "|";
				str += "|";
			}
			for (int i = 0; i < process.getBurstTime(); i++) {
				if (i == process.getBurstTime() / 2) {
					str += "p" + process.getProcessId();
				} else {

					str += " ";
				}
			}

			str += "|";
			previousProcess = process;
		}

		System.out.println();
		for (int i = 0; i < str.length(); i++) {
			System.out.print("=");
		}
		System.out.println();
		System.out.println(str);
		for (int i = 0; i < str.length(); i++) {
			System.out.print("=");
		}
		System.out.println();
		printGanttTimestamps(str);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

	/**
	 * Help function that prints the timeline of the gantt chart.
	 * 
	 * @param str - The string containing the gantt-chart
	 */
	private void printGanttTimestamps(String str) {
		int processAt = 0;
		Process previousProcess = processes.get(processAt);

		if (previousProcess.getArrivalTime() > 0) {
			System.out.print("0");
			for (int i = 0; i < previousProcess.getArrivalTime(); i++) {
				System.out.print(" ");
			}
			if (previousProcess.getArrivalTime() > 9) {
				System.out.print(previousProcess.getArrivalTime());
			} else {

				System.out.print(previousProcess.getArrivalTime() + " ");
			}
		} else {
			System.out.print(previousProcess.getArrivalTime());
		}
		for (Process process : processes) {
			if (process.getArrivalTime() > previousProcess.getCompletedTime()) {
				for (int i = previousProcess.getCompletedTime(); i < process.getArrivalTime(); i++) {
					System.out.print(" ");
				}
				if (process.getArrivalTime() > 10) {
					System.out.print(process.getArrivalTime());
				} else {
					System.out.print(process.getArrivalTime() + " ");
				}
			}
			for (int i = 0; i < process.getBurstTime(); i++) {
				System.out.print(" ");
			}
			System.out.print(" ");
			if (process.getCompletedTime() > 9) {
				System.out.print(process.getCompletedTime());
			} else {
				System.out.print(process.getCompletedTime() + " ");
			}

			previousProcess = process;
		}
	}
}
