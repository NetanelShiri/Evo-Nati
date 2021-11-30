package engine.ett;

public class DataManager {


    FileManager fileManager;

    public DataManager() {
        fileManager = new FileManager();
    }

    FileManager getFileManager() {return fileManager;}

}
