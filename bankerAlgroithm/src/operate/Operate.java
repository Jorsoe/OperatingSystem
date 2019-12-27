package operate;

import banker.Banker;

import java.util.Scanner;

public class Operate {

    Banker banker = new Banker();

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

    public void selectRequestResource()
    {
        int state = 1;
        while(state == 1)
        {
            System.out.println("{[1]:资源请求}|{[0]:退出} <> 请选择:");
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

    public void init()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-----------------------------------------------------");
        System.out.print("请输入进程数:");
        banker.setProcess(scanner.nextInt());
        System.out.print("请输入资源种类数:");
        banker.setSource(scanner.nextInt());

        int []maxSource = new int[banker.getSource()];
        System.out.println("请依次输入各个进程资源总数:");
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
                System.out.print("请输入进程P["+j+"]的第["+(k+1)+"]类资源数目:");
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
                System.out.print("请输入进程P["+m+"]的第["+(n+1)+"]类已分配资源数目:");
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

        int zeroJudge = 0;
        Boolean finish[] = new Boolean[banker.getProcess()];
        for(int e=0;e<banker.getProcess();e++)
        {
            finish[e] = false;
        }
        for(int f=0;f<banker.getProcess();f++)
        {
            for(int g=0;g<banker.getSource();g++)
            {
                zeroJudge += need[f][g];
            }
            if(zeroJudge == 0)
            {
                finish[f] = true;
                for(int h=0;h<banker.getSource();h++)
                {
                    available[h] += allocation[f][h];
                    allocation[f][h] = 0;
                }
                System.out.println("进程P["+f+"]的Need三种资源都为0，予以回收！");
            }
        }
        banker.setFinish(finish);
        banker.setAllocation(allocation);
        System.out.println("各个资源已初始化完成！");
        System.out.println("-----------------------------------------------------");
        this.display(banker);
    }

    public Boolean securityCheck(Banker banker)
    {
        int index = 0;
        int []work = banker.getAviailable().clone();
        int [][]need = banker.getNeed();
        int [][]allocation = banker.getAllocation();
        int []securitySequence = new int[banker.getProcess()];
        Boolean []finish = banker.getFinish().clone();
        Boolean judge = false;
        if (!judge)
        {
            while(!judge)
            {
                Boolean tag = false;
                for(int i=0;i<banker.getProcess();i++)
                {
                    if (!finish[i] && this.canAllocation(need[i],work))
                    {
                        finish[i] = true;
                        for (int j=0;j<banker.getSource();j++)
                        {
                            work[j] += allocation[i][j];
                        }
                        securitySequence[index] = i;
                        index++;
                        tag = true;
                        break;
                    }
                }
                judge = true;
                for (int i=0;i<banker.getProcess();i++)
                {
                    judge = judge && finish[i];
                }
                if (tag.equals(false))
                {
                    break;
                }
            }
        }
        if(judge)
        {
            System.out.println("系统状态安全");
            System.out.print("安全序列为: {");
            for (int m=0;m<banker.getProcess();m++)
            {
                System.out.print("P["+securitySequence[m]+"]");
                if (m<(banker.getProcess()-1))
                {
                    System.out.print(" ");
                }
            }
            System.out.println("}");
            return true;
        }
        else
        {
            System.out.println("系统状态不安全！");
            return false;
        }
    }

    public void requestResource()
    {
        int [][]allocation = banker.getAllocation().clone();
        int [][]need = banker.getNeed().clone();
        int []available = banker.getAviailable().clone();
        Boolean []finish = banker.getFinish().clone();
        int []request = new int[banker.getSource()];
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要空间申请的进程号:");
        int processNumber = scanner.nextInt();
        for (int i=0;i<banker.getSource();i++)
        {
            System.out.print("请输入request申请的第"+(i+1)+"类资源：");
            request[i] = scanner.nextInt();
        }
        if (!finish[processNumber] && compare(request,available) && compare(request,need[processNumber]))
        {
            for (int j=0;j<banker.getSource();j++)
            {
                available[j] -= request[j];
                need[processNumber][j] -= request[j];
                allocation[processNumber][j] += request[j];
            }
            banker.setAviailable(available);
            banker.setNeed(need);
            banker.setAllocation(allocation);
            System.out.println("试分配之后进行安全性检测：");
            if(this.securityCheck(banker))
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
                System.out.println(")允许！");
                int zeroJudge = 0;
                for (int k=0;k<banker.getSource();k++)
                {
                    zeroJudge += need[processNumber][k];
                }
                if (zeroJudge == 0)
                {
                    System.out.println("资源请求被允许后进程状态列表：");
                    this.display(banker);
                    System.out.println("Request["+processNumber+"]请求满足之后，进程P["+processNumber+"]的Need["+processNumber+"]数组"+banker.getSource()+"种资源都为0，予以回收！");
                    for (int l=0;l<banker.getSource();l++)
                    {
                        available[l] += allocation[processNumber][l];
                        allocation[processNumber][l] = 0;
                    }
                    finish[processNumber] = true;
                    banker.setFinish(finish);
                }
            }
            else
            {
                for (int a=0;a<banker.getSource();a++)
                {
                    available[a] += request[a];
                    need[processNumber][a] += request[a];
                    allocation[processNumber][a] -= request[a];
                }
                banker.setAviailable(available);
                banker.setNeed(need);
                banker.setAllocation(allocation);
                System.out.println("P["+processNumber+"]进程资源请求Request["+processNumber+"]不允许！");
            }
        }
        else
        {
            System.out.println("Request["+processNumber+"]不符合分配条件，不允许！");
        }
        this.display(banker);
    }

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
            System.out.print("\t\t");
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
