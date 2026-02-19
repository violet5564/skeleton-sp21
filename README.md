Gitlet - A Version Control System in Java
Gitlet is a version-control system that mimics the core functionality of Git. It supports local version control operations including initialization, committing, branching, merging, and checking out specific versions of files.
This project was implemented as part of the CS 61B Data Structures course at UC Berkeley. It involves complex file I/O, object serialization, and graph algorithms to manage the history of a repository.
🚀 Key Features
Repository Management: init, status
Staging Area: add, rm (Supports staging for addition and removal)
Commit History: commit, log, global-log, find
Branching & Merging: branch, rm-branch, checkout, reset, merge
Conflict Resolution: Automatically detects merge conflicts and modifies files with standard conflict markers (<<<<<<<, =======, >>>>>>>).
🛠 Technical Architecture
1. Persistence & Serialization
The state of the repository (Commits, Blobs, Staging Area) is persisted to the disk using Java Serialization.
Blobs: File contents are treated as byte arrays and stored as blobs.
Commits: Commit objects contain metadata (message, timestamp, parent references) and a mapping of file names to blob references.
2. Content-Addressable Storage (SHA-1)
Gitlet uses SHA-1 hashing to generate unique IDs for every object (Commit or Blob).
The .gitlet/objects directory acts as a key-value store where the key is the SHA-1 hash and the value is the serialized object.
This ensures data integrity and deduplication (files with identical content share the same blob).
3. Graph Algorithms (DAG)
The commit history is structured as a Directed Acyclic Graph (DAG).
Branching: Implemented as pointers (refs) to specific commit nodes.
Merging: Implemented a graph traversal algorithm (BFS) to find the Split Point (Latest Common Ancestor) between the current branch and the given branch to determine how files should be merged.
📂 Internal Structure
The .gitlet directory structure:
code
Text
.gitlet
|-- objects/        # Stores all Blobs and Commits by SHA-1 hash
|-- refs/
|    |-- heads/     # Stores branch pointers (e.g., master)
|-- HEAD            # Stores the name of the current active branch
|-- stage           # Serialized Staging Area (Add/Remove maps)
💻 Usage
To compile the project:
code
Bash
javac gitlet/*.java
To run commands:
code
Bash
# Initialize a repository
java gitlet.Main init

# Add a file
java gitlet.Main add hello.txt

# Commit changes
java gitlet.Main commit "Added hello.txt"

# View logs
java gitlet.Main log

# Create a new branch
java gitlet.Main branch feature-x

# Checkout the new branch
java gitlet.Main checkout feature-x

# Merge master into feature-x
java gitlet.Main merge master
🏆 Challenges & Learnings
Handling Edge Cases: Implemented robust logic to handle edge cases like "untracked files overwriting", "merging an ancestor branch", and "split point identification".
Cross-Platform Compatibility: Solved line-ending discrepancies (CRLF vs LF) between Windows and Unix environments to ensure consistent hashing and file processing.
System Design: Designed a clean separation of concerns between the command interface (Main), the logic controller (Repository), and data models (Commit, Stage, Blob).
