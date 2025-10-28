package deque;



import java.util.Iterator;
import java.util.Objects;

public class LinkedListDeque<T> implements Deque<T>,Iterable<T> {
    private int size;
    private StuffNode sentinel;

    private static class StuffNode<T> {
        private StuffNode prev;
        private T item;
        private StuffNode next;
        StuffNode(StuffNode p, T i, StuffNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

//    // Creat normal a LLD, record it's size.
//    public  LinkedListDeque(T x){
//        size = 1;
//        sentinel = new StuffNode<>(null,null,null);
//        // 1. 一次性创建好新节点，并直接将其 prev 和 next 都设置为 sentinel。
//        StuffNode<T> newNode = new StuffNode<>(sentinel, x, sentinel);
//        // 2. 更新 sentinel 的指针，让它们都指向这个新节点。
//        sentinel.next = newNode;
//        sentinel.prev = newNode;
//    }

    // Creat a null LLD
    public LinkedListDeque() {
        size = 0;
        sentinel = new StuffNode<>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }


    //Adds an item of type T to the front of the deque.
    @Override
    public void addFirst(T item) {
        StuffNode<T> addNode = new StuffNode<>(sentinel, item, sentinel.next);
        sentinel.next.prev = addNode;
        sentinel.next = addNode;
        size += 1;

    }

    //Adds an item of type T to the back of the deque.
    @Override
    public void addLast(T item) {
        StuffNode<T> addNode = new StuffNode<>(sentinel.prev, item, sentinel);
        sentinel.prev.next  = addNode;
        sentinel.prev = addNode;
        size += 1;
    }

//    // Returns true if deque is empty, false otherwise.
//    @Override
//    public boolean isEmpty(){
//        return size == 0;
//    }

    // Return the size of the Deque.
    @Override
    public int size() {
        return size;
    }

    //Prints the items in the deque from first to last, separated by a space.
    // Once all the items have been printed, print out a new line.
    @Override
    public void printDeque() {
        StuffNode<T> p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    //Removes and returns the item at the front of the deque.
    //If no such item exists, returns null.
    @Override
    public T removeFirst() {
        if (size ==0) {
            return null;
        }
        StuffNode<T> firstNode = sentinel.next;
        T itemToReturn = firstNode.item;
        sentinel.next = firstNode.next;
        firstNode.next.prev = sentinel;
        size -= 1;
        return itemToReturn;
    }

    //Removes and returns the item at the back of the deque.
    //If no such item exists, returns null.
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        StuffNode<T> lastNode = sentinel.prev;
        T itemToReturn = lastNode.item;
        sentinel.prev = lastNode.prev;
        lastNode.prev.next = sentinel;
        size -= 1;
        return itemToReturn;
    }

    /*Gets the item at the given index, where 0 is the front,
    1 is the next item, and so forth. If no such item exists,
    returns null. Must not alter the deque! */
    @Override
    public T get(int index) {
        if(index >= size || size == 0 ) {
            return null;
        }
        StuffNode<T> p = sentinel.next;
        for (int i =0; i < index; i++) {
            p = p.next;
        }
        T itemToReturn = p.item;
        return itemToReturn;

    }

    // Same as get, bug uses recursion
    public T getRecursive(int index) {
        if (index >= size || size == 0 ) {
            return null;
        }
        StuffNode<T> p = sentinel.next;
        return helper(p, index);
    }

    /**
     * 这是一个helper函数，用于完成get的递归写法。
     * @param p 指针，始终指向当前迭代的节点位置
     * @param index 索引
     * @return helper函数对下一个节点调用的结果
     */
    private T helper(StuffNode<T> p, int index) {
        if (index == 0) {
            return p.item;
        }
        StuffNode<T> next = p.next;
        return helper(next, index - 1);
    }

    // return an iterator (lecture11)
    @Override
    public Iterator<T> iterator() {
        return new LldIterator();
    }

    private class LldIterator implements Iterator<T> {
        private StuffNode<T> P;
        public LldIterator() {
            P = sentinel.next;
        }
        public boolean hasNext() {
            if  (P == sentinel) {
                return false;
            }
            return true;
        }
        public T next() {
            T returnItem = P.item;
            P = P.next;
            return returnItem;
        }
    }
//    @Override
//    public boolean equals(Object o){
//        if(!(o instanceof  LinkedListDeque)){
//            return false;
//        }
//        LinkedListDeque<T> otherDeque =(LinkedListDeque<T>) o;
//        if(this.size() != otherDeque.size()){
//            return false;
//        }
//        int dequeSize = this.size();
//        for(int i =0; i<dequeSize; i++){
//            T itemThis = this.get(i);
//            T itemOther = otherDeque.get(i);
//            if(!itemThis.equals(itemOther)){
//                return false;
//            }
//        }
//        return true;
//    }

    @Override
    public boolean equals(Object o) {
        // 检查是不是自己
        if (this == o) {
            return true;
        }
        // 检查是不是Deque类型
        if (!(o instanceof Deque)) {
            return false;
        }
        // 将o转换成Deque类型
        Deque<?> otherDeque = (Deque<?>) o;

        if (this.size() != otherDeque.size()) {
            return false;
        }
        int dequeSize = this.size();
        for (int i = 0; i < dequeSize; i++) {
            Object itemThis = this.get(i);
            Object itemOther = otherDeque.get(i);
            if (!Objects.equals(itemThis, itemOther)) {
                return false;
            }
        }
        return true;
    }

}
