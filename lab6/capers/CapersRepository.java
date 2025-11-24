package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/** A repository for Capers 
 * @author Shabriri
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = join(CWD, ".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils
    //create Dogs_folder
    static final File DOG_FOLDER = join(CAPERS_FOLDER, "dogs");
    // create Story_file
    static final File STORY_FILE = join(CAPERS_FOLDER, "story");
    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        //initialize .capers
        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        // create dog
        if (!DOG_FOLDER.exists()) {
            DOG_FOLDER.mkdir();
        }
        // create Story
        if (!STORY_FILE.exists()) {
            try {
                STORY_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        // TODO
        String oldStroy = "";
        if (STORY_FILE.exists()) {
            oldStroy = readContentsAsString(STORY_FILE);
        }
        String newStory = oldStroy + text + "\n";
        writeContents(STORY_FILE, newStory);
        // prints out the current story.
        System.out.println(newStory);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        // TODO
        // 创建并写入dog对象
        Dog newDog = new Dog(name, breed, age);
        newDog.saveDog();

        // prints out the dog.
        System.out.println(newDog.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
        //1. get the dog object
        Dog d = Dog.fromFile(name);
        if (d == null) {
            System.out.println("未找到" + name);
            return;
        }
        d.haveBirthday();
        d.saveDog();
    }
}
