package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  此处用于存放一些函数操作，如init以保证Main中代码的简洁。
 *  does at a high level.
 *
 *  @author Shabriri
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     * 1
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */
    /** The object directory*/
    public static final File OBJECT_DIR = join(GITLET_DIR, "objects");
    /** The refs directory */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The heads directory */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    /** The HEAD file */
    public static final File HEAD_F = join(GITLET_DIR, "HEAD");
    /** The stage file */
    public static final File stage_FILE = join(GITLET_DIR, "stage");
    /**
     Creates a new Gitlet version-control system in the current directory.
     This system will automatically start with one commit: a commit that contains
     no files and has the commit message initial commit (just like that, with no
     punctuation). It will have a single branch: master, which initially points
     to this initial commit, and master will be the current branch. The timestamp
     for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in
     whatever format you choose for dates (this is called “The (Unix) Epoch”,
     represented internally by the time 0.) Since the initial commit in all repositories
     created by Gitlet will have exactly the same content, it follows that all repositories
     will automatically share this commit (they will all have the same UID) and all commits
     in all repositories will trace back to it.
     */
    public static void init() {
        /** ======第一步搭建文件夹框架===== */
        // If there is already a Gitlet version-control system
        // in the current directory, it should abort
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        mkdir(GITLET_DIR);
        mkdir(OBJECT_DIR);
        mkdir(REFS_DIR);
        mkdir(HEADS_DIR);
        // create HEAD_F
        mkfile(HEAD_F);
        // create stageFile
        mkfile(stage_FILE);
        /** ==第二部创建initialCommit== */
        //initialize timestamp in commit class
        Commit initalCommit = new Commit("initial commit", new ArrayList<>(), new HashMap<>());
        String initCommitID = initalCommit.generateID();
        // 将initial commit写入objects文件夹
        initalCommit.saveCommit();

        /** ==第三步维护master,HEAD,Stage== */
        // 初始化master，文件名为master
        File masterFile = join(HEADS_DIR, "master");
        writeContents(masterFile, initCommitID);
        // 初始化Head，由于gitlet没有detached状态，因此
        // HEAD的内容由"ref: refs/heads/master\n"变为“master”
        writeContents(HEAD_F, "master");
        //初始化 Stage
        Stage initialStage = new Stage();
        initialStage.saveStage();
    }

    /**
     * 辅助函数：用于创建文件
     * @param f 文件目录
     */
    private static void mkdir(File f) {
        if(!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * 辅助函数：用于创建文件夹
     * @param f 文件夹目录
     */
    private static void mkfile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     Saves a snapshot of tracked files in the current commit and staging area so
     they can be restored at a later time, creating a new commit. The commit is said
     to be tracking the saved files. By default, each commit’s snapshot of files will
     be exactly the same as its parent commit’s snapshot of files; it will keep versions
     of files exactly as they are, and not update them. A commit will only update the
     contents of files it is tracking that have been staged for addition at the time of
     commit, in which case the commit will now include the version of the file that was
     staged instead of the version it got from its parent. A commit will save and start
     tracking any files that were staged for addition but weren’t tracked by its parent.
     Finally, files tracked in the current commit may be untracked in the new commit as
     a result being staged for removal by the rm command (below).
     */
    // 注意serialize 不能用指针，而要用hashcode
    public static void makeCommit(String message) {
        // Read form my computer the head commit object and the staging area


        // Clone the HEAD commit
        // modify its message and timestamp according to user input
        // Use the staging area in order to modify the files tracked by the new commit

        // Write back any new object made or any modified objects read earlier

        // After we done with commit needs to do, we need to ask did we make some new objects
        // that need to be saved.or did we read some objects and modified them.
    }


    /**
     * 将指定文件添加到staging area中
     */
    public static void add(String fileName) {
        // 1.读取文件
        File addFile = new File(fileName);
        if(!addFile.exists()) {
            System.out.println("File does not exit");
            return;
        }
        // 2.计算文件的SHA-1
        String currentContent = readContentsAsString(addFile);
        String currBlobHash = Utils.sha1(currentContent);

        // 3.读取Stage对象和stage的addHashMap
        Stage stage = Stage.readStage();
        HashMap<String, String> addHashMap = stage.getAddFile();
        // 4.获取当前commit的hashcode
        Commit commit = getHeadCommit();
        String commitBlobHash = commit.getFileHash(fileName);
        // 5. 如果add的文件和commit中的文件相同,将其从Staging area移除
        if(commitBlobHash.equals(currBlobHash)) {
            stage.unstageAdd(fileName);
            stage.unstageRemove(fileName);
        } else {
            stage.add(fileName, currBlobHash);
            stage.unstageRemove(fileName);
            // Blob.save();
        }
        //6. 将文件添加到staging area并保存

        stage.saveStage();


    }

    /**
     * 该方法用于从HEAD一路顺藤摸瓜获取当前分支最新的Commit
     * @return 当前分支最新的commit
     */
    public static Commit getHeadCommit() {
        //1. 从head中提取对应分支的字符出
        String headContent = readContentsAsString(HEAD_F).trim(); // trim去除可能的换行符

        // 2. 提取分支文件的路径
        // 也就是把 "ref: " 去掉，拿到 "refs/heads/xxxx"
        String branchPath = headContent.substring(5);

        // 3. 找到那个分支文件 (比如 .gitlet/refs/heads/master)
        File branchFile = Utils.join(GITLET_DIR, branchPath);

        //4. 读取文件中的哈希hi
        if(!branchFile.exists()) {
            return null; //防御性检查
        }
        String hash = readContentsAsString(branchFile).trim();

        // 5.根据哈希值获取commit路径
        File commitPath = join(OBJECT_DIR, hash);
        return readObject(commitPath, Commit.class);

    }

}
