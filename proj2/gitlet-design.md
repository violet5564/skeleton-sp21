# Gitlet Design Document

**Name**:Shabriri

## Classes and Data Structures

### 1.Commit
commit表示用于记录gitlet中的一个个commit信息，包括message，
date, parent commit，fileMap
#### Fields

1. private String message;
用于存储message信息
2. private String timestamp;commit的创建时间，assigned by the constructor
3. private String parent;父commit的hashcode
4. private HashMap<String, String> fileMap; 用于储存commit所追踪的文件，结构是文件名-> Blob Hash


### 2.Repository
这里是程序逻辑的主要所在，该类处理了所有gitlet的实际命令，序列化，以及错误检查操作等等。
#### Fields

1. public static final File CWD = new File(System.getProperty("user.dir"));当前工作目录
2. public static final File GITLET_DIR = join(CWD, ".gitlet"); .gitlet目录，所有备份文件，commit都在这里
3. public static final File OBJECT_DIR = join(GITLET_DIR, "objects");用于存放序列化后的文件，包含blob和commit
4. public static final File REFS_DIR = join(GITLET_DIR, "refs");存放所有的引用
5. public static final File HEAD_F = join(GITLET_DIR, "HEAD");存放当前分支
6. stage_FILE = join(GITLET_DIR, "stage");存放Staging area

### 3.Stage
Stage对象用于staging area的实现，存放着add 和remove的staging area
#### Fields

1. private HashMap<String,String> addFile;储存待add的文件名和哈希值
2. private HashSet<String> removeFile;储存待remove的文件名和哈希值

## Algorithms

## Persistence
The directory structure looks like this:
.gitlet
|--objects/             <-- [目录] 所有的文件内容(Blob)和Commit对象都混在这个池子里
|    |commits and blobs/ <-- [文件] 内容是序列化之后的文件
|
|--refs/                <-- [目录]
|    |--heads/          <-- [目录]
|         |--master     <-- [文件] 内容是 Commit Hash (例如 "a1b2c3...")
|         |--other      <-- [文件] 内容是 Commit Hash
|
|--HEAD                 <-- [文件] 内容是 "ref: refs/heads/master"
|
|--Stage               <-- [文件] 这是一个序列化后的 Stage 对象。 它里面同时包含了 addedMap 和 removedSet。
