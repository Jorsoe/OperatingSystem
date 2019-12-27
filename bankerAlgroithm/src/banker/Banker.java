package banker;

public class Banker {

    int process;                        //进程数
    int source;                         //资源种类数
    int maxSource[];                    //各类资源总数数组
    int[][] Allocation;                 //已分配资源数组
    int maxNeed[][];                    //最大资源需求数组
    int Need[][];                       //需求数组
    int Aviailable[];                   //可利用资源数组
    int Work[];                         //工作向量
    Boolean Finish[];                   //资源分配状态数组

    public Banker() {
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int[] getMaxSource() {
        return maxSource;
    }

    public void setMaxSource(int[] maxSource) {
        this.maxSource = maxSource;
    }

    public int[][] getAllocation() {
        return Allocation;
    }

    public void setAllocation(int[][] allocation) {
        Allocation = allocation;
    }

    public int[][] getMaxNeed() {
        return maxNeed;
    }

    public void setMaxNeed(int[][] maxNeed) {
        this.maxNeed = maxNeed;
    }

    public int[][] getNeed() {
        return Need;
    }

    public void setNeed(int[][] need) {
        Need = need;
    }

    public int[] getAviailable() {
        return Aviailable;
    }

    public void setAviailable(int[] aviailable) {
        Aviailable = aviailable;
    }

    public int[] getWork() {
        return Work;
    }

    public void setWork(int[] work) {
        Work = work;
    }

    public Boolean[] getFinish() {
        return Finish;
    }

    public void setFinish(Boolean[] finish) {
        Finish = finish;
    }
}
