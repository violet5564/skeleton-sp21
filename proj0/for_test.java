import java.util.Arrays;
public class for_test {

    public static void main(String[] args){
        int[][] rawVals = new int[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16},
        };
//        for(int i = 0; i< rawVals.length; i++){
//            for(int j = 0; j<rawVals.length; j++){
//                System.out.println(rawVals[i][j]);
//            }
//        }
        System.out.println(Arrays.deepToString(rawVals));
        int column = 3;
        for(int i =0; i < rawVals.length; i++){
            rawVals[i][column] = 0;
        }
        System.out.println(Arrays.deepToString(rawVals));
    }
}
