package lec2_intro2;

/**
 * Created by hug.
 */
public class DogProblemNoHelperMethods {
    public static Dog[] largerThanFourNeighbors(Dog[] dogs) {
        Dog[] returnDogs = new Dog[dogs.length];
        int cnt = 0;
        for (int i = 0; i < dogs.length; i++){
            if (isBiggestOfFour(dogs, i)) {
                returnDogs[cnt] = dogs[i];
                cnt += 1;
            }
        }
        Dog[] noNullDogs = new Dog[cnt];
        for (int i = 0; i < cnt; i+= 1){
            noNullDogs[i] = returnDogs[i];
        }
        return noNullDogs;
    }

    public static boolean isBiggestOfFour(Dog[] dogs, int i){
        boolean isBiggest = true;
        for (int j = -2; j <=2; j+= 1){
            int compareIndex = i + j;
            //avoid compare ourself to ourself
            if (j == 0){
                continue;
            }
            if (valuedIndex(dogs, compareIndex)){
                if (dogs[compareIndex].weightInPounds >= dogs[i].weightInPounds){
                    isBiggest = false;
                }
            }
        }

        return isBiggest;
    }
    public static boolean valuedIndex(Dog[] dogs,int i ){
        if (i < 0){
            return false;
        }
        if (i>=dogs.length){
            return false;
        }
        return true;

    }

    public static void main(String[] args) {
        Dog[] dogs = new Dog[]{
                new Dog(10),
                new Dog(15),
                new Dog(20),
                new Dog(15),
                new Dog(10),
                new Dog(5),
                new Dog(10),
                new Dog(15),
                new Dog(22),
                new Dog(15),
                new Dog(20)
        };
        Dog[] bigDogs1 = largerThanFourNeighbors(dogs);

        for (int k = 0; k < bigDogs1.length; k += 1) {
            System.out.print(bigDogs1[k].weightInPounds + " ");
        }
        System.out.println();

    }
}
