package pcb;

public class PCB {
    public String processCharacter;        //进程标识符
    public int priority;                   //优先级
    public int arriveTime;                 //进入时间
    public int serviceTime;                //服务时间
    public int beginTime;                  //开始时间
    public int finishTime;                 //结束时间
    public int roundTime;                  //周转时间
    public double aveRoundTime;            //带权周转时间
    public double clock = 0;               //时间片已使用的时长
    public int outQueueTime;               //时间片的出队次数

    public PCB(String processCharacter, int arriveTime, int serviceTime, int priority) {
        super( );
        this.processCharacter = processCharacter;
        this.arriveTime = arriveTime;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }
}