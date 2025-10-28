package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    // 默认比较器
    private Comparator<T> defaultCompatator;

    //creates a MaxArrayDeque with the given Comparator.
    public MaxArrayDeque(Comparator<T> c) {
        defaultCompatator = c;
    }



    // returns the maximum element in the deque as governed by the
    // previously given Comparator. If the MaxArrayDeque is empty, simply return null.
    public T max() {
        return max(this.defaultCompatator);
    }
    //  returns the maximum element in the deque as governed by the parameter
    //  Comparator c. If the MaxArrayDeque is empty, simply return null.
    public T max(Comparator<T> c) {
        if (size() < 1) {
            return null;
        }
        T maxItem = get(0);
        for (int i = 1; i < size(); i++) {
            T otherItem = get(i);
            if (c.compare(maxItem, otherItem) < 0) {
                maxItem = otherItem;
            }
        }
        return maxItem;
    }
}
