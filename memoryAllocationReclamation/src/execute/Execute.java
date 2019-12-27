package execute;

import algorihm.Algorithm;
import memory.Memory;
import operate.Allocation;
import java.util.Scanner;

public class Execute {

    public void initSelect()
    {
        System.out.print("1:分配 2：回收 | 选项:");
        Scanner scanner = new Scanner(System.in);
        int choose = scanner.nextInt();
        switch (choose){
            case 1:
                System.out.print("分配大小：");
                int initAllocationSize = scanner.nextInt();
                Algorithm.firstAdapt(initAllocationSize);
                break;
            case 2:
                Allocation.executeReclamation();break;
            default:
                System.out.println("无效选项！");break;
        }
    }

    public void select() {
        System.out.println("------------------------");
        System.out.println("分配还是回收？");
        System.out.println("1：分配");
        System.out.println("2：回收");
        System.out.println("------------------------");
        Scanner scanner = new Scanner(System.in);
        System.out.print("选项：");
        int selectOperating = scanner.nextInt();
        switch(selectOperating){
            case 1:
                this.allocationSelect();break;
            case 2:
                Allocation.showZones();
                Allocation.executeReclamation();
                break;
        }
    }

    public void allocationSelect()
    {
        System.out.println("------------------------");
        System.out.println("选择要进行的分配算法：");
        System.out.println("1：首次适应算法");
        System.out.println("2：循环首次适应算法");
        System.out.println("3：最优适应算法");
        System.out.println("4：最坏适应算法");
        System.out.println("------------------------");
        Scanner scanner = new Scanner(System.in);
        System.out.print("键入选项：");
        int selectAlgorithm = scanner.nextInt();
        System.out.print("键入要分配的大小：");
        int size = scanner.nextInt();
        switch (selectAlgorithm){
            case 1:
                Algorithm.firstAdapt(size);break;
            case 2:
                Algorithm.nextAdapt(size);break;
            case 3:
                Algorithm.bestAdapt(size);break;
            case 4:
                Algorithm.worstAdapt(size);break;
            default:
                System.out.println("无效选项！");break;
        }
        Allocation.showZones();
    }

    public static void main(String[] args) {
        int judge = 1;
        Execute execute = new Execute();
        System.out.print("键入总共内存大小：");
        Scanner scanner = new Scanner(System.in);
        int total = scanner.nextInt();
        Memory memory = new Memory(total);
        System.out.println("接下来初操作始化内存：");
        while(judge == 1)
        {
            execute.initSelect();
            System.out.print("[键入'1'继续初始化 '0'完成初始化]: ");
            judge = scanner.nextInt();
        }
        if (judge == 0)
        {
            System.out.println("内存初始化完成！");
            Allocation.showZones();
        }
        execute.select();
    }
}
