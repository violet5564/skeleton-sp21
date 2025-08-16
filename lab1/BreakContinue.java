public class BreakContinue {
    public static void windowPosSum(int[] a, int n) {
        /** your code here */
        for(int i = 0; i < a.length; i += 1){
            if(a[i] >0){
                int sum = a[i]; //sum用于记录用 = a[i] +... +a[i+n]
                for(int j = i+1; j <= i+n ; j+=1){
                    if(j < a.length){
                        sum += a[j];
                    }
                    else{
                        break;
                    }
                }
                a[i] = sum;
            }

        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        // 记录开始时间（纳秒）
        long startTime = System.nanoTime();

        windowPosSum(a, n);

        // 记录结束时间
        long endTime = System.nanoTime();

        // 计算耗时
        long duration = endTime - startTime;

        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
        System.out.println("windowPosSum 执行时间：" + duration);
    }
}