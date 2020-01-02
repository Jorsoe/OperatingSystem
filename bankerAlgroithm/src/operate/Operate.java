package operate;

import banker.Banker;
import java.util.ArrayList;
import java.util.Scanner;

public class Operate {

    Banker banker = new Banker();
    /**
     * 进行功能性选择
     */
    public void select()
    {
        System.out.println("请先进行[进程数][资源种类数][各类资源总数],T0时刻各进程的[最大需求数][已分配数]的初始化:");
        this.init();
        int select = 1;
        while(select > 0)
        {
            System.out.println("---------------------------");
            System.out.println("选择要进行的操作:");
            System.out.println("1:安全性检测");
            System.out.println("2:进程动态请求资源的银行家算法检查");
            System.out.println("0:退出");
            System.out.println("---------------------------");
            Scanner scanner = new Scanner(System.in);
            System.out.print("请选择操作:");
            select = scanner.nextInt();
            switch (select){
                case 0:
                    break;
                case 1:
                    this.securityCheck(banker);break;
                case 2:
                    this.selectRequestResource();break;
            }
        }
        if (select == 0)
            System.out.println("已退出！");
    }
    /**
     * 进行请求资源时的功能请求
     */
    public void selectRequestResource()
    {
        int state = 1;
        while(state == 1)
        {
            System.out.print("{[1]:资源请求}|{[0]:退出} <> 请选择:");
            Scanner scanner = new Scanner(System.in);
            state = scanner.nextInt();
            switch (state){
                case 0:
                    break;
                case 1:
                    this.requestResource();
            }
        }
    }
    /**
     * 初始化对象中所有属性的函数
     * 用户输入process(进程数),source(资源数),maxSource(资源总数),maxNeed(最大需求矩阵),allocation(已分配矩阵)
     * 通过用户输入的数字计算得出need(需求矩阵),available(可用资源数组)，work(工作向量数组)并初始化finish(状态数组)
     */
    public void init()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-------------------------初始化----------------------------");
        System.out.print("请输入进程数:");
        banker.setProcess(scanner.nextInt());
        System.out.print("请输入资源种类数:");
        banker.setSource(scanner.nextInt());

        int []maxSource = new int[banker.getSource()];
        System.out.println("请依次输入各个资源总数:");
        for(int i=0;i<maxSource.length;i++)
        {
            System.out.print("第"+(i+1)+"资源总数为:");
            maxSource[i] = scanner.nextInt();
        }
        banker.setMaxSource(maxSource);

        int maxNeed[][] = new int[banker.getProcess()][banker.getSource()];
        System.out.println("请依次输入最大需求总数矩阵:");
        for(int j=0;j<banker.getProcess();j++)
        {
            for(int k=0;k<banker.getSource();k++)
            {
                //System.out.print("请输入进程P["+j+"]的第["+(k+1)+"]类资源数目:");
                maxNeed[j][k] = scanner.nextInt();
            }
        }
        banker.setMaxNeed(maxNeed);

        int allocation[][] = new int[banker.getProcess()][banker.getSource()];
        System.out.println("请依次输入已分配资源矩阵:");
        for(int m=0;m<banker.getProcess();m++)
        {
            for(int n=0;n<banker.getSource();n++)
            {
                //System.out.print("请输入进程P["+m+"]的第["+(n+1)+"]类已分配资源数目:");
                allocation[m][n] = scanner.nextInt();
            }
        }

        int need[][] = new int[banker.getProcess()][banker.getSource()];
        for(int a=0;a<banker.getProcess();a++)
        {
            for(int b=0;b<banker.getSource();b++)
            {
                need[a][b] = maxNeed[a][b] - allocation[a][b];
            }
        }
        banker.setNeed(need);

        int []available = banker.getMaxSource();
        for(int c=0;c<banker.getProcess();c++)
        {
            for(int d=0;d<banker.getSource();d++)
            {
                available[d] = available[d] - allocation[c][d];
            }
        }
        banker.setAviailable(available);
        banker.setWork(banker.getAviailable().clone());

        int zeroJudge = 0;                                      //该变量对某个进程进行资源都为0的判断
        Boolean finish[] = new Boolean[banker.getProcess()];
        for(int e=0;e<banker.getProcess();e++)
        {
            finish[e] = false;
        }
        for(int f=0;f<banker.getProcess();f++)
        {
            for(int g=0;g<banker.getSource();g++)
            {
                zeroJudge += need[f][g];                        //将第f进程的所有资源都累加
            }
            if(zeroJudge == 0)                                  //如果等于0，进入操作
            {
                finish[f] = true;                               //改变f进程的状态
                for(int h=0;h<banker.getSource();h++)
                {
                    available[h] += allocation[f][h];           //回收该进程占用的资源
                    allocation[f][h] = 0;
                }
                System.out.println("---------------------------------------");
                System.out.println("|进程P["+f+"]的Need三种资源都为0，予以回收！|");
                System.out.println("---------------------------------------");
            }
            zeroJudge = 0;                                      //进入下一进程资源总数判断之前，先将该变量恢复初值
        }
        banker.setFinish(finish);
        banker.setAllocation(allocation);
        System.out.println("各个资源已初始化完成！");
        System.out.println("--------------------------初始化---------------------------");
        this.display(banker);
    }
    /**
     * 安全性检测
     * 该方法内对象的所有变量不可被改变
     * @param banker
     */
    public Boolean securityCheck(Banker banker)
    {
        int []work = banker.getAviailable().clone();            //拿到work[]的复制数组，该数组将在本方法内进行操作
        int [][]need = banker.getNeed();                        //need[]数组将被用来比较
        int [][]allocation = banker.getAllocation();            //allocation[]数组将会被用来对work[]进行变更
        ArrayList securitySequence = new ArrayList<Integer>();  //动态数组securitySequence存放安全序列
        Boolean []finish = banker.getFinish().clone();          //finish[]数组存放各个进程的状态
        Boolean judge = false;                                  //judge用来存放安全性检测时finish[]相与的结果，也是循环的判断点
        System.out.println("[找寻安全序列的过程如下：]");
        System.out.println("-------------------------------------------------------------------------------------");
        for (int i=0;i<banker.getProcess();i++)
        {
            if (finish[i].equals(true))
            {
                securitySequence.add(i);
                System.out.println("找到P["+i+"]进程，该进程已被回收，加入安全序列！");
            }
        }
        if (!judge)                                             //如果judge的值为false则进入判断体
        {
            while(!judge)                                       //judge的更改将会在每次遍历完所有进程之后
            {
                Boolean tag = false;                            //tag的值用来控制跳出大循环
                for(int i=0;i<banker.getProcess();i++)
                {
                    if (!finish[i] && this.canAllocation(need[i],work))     //当该进程状态为false并且work[]大于need[][]
                    {                                           //以下用以输出寻找过程
                        System.out.print("找到P["+i+"]，Finish["+i+"]="+finish[i]+",work[]");
                        displsyList(work);
                        System.out.print(">need["+i+"][]");
                        displsyList(need[i]);
                        System.out.print(",work[]=");
                        displsyList(work);
                        System.out.print("+");
                        displsyList(allocation[i]);
                        System.out.print("=");                  //以上用以输出寻找过程
                        finish[i] = true;                       //若满足条件，改变进程状态
                        for (int j=0;j<banker.getSource();j++)
                        {
                            work[j] += allocation[i][j];        //将此进程已分配的资源
                        }
                        displsyList(work);
                        System.out.println();
                        securitySequence.add(i);                //将找到的满足条件并且满足条件的进程放进安全序列
                        tag = true;                             //如果可以找到满足条件的进程，改变tag为true
                        break;                                  //找到不再进行寻找，立刻跳出循环
                    }
                }
                judge = true;                                   //如果遍历之后未发现符合要求的进程，将其改为true
                for (int i=0;i<banker.getProcess();i++)
                {
                    judge = judge && finish[i];                 //将finish[]相与(&&)的结果交给judge
                }
                if (tag.equals(false))
                {
                    break;                                      //如果tag的值未变动，则说明没有找到符合条件的进程，跳出大循环
                }
            }
            System.out.println("-------------------------------------------------------------------------------------");
        }
        if(judge)                                               //如果judge的值为true,说明所有的进程都被遍历，状态安全
        {
            System.out.println("系统状态安全！");
            System.out.print("安全序列为: {");
            for (int m=0;m<securitySequence.size();m++)
            {                                                   //输出安全序列
                System.out.print("P["+ securitySequence.get(m) +"]");
                if (m<(securitySequence.size()-1))
                {
                    System.out.print(" ");
                }
            }
            System.out.println("}");
            return true;                                        //状态安全，返回值为true
        }
        else
        {
            System.out.println("系统状态不安全！");
            return false;                                       //状态不安全，返回值为false
        }
    }
    /**
     * 进程动态请求资源的银行家算法检查
     */
    public void requestResource()
    {
        int [][]allocation = banker.getAllocation().clone();    //以下为逐步拿到各个数组
        int [][]need = banker.getNeed().clone();
        int []available = banker.getAviailable().clone();
        Boolean []finish = banker.getFinish().clone();          //以上为逐步拿到各个数组
        int []request = new int[banker.getSource()];            //请求进程的资源数组
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要空间申请的进程号:");
        int processNumber = scanner.nextInt();
        for (int i=0;i<banker.getSource();i++)
        {
            System.out.print("请输入request申请的第"+(i+1)+"类资源：");
            request[i] = scanner.nextInt();                     //初始化请求进程的资源数组
        }
        if (!finish[processNumber] && compare(request,available) && compare(request,need[processNumber]))
        {                                                       //满足请求进程状态为false，request[]<(available/need)进行操作
            for (int j=0;j<banker.getSource();j++)
            {
                available[j] -= request[j];                     //从available[]中减掉request[]请求资源
                need[processNumber][j] -= request[j];           //从need[][]中减掉request[]请求资源
                allocation[processNumber][j] += request[j];     //给allocation[][]加上request[]请求资源
            }
            int zeroJudge = 0;                                  //变量zeroJudge用来判断请求之后need是否变为0
            for (int k=0;k<banker.getSource();k++)
            {
                zeroJudge += need[processNumber][k];            //将该进程的need[][]累加至zeroJudge
            }
            if (zeroJudge == 0)                                 //如果该进程需求为0，则回收该进程
            {
                finish[processNumber] = true;                   //改动该进程状态
                System.out.println("资源请求被允许后进程状态列表：");
                System.out.println("--------------------------------------------------------------------");
                System.out.println("|Request["+processNumber+"]请求满足之后，进程P["+processNumber+"]的Need["+processNumber+"]数组"+banker.getSource()+"种资源都为0，予以回收！|");
                System.out.println("--------------------------------------------------------------------");
                for (int l=0;l<banker.getSource();l++)
                {
                    available[l] += allocation[processNumber][l];       //改动available的值，将allocation(已分配)回收
                    allocation[processNumber][l] = 0;                   //再将其改为0
                }
            }
            banker.setAviailable(available);                    //将改动之后的结果放入对象
            banker.setNeed(need);
            banker.setAllocation(allocation);
            banker.setFinish(finish);
            this.display(banker);                               //输出改动之后的表
            System.out.println("试分配之后进行安全性检测：");
            if(this.securityCheck(banker))                      //判断改变之后的的状态是否安全，如果安全进行操作
            {
                System.out.print("P["+processNumber+"]进程资源请求Request["+processNumber+"](");
                for (int k=0;k<banker.getSource();k++)
                {
                    System.out.print(request[k]);
                    if ((k<banker.getSource()-1))
                    {
                        System.out.print(",");
                    }
                }
                System.out.println(")允许！");                  //以上用来打印结果
            }
            else                                                //如果该进程需求需求不为零
            {
                for (int a=0;a<banker.getSource();a++)          //将减掉的东西加回原来的数组
                {
                    available[a] += request[a];
                    need[processNumber][a] += request[a];
                    allocation[processNumber][a] -= request[a];
                }
                banker.setAviailable(available);                //再将其放入对象
                banker.setNeed(need);
                banker.setAllocation(allocation);
                System.out.println("P["+processNumber+"]进程资源请求Request["+processNumber+"]不允许！");
            }
        }
        else                                                    //如果不满足条件，则不可分配
        {
            System.out.println("Request["+processNumber+"]不符合分配条件，不允许！");
        }
        this.display(banker);
    }
    /**
     * 比较函数
     * 如果list1中有任何一个值大于list2中的任何一个值，则返回false，反之返回true
     * @param list1
     * @param list2
     * @return
     */
    public Boolean compare(int []list1,int []list2)
    {
        for(int i=0;i<list1.length;i++)
        {
            if (list1[i] > list2[i])
            {
                return false;
            }
        }
        return true;
    }
    /**
     * 是否可分配函数
     * 倘若need[]中有任何一个值大于work，则返回false，反之则返回true
     * @param need
     * @param work
     * @return
     */
    public Boolean canAllocation(int []need,int []work)
    {
        for(int i=0;i<need.length;i++)
        {
            if (need[i] > work[i])
            {
                return false;
            }
        }
        return true;
    }
    /**
     * 数组规范输出
     * 将List数组中的值以(1,2,3,...,n)的形式输出
     * @param List
     */
    public void displsyList(int []List)
    {
        System.out.print("(");
        for (int z=0;z<banker.getSource();z++)
        {
            System.out.print(List[z]);
            if (z<(banker.getSource()-1))
            {
                System.out.print(",");
            }
        }
        System.out.print(")");
    }
    /**
     * 图标输出状态表
     * 将banker对象中的Allocation，Need和Available数组以图表的形式输出
     * @param banker
     */
    public void display(Banker banker)
    {
        Boolean tag = true;
        int [][]allocation = banker.getAllocation();
        int [][]need = banker.getNeed();
        int []available = banker.getAviailable();
        System.out.println("****************Process State List***************");
        System.out.println("Process\t\tAllocation\t\tNeed\t\tAvailable");
        for (int a=0;a<banker.getProcess();a++)
        {
            System.out.print("P["+a+"]\t\t");
            for (int b=0;b<banker.getSource();b++)
            {
                System.out.print(allocation[a][b]+" ");
            }
            System.out.print("\t\t\t");
            for (int c=0;c<banker.getSource();c++)
            {
                System.out.print(need[a][c]+" ");
            }
            System.out.print("\t\t\t");
            if (tag)
            {
                for (int d=0;d<banker.getSource();d++)
                {
                    System.out.print(available[d]+" ");
                }
                tag = false;
            }
            System.out.println(" ");
        }
        System.out.println("*************************************************");
    }

    public static void main(String[] args) {
        Operate operate = new Operate();
        operate.select();
    }
}