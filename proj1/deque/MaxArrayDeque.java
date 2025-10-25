package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    // 默认比较器
    private Comparator<T> defaultCompatator;

    //creates a MaxArrayDeque with the given Comparator.
    public MaxArrayDeque(Comparator<T> c){
        defaultCompatator = c;
    }



    // returns the maximum element in the deque as governed by the
    // previously given Comparator. If the MaxArrayDeque is empty, simply return null.
    public T max(){
        return max(this.defaultCompatator);
    }
    //  returns the maximum element in the deque as governed by the parameter
    //  Comparator c. If the MaxArrayDeque is empty, simply return null.
    public T max(Comparator<T> c){
        if(size() < 1){
            return null;
        }
        T maxItem = get(0);
        for(int i = 1; i<size();i++){
            T otherItem = get(i);
            if(c.compare(maxItem,otherItem)<0){
                maxItem = otherItem;
            }
        }
        return maxItem;
    }

    public static void main (String[] args){
        // 1. 创建一个用于比较 String 长度的比较器
        class MyLengthComparator implements Comparator<String>{
            @Override
            public int compare(String s1, String s2){
                return s1.length() - s2.length();
            }
        }
        Comparator<String> lengthComparator = new MyLengthComparator();
//        Comparator<String> lengthComparator = new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return s1.length() - s2.length();
//            }
//        };

// 2. 使用这个比较器创建 MaxArrayDeque
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(lengthComparator);

        mad.addLast("apple");      // 长度 5
        mad.addLast("banana");     // 长度 6
        mad.addLast("fig");        // 长度 3

// 3. 调用 max()，它会使用 "长度比较器"
//    输出 "banana"
        System.out.println(mad.max());


// 4. 创建另一个 "按字母顺序" 比较的比较器
        Comparator<String> alphabeticalComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        };

// 5. 调用 max(Comparator c)，传入这个新比较器
//    "fig" 在字母表中最靠后
//    输出 "fig"
        System.out.println(mad.max(alphabeticalComparator));
    }
}
