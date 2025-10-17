package deque;

import net.sf.saxon.style.LiteralResultElement;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst = 4;
    private int nextLast = 5;
    //create a empty ArrayDeque
    public ArrayDeque() {
        items = (T [])  new Object[8];
        size = 0;
    }

    //Adds an item of type T to the front of the deque.
    public void addFirst(T item){
        if(size == items.length){
            resize(size*2);
        }
        items[nextFirst] = item;
        if(nextFirst>0){
            nextFirst -= 1;
        }else{
            nextFirst = size-1;
        }
        size += 1;
    }

    public void addLast(T item) {
        if(size == items.length){
            resize(size*2);
        }
        items[nextLast] = item;
        if(nextLast < items.length-1) {
            nextLast += 1;
        }else{
            nextLast = 0;
        }
        size += 1;
    }

    // Returns true if deque is empty, false otherwise.
    public boolean isEmpty(){
        return size == 0;
    }
    // Return the size of the Deque.
    public int size() {
        return size;
    }

    // Prints the items in the deque from first to last
    // 从first遍历到null或次数达到size；
    // 如果一直没到null，中间加个折返到0的操做
    public void printDeque(){
        for(int i = 0; i < size; i++){
            if(get(i) == null){
                break;
            }
            T result = get(i);
            System.out.print(result + " ");
        }
        System.out.println();
    }

    //  Removes and returns the item at
    //  the front of the deque. If no such item exists, return null.
    public T removeFirst(){
        if(size == 0){
            return null;
        }
        T result ;
        if(nextFirst == items.length -1){
            result = items[0];
            items[0] = null;
            nextFirst = 0;
        }else{
            result =  items[nextFirst + 1];
            items[nextFirst + 1] = null;
            nextFirst = nextFirst + 1;
        }
        size -= 1;
        return result;
    }

    // Removes and returns the item at the back of the deque.
    // If no such item exists, returnn null
    public T removeLast(){
        if(size == 0){
            return null;
        }
        T result;
        // 处理特殊情况尾部在items[0]
        if(nextLast == 0){
            result = items[items.length - 1];
            items[items.length-1] = null;
            nextLast = items.length - 1;
        }else{
            result = items[nextLast - 1];
            items[nextLast - 1] = null;
            nextLast = nextLast - 1;
        }
        size -= 1;
        return result;
    }

    public T getLast() {
        if(size == 0){
            return null;
        }
        return items[nextLast-1];

    }

    public T get(int i) {
        if(i>=size || size == 0){
            return null;
        }
        int index = nextFirst + i + 1;
        if(index < items.length) {
            return items[index];
        }else{
            return items[index - items.length];
        }

    }


    private void resize(int capacity){
        T[] a =(T[])  new Object[capacity];
        System.arraycopy(items,0,a, 0,nextLast); // 头尾断开，复制左边的尾部
        int lenRight = items.length - nextLast;
        int startPoint = capacity - lenRight;
        System.arraycopy(items, nextLast, a,startPoint ,lenRight); // 复制右边的头部 len = size - nextFirst  strat_point = capacity - len
        items = a; // 更新新的arrayDeque
        // 更新nextFirst
        nextFirst = nextFirst + size;
    }


    public boolean equals(Object o){
        if(!(o instanceof ArrayDeque)){
            return false;
        }
        ArrayDeque<T> otherDeque = (ArrayDeque<T>) o;
        if(this.size != otherDeque.size){
            return false;
        }
        for(int i = 0; i < this.size; i++){
            T itemThis = this.get(i);
            T itemOther = otherDeque.get(i);
            if(!itemThis.equals(itemOther)){
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args){
        ArrayDeque<String> A1 = new ArrayDeque<>();
        A1.addLast("a");
        A1.addLast("b");
        A1.addFirst("c");
        A1.addLast("d");
        A1.addLast("e");
        A1.addFirst("f");
        A1.addLast("g");
        A1.addLast("h");
        A1.addLast("i");
        A1.addLast("j");
        A1.printDeque();
//        System.out.println(A1.removeFirst());
//        System.out.println(A1.removeFirst());
//        System.out.println(A1.removeFirst());
//        System.out.println(A1.removeFirst());
//        System.out.println(A1.removeFirst());
//        System.out.println(A1.removeFirst());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());
        System.out.println(A1.removeLast());

    }
}
