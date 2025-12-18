package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  this class represent a commit object in gitlet
 *  does at a high level.
 *
 *  @author TODO
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
    private String parent; // ??如果有两个parent怎么办？
    // some other stuff


    /* TODO: fill in the rest of this class. */
    // Initialize commit obj
    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.message.equals("initial commit") && parent == null) {
            this.timestamp = dateToTimeStamp(new Date(0));
        } else {
            this.timestamp = dateToTimeStamp(new Date());
        }
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

    public String getParent() {
        return this.parent;
    }



}
