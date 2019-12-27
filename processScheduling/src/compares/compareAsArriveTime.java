package compares;

import pcb.PCB;

import java.util.Comparator;

public class compareAsArriveTime implements Comparator<PCB> {
    public int compare(PCB pcb1, PCB pcb2) {
        int a = pcb1.arriveTime - pcb2.arriveTime;
        if (a > 0)
            return 1;
        else if (a == 0) {
            return pcb1.serviceTime > pcb2.serviceTime ? 1 : -1;
        } else
            return -1;
    }
}
