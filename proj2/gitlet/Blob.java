package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

/**
 * 代表所有备份的文件，可以被序列化
 * 属性：文件名，文件内容
 * 方法：读取保存
 */
public class Blob implements Serializable {
    //文件名称
    private String fileName;
    private byte[] fileContent; //文件内容

    public Blob(String fileName, byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    /**从给定目录读取文件
     * @param blobHash Blob文件的哈希值
     */
    public static Blob fromFile(String blobHash) {
        File blobFile = join(OBJECT_DIR, blobHash);
        if (!blobFile.exists()) {
            return null;
        }
        return readObject(blobFile, Blob.class);
    }

    public String generateID() {
        return Utils.sha1(fileName, fileContent);
    }

    /**将Blob保存到Objects文件夹中
     *
     */
    public void save() {
//        String BlobHash = this.generateID();
        File savePath = join(OBJECT_DIR, this.fileName);
        writeObject(savePath, this);
    }

    /**
     * 用于返回Blob对象保存的文件内容
     * @return 以byte数组储存的文件内容
     */
    public byte[] getFileContent() {
        return fileContent;
    }




}
