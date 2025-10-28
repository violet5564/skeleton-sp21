package deque;

import java.util.Iterator;
import java.util.Objects;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst = 4;
    private int nextLast = 5;
    private final double usageLimit = 0.25;

    //create a empty ArrayDeque
    public ArrayDeque() {
        items = (T[])  new Object[8];
        size = 0;
    }

    //Adds an item of type T to the front of the deque.
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        if (nextFirst > 0) {
            nextFirst -= 1;
        } else {
            nextFirst = items.length - 1;
        }
        size += 1;
    }
    //Adds an item of type T to the back of the deque.
    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        if (nextLast < items.length - 1) {
            nextLast += 1;
        } else {
            nextLast = 0;
        }
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

    // Prints the items in the deque from first to last
    // 从first遍历到null或次数达到size；
    // 如果一直没到null，中间加个折返到0的操做
    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            if (get(i) == null) {
                break;
            }
            T result = get(i);
            System.out.print(result + " ");
        }
        System.out.println();
    }

    //  Removes and returns the item at
    //  the front of the deque. If no such item exists, return null.
    // remove之后利用率低于25%要resize压缩空间（100一下不用压缩）
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T result;
        if (nextFirst == items.length - 1) {
            result = items[0];
            items[0] = null;
            nextFirst = 0;
        } else {
            result =  items[nextFirst + 1];
            items[nextFirst + 1] = null;
            nextFirst = nextFirst + 1;
        }
        size -= 1;
        // 检测并进行压缩操作
        if (items.length > 16 && ((double) size / items.length) < usageLimit) {
            resize(items.length / 2);
        }
        return result;
    }

    // Removes and returns the item at the back of the deque.
    // If no such item exists, returnn null
    // 考虑空间利用率；当items.length =200, size<50的时候，remove之后要调用resizing
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T result;
        // 处理特殊情况尾部在items[0]
        if (nextLast == 0) {
            result = items[items.length - 1];
            items[items.length - 1] = null;
            nextLast = items.length - 1;
        } else {
            result = items[nextLast - 1];
            items[nextLast - 1] = null;
            nextLast = nextLast - 1;
        }
        size -= 1;
        if (items.length > 16 && ((double) size / items.length) < usageLimit) {
            resize(items.length / 2);
        }
        return result;
    }

//    public T getLast() {
//        if(size == 0){
//            return null;
//        }
//        return items[nextLast-1];
//
//    }
    @Override
    public T get(int i) {
        if (i >= size || size == 0) {
            return null;
        }
        int index = nextFirst + i + 1;
        if (index < items.length) {
            return items[index];
        } else {
            return items[index - items.length];
        }

    }

    /**
     * 这是一个helper函数，用于处理当add时候size不足的问题，将
     * 数据结构的size扩大到capacity大小
     * @param capacity 扩容目标大小
     */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        // 先计算头部的部分
        int firstPartStart = (nextFirst + 1) % items.length;
        int firstPartLen = items.length - firstPartStart;
        // 如果size<第一部分长度，说明没有环绕，右边长度就是size大小
        if (size < firstPartLen) {
            firstPartLen = size;
        }
        //复制第一部分
        System.arraycopy(items, firstPartStart, a, 0, firstPartLen);

        //在计算尾部
        int secondPartLen = size - firstPartLen;
        // 复制第二部分
        if (secondPartLen > 0) {
            System.arraycopy(items, 0, a, firstPartLen, secondPartLen);
        }

        // 更新数组
        items = a;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    @Override
    public Iterator<T> iterator() {
        return new AdIterator();
    }

    private class AdIterator implements Iterator<T> {
        private int index;
        AdIterator() {
            index = 0;
        }
        public boolean hasNext() {
            if (index < size) {
                return true;
            }
            return false;
        }
        public T next() {
            T returnItem = get(index);
            index += 1;
            return returnItem;
        }
    }

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
