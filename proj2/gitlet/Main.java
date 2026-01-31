package gitlet;


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
            return;
        }

        String firstArg = args[0];
        try {
            switch(firstArg) {
                case "init":
                    // TODO: handle the `init` command
                    validNumberFormat(args, 1);
                    // 初始化操作
                    Repository.init();
                    break;
                case "commit":
                    // failure case
                    validNumberFormat(args, 2);
                    Repository.makeCommit(args[1]);
                    break;
                case "add":
                    // TODO: handle the `add [filename]` command
                    // failure case
                    validNumberFormat(args, 2);
                    Repository.add(args[1]);
                    break;
                // TODO: FILL THE REST IN
                case "rm":
                    validNumberFormat(args, 2);
                    Repository.rm(args[1]);
                    break;
                case "log":
                    validNumberFormat(args, 1);
                    Repository.log();
                    break;
                case "global-log":
                    validNumberFormat(args, 1);
                    Repository.globalLog();
                    break;
                case "find":
                    validNumberFormat(args, 2);
                    Repository.find(args[1]);
                    break;
                case "status":
                    validNumberFormat(args, 1);
                    Repository.status();
                    break;
                case "checkout":
                    handleCheckout(args);
                    break;

                case "branch":
                    validNumberFormat(args, 2);
                    Repository.branch(args[1]);
                    break;
                case "rm-branch":
                    validNumberFormat(args, 2);
                    Repository.rmBranch(args[1]);
                    break;
                case "reset":
                    validNumberFormat(args, 2);
                    Repository.reset(args[1]);
                    break;
                case "merge":
                    validNumberFormat(args,2);
                    Repository.merge(args[1]);
                    break;
                default:
                    System.out.println("No command with that name exists.");
            }
        } catch (GitletException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    /**
     * 用来确保传入操作个数的正确
     * @param args 命令行参数输入
     * @param formatNumber 期望的参数个数
     */
    private static void validNumberFormat(String[] args, int formatNumber) {
        // If a user inputs a command with the wrong number or format of operands,
        // print the message Incorrect operands. and exit.
        // 如果没有进行init,则需要输出并报错
        String firstArg = args[0];
        if (!firstArg.equals("init") && !Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (args.length != formatNumber) {
            throw Utils.error("Incorrect operands.");
        }


    }

    /**
     * 辅助函数，用于处理checkout命令的输入参数检查
     * @param args 命令函参数
     */
    private static void handleCheckout(String[] args) {
        if (args.length == 2) {
            // java gitlet.Main checkout [branch name]
            Repository.checkoutBranch(args[1]);
        } else if (args.length == 3) {
//            java gitlet.Main checkout -- [file name]
            if (!args[1].equals("--")) {
                throw Utils.error("Incorrect operands.");
            }
            Repository.checkoutFile(args[2]);
        } else if (args.length == 4) {
//            java gitlet.Main checkout [commit id] -- [file name]
            if (!args[2].equals("--")) {
                throw Utils.error("Incorrect operands.");
            }
            Repository.checkoutCommitFile(args[1], args[3]);
        } else {
            throw Utils.error("Incorrect operands.");
        }
    }
}
