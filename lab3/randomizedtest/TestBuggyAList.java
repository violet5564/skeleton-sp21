package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import javax.swing.plaf.synth.SynthTextAreaUI;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */

public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove(){
        // 分别加4，5，6，然后再移除
        AListNoResizing<Integer> correctAList= new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();
        for(int i = 4; i<=6; i++){
            correctAList.addLast(i);
            buggyAList.addLast(i);
        }
        for(int i = 0; i<3; i++){
            int correctRemove = correctAList.removeLast();
            int buggyRemove = buggyAList.removeLast();
            assertEquals(correctRemove,buggyRemove);
        }
    }
    @Test
    public void randomComparisonTests(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
                System.out.println("addLast:" + randVal);
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = L2.size();
                System.out.printf("size1:%d,size2:%d\n",size1,size2);
                assertEquals(size1, size2);

            }else if(operationNumber == 2 && L.size()>0){
                // removeLast
                int remove1 = L.removeLast();
                int remove2 = L2.removeLast();
                System.out.printf("remove1:%d, remove2:%d\n", remove1,remove2);
                assertEquals(remove1, remove2);
            }else if(operationNumber == 3 && L.size() > 0){
                // getLast
                int get1 = L.getLast();
                int get2 = L2.getLast();
                System.out.printf("get1:%d,get2:%d\n",get1,get2);
                assertEquals(get1,get2);
            }
        }
    }
}
