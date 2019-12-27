package compares;

import pcb.PCB;

import java.util.Comparator;

public class compareAsServiceTime implements Comparator<PCB> {
    public int compare(PCB pcb1,PCB pcb2) {
        return pcb1.serviceTime - pcb2.serviceTime;
    }
}