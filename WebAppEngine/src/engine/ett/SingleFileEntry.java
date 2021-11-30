package engine.ett;

public class SingleFileEntry {
    private final String fileString;
    private final String username;
    private final long time;

    public SingleFileEntry(String fileString, String username) {
        this.fileString = fileString;
        this.username = username;
        this.time = System.currentTimeMillis();
    }

    public String getFileString() {
        return fileString;
    }

    public long getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return (username != null ? username + ": " : "") + fileString;
    }
}
