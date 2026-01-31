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
            throw error("A Gitlet version-control system already exists in the current directory.");
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
            throw error("Please enter a commit message.");
        }
        // 2.  Read form my computer the head commit object and the staging area
        Commit currCommit = getHeadCommit();
        if (currCommit == null) {
            // 处理异常情况
            // 对于 Gitlet，这意味着数据损坏或找不到对象
            throw error("Error: Commit not found.");
        }
        Stage stage = Stage.readStage();
        HashMap<String, String> addFile = stage.getAddFile();
        HashSet<String> removeFile = stage.getRemoveFile();
        // failure case: 检查stage是否为空
        if (addFile.isEmpty() && removeFile.isEmpty()) {
            throw error("No changes added to the commit.");
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
     * 该重载方法用于处理merge最后的commit
     * @param message commit信息
     * @param otherBranch 与当前branch merge的另外的branch
     */
    public static void makeCommit(String message, String otherBranch) {
        // 1.failure case Every commit must have a non-blank message.
        if (message == null || message.trim().isEmpty()) {
            throw error("Please enter a commit message.");
        }
        // 2.  Read form my computer the head commit object and the staging area
        Commit currCommit = getHeadCommit();
        if (currCommit == null) {
            // 处理异常情况
            // 对于 Gitlet，这意味着数据损坏或找不到对象
            throw error("Error: Commit not found.");
        }
        Stage stage = Stage.readStage();
        HashMap<String, String> addFile = stage.getAddFile();
        HashSet<String> removeFile = stage.getRemoveFile();
        // failure case: 检查stage是否为空
        if (addFile.isEmpty() && removeFile.isEmpty()) {
            throw error("No changes added to the commit.");
        }
        // 3.准备parent列表
        String parentId1 = getHeadCommitID();
        List<String> parents = new ArrayList<>();
        parents.add(parentId1);
        parents.add(otherBranch);
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
    }


    /**
     * 将指定文件添加到staging area中
     */
    public static void add(String fileName) {
        // 1.读取文件
        File addFile = join(CWD, fileName);
        if(!addFile.exists()) {
            throw error("File does not exit");
        }
        // 2.计算文件的SHA-1
        byte[] fileContent = readContents(addFile);
        String currBlobHash = Utils.sha1(fileContent);

        // 将原本位于此处的Blob save逻辑后置。

        // 4.读取Stage对象和stage的addHashMap
        Stage stage = Stage.readStage();
        // 5.获取fileName在当前Commit的Hashcode（没有则为null）
        Commit commit = getHeadCommit();
        if (commit == null) {
            //commit为null,说明getlet文件夹被破坏，终止程序
//            System.exit(0);
            throw error("Can not find .gitlet dir");
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
        //将文件放入Blob文件夹
        File blobFile = join(OBJECT_DIR, currBlobHash);
        if (!blobFile.exists()) {
            Blob newBlob = new Blob(currBlobHash, fileContent);
            newBlob.save();
        }
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
            throw error("No reason to remove the file.");
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
     *  Prints out the ids of all commits that have the given commit message
     * @param message
     */
    public static void find(String message) {
        // 1.获取所有文件名
        List<String> fileName = Utils.plainFilenamesIn(OBJECT_DIR);
        if (fileName == null) {
            return; //防御
        }
        // 设置boolean变量用于判断failure case
        boolean found = false;

        // 2.判断是commit 还是blob
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
                    Commit curr = (Commit)obj;
                    // 遍历所有commit,message符合就输出id
                    if (curr.getMessage().equals(message)) {
                        found = true;
                        System.out.println(item);
                    }
                }
            } catch (IllegalArgumentException e) {
                // 如果遇到读不出来的坏文件，直接忽略，防止程序崩溃
                continue;
            }
        }
        if (!found) {
            throw error("Found no commit with that message.");
        }
    }

    /**
     * Displays branches, stage, other file information.
     */
    public static void status() {
        // 1.获取所有分支
        List<String> branches = Utils.plainFilenamesIn(HEADS_DIR);
        if (branches == null) {
            throw error("branches读取失败");
        }
        String head = Utils.readContentsAsString(HEAD_F).trim();
        //2. 给当前分支加上*
        for (int i = 0; i < branches.size(); i++) {
            if (branches.get(i).equals(head)) {
                branches.set(i, "*" + branches.get(i));
                break; // 通常只有一个当前分支，找到即可退出
            }
        }
        // 打印Branches表
        printTable("Branches", branches);

        //读取stage内容
        Stage stage = Stage.readStage();
        HashMap<String, String> addMap = stage.getAddFile();
        HashSet<String> rmSet = stage.getRemoveFile();
        // 将stage的Map和set都转成list方便打印
        List<String> addList = new ArrayList<>(addMap.keySet());
        List<String> rmList = new ArrayList<>(rmSet);
        Collections.sort(addList); // 字典序
        Collections.sort(rmList);  // 字典序
        printTable("Staged Files", addList);
        printTable("Removed Files", rmList);
        // 这样写既符合格式，又偷懒了（打印了标题和空行，但不打印内容）
        printTable("Modifications Not Staged For Commit", new ArrayList<>());
        printTable("Untracked Files", new ArrayList<>());
    }

    /**
     * 切换分支
     * @param targetBranch 目标分支id
     */
    public static void checkoutBranch(String targetBranch) {
        // 获取branch分支的head commit
        File targetBranchFile = join(HEADS_DIR, targetBranch);
        if (!targetBranchFile.exists()) {
            throw error("No such branch exists.");
        }
        // 如果是同一个branch，报错
        String currBranch = readContentsAsString(HEAD_F).trim();
        if (currBranch.equals(targetBranch)) {
            throw error("No need to checkout the current branch.");
        }
        // 获取targetBranch headCommit
        String targetBranchID = readContentsAsString(targetBranchFile).trim();
        // 调用核心报错和文件覆盖逻辑
        checkoutAllFiles(targetBranchID);
        // 更改HEAD
        Utils.writeContents(HEAD_F, targetBranch);
    }

    /**
     * 将指定文件恢复到最新的commit中的状态
     * @param file 文件名
     */
    public static void checkoutFile(String file) {
        //1. 从head中读取最新的commit
        String currID = getHeadCommitID();
        if (currID == null) {
            throw error("Can't find Head Commit.");
        }
        checkoutCommitFile(currID, file);
    }

    /**
     * 从之前的某个Commit追踪的文件中把filename的文件拉过来
     * @param commitID 之前的某个commitID
     * @param file 文件名
     */
    public static void checkoutCommitFile(String commitID, String file) {
        // 1.根据ID获取commit对象
         String fullID = resolveCommitID(commitID);
         if (fullID == null) throw error("No commit with that id exists.");
        Commit curr = Commit.fromFile(fullID);
        if (curr == null) {
            throw error("No commits with this id exits.");
        }

        // 2.读取Blob对象,处理failure case
        String blobId = curr.getFileHash(file);
        if (blobId == null) {
            throw error("File does not exist in that commit.");
        }
        // 3.读取blob对象
        Blob fileBlob = Blob.fromFile(blobId);
        if (fileBlob == null) {
            throw error("读取Blob对象失败");
        }
        // 写入CWD的file
        File file1 = join(CWD, file);
        Utils.writeContents(file1, (Object) fileBlob.getFileContent());

    }


    /**
     * Creates a new branch with the given name, and points it at the current head commit.
     * @param branchName the new branch name
     */
    public static void branch (String branchName) {
        // 在HEADS文件夹中创建分支文件，名字是分支名
        File branchFile = join(HEADS_DIR, branchName);
        if (branchFile.exists()) {
            throw error("A branch with that name already exists.");
        }
        // 获取最新commitID
        String currId = getHeadCommitID();

        writeContents(branchFile, currId);

    }

    /**
     * Deletes the branch with the given name.
     * @param branchName the branch need to be deleted
     */
    public static void rmBranch (String branchName) {
        File branchFile = join(HEADS_DIR, branchName);
        if (!branchFile.exists()) {
            throw error("A branch with that name does not exist.");
        }
        // 如果移除的是当前的branch，报错
        String currBranch = Utils.readContentsAsString(HEAD_F);
        if (currBranch.equals(branchName)) {
            throw error("Cannot remove the current branch.");
        }
         Utils.restrictedDelete(branchFile);

    }

    /**
     *将版本回滚到指定的commitID处
     * @param commitID 给定的commit的哈希值
     */
    public static void reset (String commitID) {
        //处理缩写hash
        String fullCommitID = resolveCommitID(commitID);
        //核心处理逻辑
        checkoutAllFiles(fullCommitID);
        // 3. 移动当前分支的指针 (Reset 的特有动作)
        //    HEAD 现在存的是 "master"
        String currentBranchName = readContentsAsString(HEAD_F).trim();
        File branchFile = join(HEADS_DIR, currentBranchName);

        //    把 master 强行指向这个 commitID
        writeContents(branchFile, fullCommitID);

    }

    /**
     * Merges files from the given branch into the current branch.
     * @param branchName given branch
     */
    public static void merge (String branchName) {
        //1. failure case
        // 1.1 stage不为空报错
        Stage stage = Stage.readStage();
        if (!stage.getAddFile().isEmpty() || !stage.getRemoveFile().isEmpty()) {
            throw error("You have uncommitted changes.");
        }
        // 1.2 Given branch不存在报错
        File branchFile = join(HEADS_DIR, branchName);
        if (!branchFile.exists()) {
            throw error("A branch with that name does not exist.");
        }
        // 1.3 试图merge自己，报错
        String currBranch = readContentsAsString(HEAD_F);
        if (currBranch.equals(branchName)) {
            throw error("Cannot merge a branch with itself.");
        }
        // 1.4 untracked file 要被deleted or overwriten

        // 2. 使用BFS找到split point
        // 获取curr branch commit and given branch commit
        String currCommit = getHeadCommitID();
        String givenCommit = readContentsAsString(branchFile);
        String splitPoint = findSplitPoint(currCommit, givenCommit);

        // 获取split point 之后处理错误情况
        if (Objects.equals(splitPoint, givenCommit)) {
            throw error("Given branch is an ancestor of the current branch.");
        }
        if (Objects.equals(currCommit, splitPoint)) {
            checkoutBranch(branchName);
            message("Current branch fast-forwarded.");
            return;
        }

        // 获取三个commit的HashMap用于后续的文件操作
        Commit curr = Commit.fromFile(currCommit);
        if (curr == null) throw error("读取commit%s失败", curr);
        HashMap<String, String> currMap = curr.getFileMap();
        Commit given = Commit.fromFile(givenCommit);
        if (given == null) throw error("读取commit%s失败", given);
        HashMap<String, String> givenMap = given.getFileMap();
        Commit split = Commit.fromFile(splitPoint);
        if (split == null) throw error("读取commit%s失败", split);
        HashMap<String, String>  sMap = split.getFileMap();

        // 3.构造文件并集
        // 获取迭代文件列表split + given + curr(CWD的untracked怎么说?)
        HashSet<String> allFile = new HashSet<>();
        allFile.addAll(curr.getFileMap().keySet());
        allFile.addAll(given.getFileMap().keySet());
        allFile.addAll(split.getFileMap().keySet());

        // 4. 检测untracked file
        // 遍历CWD中的文件，如果untracked,且givenMap中有，报错。（这是更宽泛的条件，可以更严格）
        List<String> cwdFile = Utils.plainFilenamesIn(CWD);
        if (cwdFile == null) throw error("读取CWD失败");//防御性检测
        for (String file : cwdFile) {
            if (!currMap.containsKey(file) && givenMap.containsKey(file)) {
                throw error("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }

        // 5. 核心循环(get返回的结果是空怎么办？即不存在当前commit,应该也没关系把)
        for (String file : allFile) {
            String sHash = sMap.get(file);
            String cHash = currMap.get(file);
            String gHash = givenMap.get(file);
            // 5.1 given改了或者删了，curr没改
            //5.5 只在given中删除
            // 此处使用object.equals来避免NPE报错
            if (Objects.equals(sHash, cHash) && !Objects.equals(sHash, gHash)) {
                if (gHash != null) {
                    checkoutCommitFile(givenCommit, file);
                    add(file);
                } else {
                    rm(file);
                }
//             下面的情况都不需要操作因此直接忽略
//            } else if (Objects.equals(sHash, gHash) && !Objects.equals(sHash, cHash)) {
//                // 5.2given没改，curr改了
//                // 5.4 只在curr中删除
//                // do nothing
//            } else if (!Objects.equals(sHash, cHash) && Objects.equals(cHash, gHash)) {
//                // 5.3 修改内容一样，什么也不变。
            } else if (!Objects.equals(sHash, cHash) &&
                    !Objects.equals(sHash, gHash) &&
                    !Objects.equals(cHash, gHash)
            ) {
                // curr和given都和split不一样，冲突！
                message("Encountered a merge conflict.");

                String currBranchContent = "";
                String givenBranchContent = "";
                if (cHash != null) { // ✅ 先检查 null
                    // 最好封装一个 Blob.getContentAsString(hash)
                    Blob b = Blob.fromFile(cHash);
                    if (b != null) currBranchContent = new String(b.getFileContent()); // 注意 byte[] 转 String
                }
                if (gHash != null) {
                    Blob b2 = Blob.fromFile(gHash);
                    if (b2 != null) givenBranchContent = new String(b2.getFileContent());
                }

                String newContent = "<<<<<<< HEAD\n" + currBranchContent + "\n" + "=======\n" + givenBranchContent + "\n" + ">>>>>>>";
                File newFile = join(CWD, file);
                writeContents(newFile, newContent);
                add(file);
            }
        }


        // 6. commit
        makeCommit("Merged " + branchName + " into " + currBranch + ".", givenCommit);
    }

//    =========================================
//    -------------------辅助函数---------------
//    =========================================
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

    /**
     * 辅助函数，用于帮助global-log命令打印内容弄个
     * @param currCommit 要打印的commit
     * @param id currCommit对应的哈希值
     */
    private static void printLog(Commit currCommit, String id) {
        System.out.println("===");
        System.out.println("commit " + id);
        System.out.println("Date: " + currCommit.getTimestamp());
        System.out.println(currCommit.getMessage());
        System.out.println();
    }

    /**
     * 辅助函数，帮助status打印所有的的表
     * @param headName 表头
     * @param content 表中内容
     */
    private static void printTable(String headName, List<String> content) {
        message("=== %s ===", headName);
        for (String item : content) {
            System.out.println(item);
        }
        System.out.println();
    }

    /**
     * 该方法用于处理输入的commitid为简化版的情况
     * @param shortID
     * @return
     */
    private static String resolveCommitID(String shortID) {
        // 如果长度是 40，假设它是完整的
        if (shortID.length() == 40) {
            return shortID;
        }
        // 否则去 objects 目录里遍历查找以 shortID 开头的文件
        List<String> objects = Utils.plainFilenamesIn(OBJECT_DIR);
        for (String id : objects) {
            if (id.startsWith(shortID)) {
                return id; // 找到了！(假设没有重复前缀，简化处理)
            }
        }
        // 没找到
        return null;
    }

//    /**
//     * 返回所有被当前分支track的文件
//     * @return 被当前分支追踪的文件名
//     */
//    private static Set<String> getTrackedFile (){
//        HashSet<String> tracked = new HashSet<>();
//        Commit curr = getHeadCommit();
//        if (curr == null) throw error("读取最新commit失败。");
//
//        Stage stage = Stage.readStage();
//
//
//        // 1. 所有当前 commit 中的文件（tracked by default）
//        tracked.addAll(curr.getFileMap().keySet());
//
//        // 2. 所有 staged for addition 的文件（即使是新文件，也算 tracked）
//        tracked.addAll(stage.getAddFile().keySet());
//
//        // 3. 移除所有 staged for removal 的文件
//        tracked.removeAll(stage.getRemoveFile());
//
//        return tracked;
//    }

    /**
     * 将CWD变为指定commit的状态
     * @param targetCommitID 指定commit的hash
     */
    private static void checkoutAllFiles(String targetCommitID) {
        // 1.获取相关文件列表
        //获取目标commit的fileMap
        Commit targetCommit = Commit.fromFile(targetCommitID);
        if (targetCommit == null) {//防御性检测
            throw error("No commit with that id exists.");
        }
        HashMap<String, String> targetCommitFileMap = targetCommit.getFileMap();
        // 获取curr commit的fileMap
        Commit curr = getHeadCommit();
        if (curr == null) {
            throw error("获取currCommit失败。");
        }
        HashMap<String, String> currCommitFileMap = curr.getFileMap();
        // 处理failure case
        Stage stage = Stage.readStage();
        HashMap<String, String> addMap = stage.getAddFile();
        List<String> fileName = Utils.plainFilenamesIn(CWD);
        if (fileName != null) {
            for(String item : fileName) {
                boolean inTarget = targetCommitFileMap.containsKey(item);
                boolean InCurr = currCommitFileMap.containsKey(item);
                boolean inStage = addMap.containsKey(item);
                if (inTarget && !InCurr && !inStage) {
                    throw error("There is an untracked file in the way; delete it, or add and commit it first.");
                }
            }
        }

        //2. 覆盖文件
        for (String blobName : targetCommitFileMap.keySet()) {//遍历所有的blob name
            String blobHash = targetCommitFileMap.get(blobName); //得到blob的Hash
            Blob blob = Blob.fromFile(blobHash);
            if (blob == null) {
                throw error("Corrupt gitlet: missing blob %s", blobHash);
            }
            File overWrite = join(CWD, blobName);
            Utils.writeContents(overWrite, (Object) blob.getFileContent());
        }
        // 3. 删除targetCommit没有的文件
        for (String item : currCommitFileMap.keySet()) {
            // 如果目标commit没有追踪
            if (!targetCommitFileMap.containsKey(item)) {
                File delete = join(CWD, item);
                Utils.restrictedDelete(delete);
            }
        }
        //4.清空stage
        stage.clear();
    }

    /**
     * 输入两个分支的commit，找到两个分支的split point
     * @return split point的commit id
     */
    private static String findSplitPoint(String commitID1, String commitID2) {
        // 1. 收集commitID1的parent
        Set<String> ancestors = new HashSet<>();
        Queue<String> fringe = new LinkedList<>();

        fringe.add(commitID1);

        while (!fringe.isEmpty()) {
            String curr = fringe.poll();
            ancestors.add(curr);
            // continue through curr's parents
            Commit commit = Commit.fromFile(curr);
            if (commit == null) {
                throw error("can not get commit %curr", curr);
            }
            List<String> parents = commit.getParent();
            if (parents == null) continue;
            for (String item : parents) {
                if (!ancestors.contains(item)) {
                    fringe.add(item);
                }
            }
        }
        // 2.遍历commitID2的parent，找到最近的共同的parent
        fringe.clear();
        fringe.add(commitID2);
        // 还需要一个 visited 集合防止 Given 分支自己有环（虽然 Git 不会有环）或者重复访问
        Set<String> visited = new HashSet<>();
        while (!fringe.isEmpty()) {
            String curr = fringe.poll();
            visited.add(curr);// 标记访问过的
            // =====核心判断逻辑=======
            if (ancestors.contains(curr)) {
                return curr;
            }
            // 把parent加入队列
            Commit commit = Commit.fromFile(curr);
            if (commit == null) {
                throw error("can not get commit %curr", curr);
            }
            List<String> parents = commit.getParent();
            if (parents == null) continue;
            for (String parent : parents) {
                if (!visited.contains(parent)) {
                    fringe.add(parent);
                }
            }
        }
        return null;
    }
}

