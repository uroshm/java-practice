package practice.java.filesystem;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class FileSystemExample {
    Directory root;

    public FileSystemExample() {
        var dirRoot = new Directory("001", "root");
        var dirDocuments = new Directory("002", "documents");
        var fileResume = new File("003", "resume.pdf", "MyResumeContent");
        var dirReports = new Directory("004", "reports");
        var fileReport = new File("005", "reports.txt", "MyReporContent");

        var dirImages = new Directory("006", "images");
        var filePhoto1 = new File("007", "photo1.jpg", "abcdef");
        var filePhoto2 = new File("008", "photo2.jpg", "ghijkl");

        dirReports.setChildren(List.of(fileReport));
        dirImages.setChildren(List.of(filePhoto1, filePhoto2));
        dirDocuments.setChildren(List.of(dirReports, fileResume));
        dirRoot.setChildren(List.of(dirDocuments, dirImages));
        this.root = dirRoot;
    }

    public void find(String id) throws Exception {
        System.out.println("Found " + findById(this.root, id).id);
    }

    public static FileSystemNode findById(FileSystemNode node, String tgtId) {
        if (node.id.equals(tgtId))
            return node;
        if (node instanceof Directory directory) {
            node = (Directory) node;
            for (var child : directory.getChildren()) {
                var found = findById(child, tgtId);
                if (found != null)
                    return found;
            }
        }
        return null;
    }

    /*
     * Requirements: find the parent using your DFS, ensure the parent is a
     * Directory, reject duplicate IDs anywhere in the file system, and then add the
     * new node.
     */
    boolean addNode(String parentId, FileSystemNode newNode) {
        if (newNode == null || parentId == null || parentId.isBlank())
            return false;

        var parent = findById(this.root, parentId);

        if (parent == null)
            return false;
        if (parent instanceof Directory directory) {
            if (findById(root, newNode.getId()) != null)
                return false;
            else {
                directory.addChild(newNode);
                return true;
            }
        } else
            return false;

    }

    boolean removeNode(String targetId) {
        if (targetId.equals(root.getId()))
            return false;
        return removeNode(root, targetId);
    }

    boolean removeNode(FileSystemNode node, String targetId) {
        if (node instanceof Directory directory) {
            for (int i = 0; i < directory.getChildren().size(); i++) {
                if (directory.getChildren().get(i).getId().equals(targetId)) {
                    directory.removeChild(directory.getChildren().get(i));
                    return true;
                }
                if (removeNode(directory.getChildren().get(i), targetId))
                    return true;
            }
        }

        return false;
    }
}

abstract class FileSystemNode {

    @Getter
    protected final String id;

    @Getter
    protected final String name;

    public FileSystemNode(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Directory extends FileSystemNode {
    @Getter
    private List<FileSystemNode> children = new ArrayList<>();

    public void setChildren(List<FileSystemNode> children) {
        this.children.clear();
        this.children.addAll(children);
    }

    public void removeChild(FileSystemNode child) {
        this.children.remove(child);
    }

    public void addChild(FileSystemNode node) {
        this.children.add(node);
    }

    public Directory(String id, String name) {
        super(id, name);
    }
}

class File extends FileSystemNode {
    @Setter
    @Getter
    String content;

    public File(String id, String name, String content) {
        super(id, name);
        this.content = content;
    }
}