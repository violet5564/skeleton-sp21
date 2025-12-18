package gitlet;

import java.io.File;

import static gitlet.Utils.join;
// 把一些对象操作都放到这里，类似于CapersRepository
public class SomeObj {
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
    public void init() {
        // Get the current working directory
        File CWD = new File(System.getProperty("user.dir"));
        File gitFolder = join(CWD, ".gitlet");
        // If there is already a Gitlet version-control system
        // in the current directory, it should abort
        if (gitFolder.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);// 这里这么退出能行么
        }
        Commit initalCommit = new Commit("initial commit", null);
        //Branches? here we need to initialize Master branch
        // and have it point to the initial commit

        // what is UID?

        //initialize timestamp in commit class
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
    public void commit() {
        // Read form my computer the head commit object and the staging area


        // Clone the HEAD commit
        // modify its message and timestamp according to user input
        // Use the staging area in order to modify the files tracked by the new commit

        // Write back any new object made or any modified objects read earlier

        // After we done with commit needs to do, we need to ask did we make some new objects
        // that need to be saved.or did we read some objects and modified them.
    }
}
