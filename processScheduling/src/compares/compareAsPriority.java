package compares;

import pcb.PCB;

import java.util.Comparator;

public class compareAsPriority implements Comparator<PCB> {
    public int compare(PCB pcb1,PCB pcb2) {
        return pcb2.priority - pcb1.priority;
    }
}
