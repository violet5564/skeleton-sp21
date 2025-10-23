package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.List;

public class TestArrayDequeEC {
    private List<String> operationSequence = new ArrayDequeSolution<>();
//    @Test
//    public void sampleTest(){
//        operationSequence.clear();
//        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
//        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();
//
//        for (int i = 0; i < 100; i += 1) {
//            double numberBetweenZeroAndOne = StdRandom.uniform();
//
//            if (numberBetweenZeroAndOne < 0.5) {
//                sad1.addLast(i);
//                sad2.addLast(i);
//                operationSequence.add("addLast(" + i + ")");
//
//            } else {
//                sad1.addFirst(i);
//                sad2.addFirst(i);
//                operationSequence.add("addFirst(" + i + ")");
//            }
//        }
//        for(int i = 0; i < 100; i++){
//            assertEquals(sad1.removeFirst(),sad2.removeFirst());
//        }
//    }

    @Test
    public void randomCall() {
        operationSequence.clear();
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();
        for (int i = 0; i < 10000; i++) {
            double numberFromZeroToThree = StdRandom.uniform(0, 4);
            if (numberFromZeroToThree < 1) {
                sad1.addFirst(i);
                sad2.addFirst(i);
                operationSequence.add("addFirst(" + i + ")");
            } else if (numberFromZeroToThree < 2) {
                sad1.addLast(i);
                sad2.addLast(i);
                operationSequence.add("addLast(" + i + ")");
            } else if (numberFromZeroToThree < 3) {
                if (!sad1.isEmpty() && !sad2.isEmpty()) {
                    Integer actual = sad1.removeFirst();
                    Integer expected = sad2.removeFirst();
                    // 记录操作
                    operationSequence.add("removeFirst()");
                    // 构造message字符
                    String message = String.join("\n", operationSequence);
                    assertEquals(message, expected, actual);
//                    assertEquals("Actual number" +actual+"not equal to"+expected,expected,actual);
//                    System.out.println(sad2.removeFirst()+" "+sad1.removeFirst());
                }
            } else {
                if (!sad1.isEmpty() && !sad2.isEmpty()) {
                    Integer actual = sad1.removeLast();
                    Integer expected = sad2.removeLast();
                    // record operation
                    operationSequence.add("removeLast(" + i + ")");
                    // construct String
                    String message = String.join("\n", operationSequence)
                    assertEquals(message, expected, actual);
//                    assertEquals("Actual number" +actual+"not equal to"+expected,expected,actual);
//                    System.out.println(sad2.removeLast()+" "+sad1.removeLast());
                }
            }
        }
    }
}
