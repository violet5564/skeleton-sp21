package deque;
import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    public void LengthComparatorTest(){
        class LengthComparator implements Comparator<String>{
            @Override
            public int compare(String s1,String s2){
                return s1.length() - s2.length();
            }
        }
        Comparator<String> myLengthComparator = new LengthComparator();
        MaxArrayDeque<String> d1 = new MaxArrayDeque<>(myLengthComparator);
        d1.addFirst("asdfg");
        d1.addFirst("nihaoya");
        d1.addFirst("wosdjafl;");
        String actual = d1.max();
        String expected = "wosdjafl;";
        assertEquals(expected, actual);
    }

    @Test
    public void alphabeticalComparatorTest(){
        Comparator<String> alphabeticalComparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<String> d2 = new MaxArrayDeque<>(alphabeticalComparator);
        d2.addFirst("zasdfg");
        d2.addFirst("nihaoya");
        d2.addFirst("wosdjafl;");
        String actual = d2.max();
        String expected = "zasdfg";
        assertEquals(expected, actual);
    }
}
