package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }


    }
    @Test
    // Return whether the Deque is empty.
    public void isEmptyTest(){

        // 这会创建一个 size=0 的 Deque
        LinkedListDeque<String> L1 = new LinkedListDeque<>();
        assertTrue(L1.isEmpty());
        // 这会创建一个 size=1，包含 12 的 Deque
        LinkedListDeque<Integer> L2 = new LinkedListDeque<>(2);
        // 在节点头部添加一个1的节点，size变为2
        L2.addFirst(1);
        L2.addLast(3);
        assertFalse(L2.isEmpty());
    }

    @Test
    // Check if you can get the right item through the index.
    public void getTest(){
        // 这会创建一个 size=1，包含 12 的 Deque
        LinkedListDeque<Integer> L = new LinkedListDeque<>(1);
        // 在节点头部添加一个1的节点，size变为2
        L.addFirst(2);
        L.addLast(3);
        L.addLast(4);
        L.addLast(5);
        int expected = 3;
        int result = L.get(2);
        assertEquals(expected,result);
    }
    @Test
    // Check if you can get the right item through the index recursively.
    public void getRecursiveTest(){
        // 这会创建一个 size=1，包含 12 的 Deque
        LinkedListDeque<Integer> L = new LinkedListDeque<>(1);
        // 在节点头部添加一个1的节点，size变为2
        L.addFirst(2);
        L.addLast(3);
        L.addLast(4);
        L.addLast(5);
        int expected = 3;
        int result = L.getRecursive(2);
        assertEquals(expected,result);

    }

    @Test
    public void equalsTest(){
        LinkedListDeque<Integer> L1 = new LinkedListDeque<>(1);
        L1.addLast(2);
        LinkedListDeque<Integer> L2 = new LinkedListDeque<>(1);
        L2.addLast(2);
        LinkedListDeque<String> L3 = new LinkedListDeque<>("1");
        L3.addLast("2");
        L1.addLast(3);
        L1.addLast(4);
        L1.addLast(5);
        L1.addLast(6);
        ArrayDeque<Integer> A1 = new ArrayDeque<>();
        A1.addLast(1);
        A1.addLast(2);
        A1.addLast(3);
        A1.addLast(4);
        A1.addLast(5);
        A1.addLast(6);
//        assertTrue("should be true",L1.equals(L2));
//        assertFalse("should be false",L1.equals(L3));
        assertTrue("should be true", A1.equals(L1));
        assertTrue("should be true", L1.equals(A1));

    }
}
