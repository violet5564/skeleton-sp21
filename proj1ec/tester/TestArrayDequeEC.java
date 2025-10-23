package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

import javax.swing.plaf.synth.SynthTextAreaUI;

public class TestArrayDequeEC {
    @Test
    public void sampleTest(){
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();

        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(i);
                sad2.addLast(i);

            } else {
                sad1.addFirst(i);
                sad2.addFirst(i);
            }
        }
        for(int i = 0; i < 100; i++){
            assertEquals(sad1.removeFirst(),sad2.removeFirst());
        }

        sad1.printDeque();
    }

    @Test
    public void randomCall(){
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();
        for(int i = 0; i < 10000 ; i++){
            double numberFromZeroToThree = StdRandom.uniform(0,4);
            if(numberFromZeroToThree == 0){
                sad1.addFirst(i);
                sad2.addFirst(i);
            } else if (numberFromZeroToThree == 1) {
                sad1.addLast(i);
                sad2.addLast(i);
            } else if (numberFromZeroToThree == 2) {
                if (!sad1.isEmpty() && !sad2.isEmpty()){
                    Integer actual = sad1.removeFirst();
                    Integer expected = sad2.removeFirst();
                    assertEquals("Actual number" +actual+"not equal to"+expected,expected,actual);
//                    System.out.println(sad2.removeFirst()+" "+sad1.removeFirst());
                }
            }else{
                if (!sad1.isEmpty() && !sad2.isEmpty()){
                    Integer actual = sad1.removeLast();
                    Integer expected = sad2.removeLast();
                    assertEquals("Actual number" +actual+"not equal to"+expected,expected,actual);
//                    System.out.println(sad2.removeLast()+" "+sad1.removeLast());
                }
            }


        }
    }
}
