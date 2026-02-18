package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import static gitlet.Repository.*;
import static gitlet.Utils.*;


/**
 * 该类用于实现Staging area的功能，用HashMap来储存待commit的文件
 * @author Shabriri
 */
public class Stage implements Serializable {
    // load和read逻辑封装到stage类中
    //有一个getAddedFiles方法，返回Map<文件名，blobHash>
    // 还有一个getRemovedFiles方法，
    // 再加一个clean方法


    private HashMap<String, String> addFile; //文件名 ->blob Hash
    // remove只需要文件，不需要hash
    private HashSet<String> removeFile;

    /**
     * 构造函数，初始化Stage对象
     */
    public Stage() {
        this.addFile = new HashMap<>();
        this.removeFile = new HashSet<>();
    }
    // == 功能方法 ==
    // 添加到add区
    public void add(String fileName, String fileHash) {
        this.addFile.put(fileName, fileHash);
    }
    // 添加到remove区
    public void addRemove(String fileName) {
        this.removeFile.add(fileName);
    }
    // 将指定文件从add区移除
    public void unstageAdd(String fileName) {
        this.addFile.remove(fileName);
    }
    // 将指定文件从remove区移除
    public void unstageRemove(String fileName) {
        this.removeFile.remove(fileName);
    }
    // 检查staging area是否清空
    public boolean isClean() {
        return addFile.isEmpty() && removeFile.isEmpty();
    }
    // Commit 之后清空暂存区
    public void clear() {
        addFile.clear();
        removeFile.clear();
        this.saveStage(); // 清空之后立刻保存
    }


    public HashMap<String, String> getAddFile() {
        return this.addFile;
    }
    public HashSet<String> getRemoveFile() {
        return this.removeFile;
    }
    // 从文件中读取Stage类
    public static Stage readStage() {
        if (!stage_FILE.exists()) {
            System.exit(0);
        }
        return readObject(stage_FILE, Stage.class);
    }
    // 将Stage对象写入文件
    public void saveStage() {
        writeObject(stage_FILE, this);
    }
}
