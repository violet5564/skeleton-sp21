package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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
        initalCommit.saveCommit(initCommitID);

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
        // 1.failure case Every commit must have a non-blank message.
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }
        // 2.  Read form my computer the head commit object and the staging area
        Commit currCommit = getHeadCommit();
        if (currCommit == null) {
            // 处理异常情况
            // 对于 Gitlet，这意味着数据损坏或找不到对象
            System.out.println("Error: Commit not found.");
            System.exit(0); // 或者 System.exit(0);
        }
        Stage stage = Stage.readStage();
        HashMap<String, String> addFile = stage.getAddFile();
        HashSet<String> removeFile = stage.getRemoveFile();
        // failure case: 检查stage是否为空
        if (addFile.isEmpty() && removeFile.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        // 3.准备parent列表
        String parentId = getHeadCommitID();
        List<String> parents = new ArrayList<>();
        parents.add(parentId);
        // 4.准备FileMapClone the HEAD commit(克隆一份父commit的内容，再根据stage area进行修改)
        HashMap<String, String> newMap = new HashMap<>(currCommit.getFileMap());
        // 用Stage更新FileMap
        newMap.putAll(addFile);
        for (String item : removeFile) {
            newMap.remove(item);
        }
        // 5.构造新Commit
        Commit newCommit = new Commit(message, parents, newMap);
        // 6. 持久化 (Persistence)
        //    - 保存 newCommit 到文件
        //    - 更新 HEAD 指针指向这个新 Commit
        //    - 清空 Stage (创建一个新的空 Stage 并保存)
        String newCommitID = newCommit.generateID();
        newCommit.saveCommit(newCommitID);
        String branchName = readContentsAsString(HEAD_F).trim();//获取当前分支
        File branchFile = Utils.join(HEADS_DIR, branchName);
        writeContents(branchFile, newCommitID);
        stage.clear();


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
        File addFile = join(CWD, fileName);
        if(!addFile.exists()) {
            System.out.println("File does not exit");
            return;
        }
        // 2.计算文件的SHA-1
        byte[] fileContent = readContents(addFile);
        String currBlobHash = Utils.sha1(fileContent);

        //3.将文件放入Blob文件夹
        File blobFile = join(OBJECT_DIR, currBlobHash);
        if (!blobFile.exists()) {
            Blob newBlob = new Blob(currBlobHash, fileContent);
            newBlob.save();
        }

        // 4.读取Stage对象和stage的addHashMap
        Stage stage = Stage.readStage();
        // 5.获取fileName在当前Commit的Hashcode（没有则为null）
        Commit commit = getHeadCommit();
        if (commit == null) {
            //commit为null,说明getlet文件夹被破坏，终止程序
            System.exit(0);
        }
        String commitBlobHash = commit.getFileHash(fileName);

        // 6. 如果add的文件和commit中的文件相同,将其从Staging area移除
        if(commitBlobHash != null && commitBlobHash.equals(currBlobHash)) {
            stage.unstageAdd(fileName);
            stage.unstageRemove(fileName);
        } else {
            stage.add(fileName, currBlobHash);
            stage.unstageRemove(fileName);

        }
        //6. 将文件添加到staging area并保存
        stage.saveStage();
    }



    /**
     * 将指定文件从CWD和stage中删除
     * @param fileName 将被删除的文件
     */
    public static void rm(String fileName) {
        // If the file is neither staged nor tracked by the head commit,
        // print the error message No reason to remove the file.
        // 1.读取fileName文件，stage,和head commit的hashMap
        File file = join(CWD, fileName);
        Stage stage = Stage.readStage();
        HashMap<String, String> addMap = stage.getAddFile();
//        HashSet<String> rmSet = stage.getRemoveFile();
        Commit currCommit = getHeadCommit();
        if (currCommit == null) {
            System.exit(0);
        }
        HashMap<String, String> fileMap = currCommit.getFileMap();//fileMap不会是null,只会为空

        // 2.If the file is neither staged nor tracked by the head commit,
        // print the error message No reason to remove the file.
        if (!addMap.containsKey(fileName)  && !fileMap.containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        //3.如果add有，unstage它
        if (addMap.containsKey(fileName)) {
            stage.unstageAdd(fileName);
        }
        //4.如果在当前commit被tracked。stage it for removal and remove the file from CWD。
        // 注意，只有在被commit tracked的时候才能从CWD中remove
        if (fileMap.containsKey(fileName)) {
            stage.addRemove(fileName);
            if (file.exists()) {
                Utils.restrictedDelete(file);
            }
        }
        //5.序列化保存stage，
        stage.saveStage();
    }

    /**
     * 输出当前head所在分支之前的所有commit的相关信息
     */
    public static void log() {
//        ===
//        commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
//        Date: Thu Nov 9 20:00:05 2017 -0800
//        A commit message.
        // 1.获取当前commitID and commit
        String commitID = getHeadCommitID();
        Commit curr = getHeadCommit();
        // 防御性检测
        if (commitID == null || curr == null) {
            return;
        }
        // 迭代并打印
        while (curr != null) {
            System.out.println("===");
            System.out.println("commit " + commitID);
            System.out.println("Date: " + curr.getTimestamp());
            System.out.println(curr.getMessage());
            System.out.println();
            // 获取parentID
            List<String> parents = curr.getParent();
            // 如果curr为inital commit
            if (parents.isEmpty()) {
                curr = null;
            } else {
                commitID = parents.get(0);
                curr = Commit.fromFile(commitID);
            }
        }
    }

    /**
     *Like log, except displays information about all commits ever made.
     */
    public static void globalLog() {
        // 1.获取所有文件名
        List<String> fileName = Utils.plainFilenamesIn(OBJECT_DIR);
        if (fileName == null) {
            return; //防御
        }
        //2. 判断是commit还是blob，对commit调用printLog
        for (String item : fileName) {
            File file = join(OBJECT_DIR, item);
            if (!file.exists()) {
                return; //防御性检测
            }
            try {
                // 2. 【关键技巧】不要只读 Commit，而是读取为通用的 Serializable 接口
                // 这样无论是 Blob 还是 Commit 都能读出来，不会报错
                Serializable obj = Utils.readObject(file, Serializable.class);
                if (obj instanceof Commit) {
                    Commit commits = (Commit)obj;
                    printLog(commits, item);
                }
            } catch (IllegalArgumentException e) {
                // 如果遇到读不出来的坏文件，直接忽略，防止程序崩溃
                continue;
            }
        }
    }



    /**
     * 该方法用于从HEAD一路顺藤摸瓜获取当前分支最新的Commit
     * @return 当前分支最新的commit
     */
    public static Commit getHeadCommit() {
        String hash = getHeadCommitID();
        if (hash == null) {
            return null;
        }
        return Commit.fromFile(hash);
    }

    /**
     * 基础层，只返回CommitID
     * 该方法用于从HEAD一路顺藤摸瓜获取当前分支最新的CommitID
     * @return 当前分支最新的commitID
     */
    public static String getHeadCommitID() {
        //1. 读取分支名乘， master还是其他什么
        String branchName = readContentsAsString(HEAD_F).trim(); // trim去除可能的换行符


        // 2. 找到那个分支文件 (比如 .gitlet/refs/heads/master)
        File branchFile = Utils.join(HEADS_DIR, branchName);

        //3. 读取文件中的哈希hi
        if(!branchFile.exists()) {
            return null; //防御性检查
        }
        return readContentsAsString(branchFile).trim();

    }

    private static void printLog(Commit currCommit, String id) {
        System.out.println("===");
        System.out.println("commit " + id);
        System.out.println("Date: " + currCommit.getTimestamp());
        System.out.println(currCommit.getMessage());
        System.out.println();
    }

}
