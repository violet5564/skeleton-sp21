package gitlet;

import jdk.dynalink.beans.StaticClass;

import java.io.File;

import static gitlet.Utils.join;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Shabriri
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     *  java gitlet.Main add hello.txt
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        // If args is empty, print a message and exit.
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        // 初始化someObj对象，用于一些操作
        SomeObj someObj = new SomeObj();
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                someObj.init();

            case "commit":
                someObj.commit();


                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN

            default:
                System.out.println("No command with that name exists.");
        }
    }
}
