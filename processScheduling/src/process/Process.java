package process;

import pcb.PCB;
import compares.compareAsServiceTime;
import queue.processQueue;
import compares.compareAsArriveTime;
import compares.compareAsPriority;
import java.util.Collections;
import java.util.Scanner;

public class Process {

    static processQueue queue = new processQueue();
    int size;

    public void init()
    {
        int processNumber;
        System.out.print("Please enter number of process:");
        Scanner scanner = new Scanner(System.in);
        processNumber = scanner.nextInt();
        PCB[] pcb = new PCB[processNumber];
        for(int i=0;i<processNumber;i++)
        {
            System.out.println("Please enter "+"No."+(i+1)+" process' processCharacter,arriveTime,serviceTime and priorityTime:");
            System.out.print("Process Character:");
            String processCharacterTemp = scanner.next();
            System.out.print("Arrive Time:");
            int arriveTimeTemp = scanner.nextInt();
            System.out.print("Service Time:");
            int serviceTimeTemp = scanner.nextInt();
            System.out.print("Priority:");
            int priorityTemp = scanner.nextInt();
            pcb[i] = new PCB(processCharacterTemp,arriveTimeTemp,serviceTimeTemp,priorityTemp);
            queue.storage.add(pcb[i]);
        }
        Collections.sort(queue.storage,new compareAsArriveTime());
    }

    public void SelectScheduling()
    {
        init();
        System.out.println("Select Process Scheduling Algorithm");
        System.out.println("-------------------------");
        System.out.println("A : FirstComeFirstService");
        System.out.println("B : ShouterJobFirst");
        System.out.println("C : RoundRubin");
        System.out.println("D : HighPriority");
        System.out.println("-------------------------");
        System.out.print("Please input option:");
        Scanner in = new Scanner(System.in);
        final String s = in.next();
        switch (s){
            case "A":
                FirstComeFirstService();
                queue.processPrint();
                break;
            case "B":
                ShouterJobFirst();
                queue.processPrint();
                break;
            case "C":
                System.out.print("Please enter time slice size:");
                size = in.nextInt();
                RoundRubin();
                queue.processPrint();
                break;
            case "D":
                HighPriority();
                queue.processPrint();
                break;
        }
    }

    public void FirstComeFirstService()
    {
        queue.enterQueue();
        while (!queue.memory.isEmpty())
        {
            queue.outQueue();
            queue.enterQueue();
        }
    }

    public void ShouterJobFirst()
    {
        queue.enterQueue();
        while (!queue.memory.isEmpty())
        {
            queue.outQueue();
            queue.enterQueue();
            Collections.sort(queue.memory,new compareAsServiceTime());
        }
    }

    public void RoundRubin()
    {
        queue.enterQueue();
        while(!queue.memory.isEmpty())
        {
            queue.outQueueRoundRubin(size);
        }
    }

    public void HighPriority()
    {
        queue.enterQueue();
        while(!queue.memory.isEmpty())
        {
            queue.outQueue();
            queue.enterQueue();
            Collections.sort(queue.memory,new compareAsPriority());
        }
    }

    public static void main(String[] args)
    {
        Process process = new Process();
        process.SelectScheduling();
    }
}