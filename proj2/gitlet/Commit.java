package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  this class represent a commit object in gitlet
 *  does at a high level.
 *
 *  @author Shabriri
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     * log massage, other matedata(commit date, author, etc), a reference to a tree,
     * references to parent commits
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private List<String> parents; // ??如果有两个parent怎么办？
    // some other stuff
    // commit追踪的文件
    private HashMap<String, String> fileMap; // 文件名-> Blob Hash

    /* TODO: fill in the rest of this class. */
    /**
     * Initialize commit object
     * @param message 接受命令行的消息
     * @param parents 该commit的父commit
     */
    public Commit(String message, List<String> parents, HashMap<String, String> fileMap) {
        this.message = message;
        this.parents = parents;
        // 第一步：设置时间，同时继承父commit的HashMap
        if (this.message.equals("initial commit") && parents.isEmpty()) {
            // 将timestamp设置为linux初始时间
            this.timestamp = dateToTimeStamp(new Date(0));
        } else {
            this.timestamp = dateToTimeStamp(new Date());
        }
        this.fileMap = fileMap;
    }

    /**
     * 计算并返回commit对象的commitID
     * @return 该commit对象的commitID
     */
    public String generateID() {
        return Utils.sha1(this.message, this.timestamp, this.parents.toString(), this.fileMap.toString());
    }

    // 用于返回指定git格式的日期
    private static String dateToTimeStamp(Date date) {
        // 格式字符串必须严格遵守 Spec
        // Thu Nov 9 20:00:05 2017 -0800
        // EEE = 星期几 (Wed)
        // MMM = 月份 (Dec)
        // d = 日期 (31)
        // HH:mm:ss = 时分秒
        // yyyy = 年
        // Z = 时区 (-0800)
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy  Z", Locale.US);
        return dateFormat.format(date);
    }



    /* get method was used to get instance attribute */
    public String getMessage() {
        return this.message;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public List<String> getParent() {
        return this.parents;
    }

    public HashMap<String, String> getFileMap() {
        return this.fileMap;
    }

    public String getFileHash(String fileName) {
        return fileMap.get(fileName);
    }

    /**
     * 读取文件名为name的文件
     * @param commitID 该commit对象的hashcode
     * @return Commit对象
     */
    public static Commit fromFile(String commitID) {
        File commitFile = join(OBJECT_DIR, commitID);
        if (!commitFile.exists()) {
            return null;
        }
        return readObject(commitFile, Commit.class);
    }

    /**
     * Saves a commit to a file for future use.
     */
    public void saveCommit() {
        //获取commitID
        String commitID = this.generateID();
        //获取保存路径
        File savePath = join(OBJECT_DIR, commitID);
        // 写入文件
        writeObject(savePath, this);

    }


}
