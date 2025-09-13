package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> ns = new AList<>(); //用于存储第一列数据
        AList<Double> times = new AList<>();// 用于存储运行时间
        for(int i = 1000; i<=128000; i *=2){
            AList<Integer> test = new AList<>();// 新建测试AList
            ns.addLast(i);
            Stopwatch sw = new Stopwatch();
            for(int j = 0; j < i; j++){
                test.addLast(1);
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        printTimingTable(ns,times,ns);
    }
}
