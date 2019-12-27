package queue;

import pcb.PCB;
import java.util.ArrayList;
import java.util.LinkedList;

public class processQueue{
    int nowIndex = 0;                                               //PCB中的进程遍历时的下标
    int nowTime = 0;                                                //当前时间
    PCB nowProess;                                                  //正在运行的进程
    public ArrayList<PCB> storage = new ArrayList<PCB>();           //存放所有的进程(存储）
    public LinkedList<PCB> memory = new LinkedList<PCB>();          //存放进入队列的进程（内存）
    public ArrayList<PCB> result = new ArrayList<PCB>();            //存放结果进程

    public void enterQueue()
    {
        while (nowIndex < storage.size()) {
            if (storage.get(nowIndex).arriveTime <= nowTime)
            {
                memory.add(storage.get(nowIndex));
                nowIndex++;
            }
            else
            {
                break;
            }
        }
    }

    public void outQueue()
    {
        nowProess = memory.removeFirst();                                       //弹出队首元素
        getData();
        result.add(nowProess);                                                  //加入result结果集
    }

    public void outQueueRoundRubin(double sliceTime)                            //时间片轮转出队
    {
        nowProess = memory.removeFirst();                                       //弹出队首元素
        nowProess.outQueueTime++;
        if(nowProess.outQueueTime == 1)
        {
            nowProess.beginTime = nowTime;
        }
        nowTime += sliceTime;                                                   //每次出队，用时一个时间片，更新当前时间
        nowProess.clock+=sliceTime;                                             //更新当前出队列的进程已服务时间
        if(nowProess.clock>=nowProess.serviceTime)
        {
            nowProess.finishTime=nowTime;                                       //计算该进程完成时间
            nowProess.roundTime = nowProess.finishTime - nowProess.arriveTime;                  //计算周转时间
            nowProess.aveRoundTime = (double)nowProess.roundTime / nowProess.serviceTime;       //计算平均周转时间
            result.add(nowProess);                                              //经处理过数据后加入result容器
            enterQueue();
        }
        else {
            enterQueue();                                                       //已到达的进程先入队
            memory.addLast(nowProess);                                          //上一轮出的再紧接着进入队尾
        }
    }

    public void getData()
    {
        nowProess.beginTime = nowTime;
        nowProess.finishTime = nowProess.beginTime + nowProess.serviceTime;                     //计算结束时间，该进程开始时间+服务时间
        nowProess.roundTime = nowProess.finishTime - nowProess.arriveTime;                      //计算周转时间
        nowProess.aveRoundTime = (double) nowProess.roundTime / nowProess.serviceTime;          //计算平均周转时间
        nowTime = nowProess.finishTime;                                                         //获得结束时间，即当前时间，方便判断剩下的进程是否已到达
    }

    public void processPrint(){
        double countRoundTime = 0;
        double countAveRoundTime = 0;
        System.out.println("进程标识符   进入时间    服务时间    优先级    开始时间    完成时间    周转时间    带权周转时间");
        for(int i=0;i<result.size();++i)
        {
            System.out.println(result.get(i).processCharacter+"\t\t\t"+result.get(i).arriveTime+"\t\t\t"+result.get(i).serviceTime+"\t\t\t"+result.get(i).priority+"\t\t\t"+result.get(i).beginTime+"\t\t\t"+result.get(i).finishTime+"\t\t\t"+result.get(i).roundTime+"\t\t\t"+result.get(i).aveRoundTime);
        }
        for(int j=0;j<result.size();++j)
        {
            countRoundTime += result.get(j).roundTime;
            countAveRoundTime +=result.get(j).aveRoundTime;
        }
        System.out.println("平均周转时间  平均带权周转时间");
        System.out.println("\t"+countRoundTime/result.size()+"\t\t\t"+countAveRoundTime/result.size());
    }
}