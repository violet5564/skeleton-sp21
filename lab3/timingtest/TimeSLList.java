package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> ns = new AList<>();
        AList<Integer> ops = new AList<>();
        AList<Double> times = new AList<>();
        for(int i = 1000; i <= 128000; i*=2){
            // 1.构建一个SLList
            SLList<Integer> test = new SLList<>();
            ns.addLast(i);
            //2.Add N items to the SLList.
            for(int j = 0; j <i; j+=1){
                test.addLast(1);
            }
            // 3.启动计时器
            Stopwatch sw = new Stopwatch();
            // 4. 执行M次getlast操作
            for(int k = 0; k <10000; k++){
                test.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            ops.addLast(10000);
        }
        printTimingTable(ns,times,ops);
    }

}
