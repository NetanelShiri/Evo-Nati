package engine.ett;

import UI.UIAdapter;

import java.io.InputStream;
import java.util.*;

/*
This class is thread safe in the manner of adding\fetching new chat lines, but not in the manner of getting the size of the list
if the use of getVersion is to be incorporated with other methods here - it should be synchronized from the user code
 */
public class FileManager {

    private final List<SingleFileEntry> fileEntries;
    HashMap<String,Integer> userFilesAmount;
    private final Map<String,UIAdapter> fileAdapter ;
    private UIAdapter chosenAdapter = null;

    public FileManager() {
        fileEntries = new ArrayList<>();
        userFilesAmount = new HashMap<>();
        fileAdapter = new HashMap<>();
    }

    public synchronized void setChosenAdapter(String fileString){
        chosenAdapter = fileAdapter.getOrDefault(fileString, null);
    }

    public synchronized UIAdapter getChosenAdapter(){
        return chosenAdapter;
    }

    public synchronized boolean canAddMore(String username){
        int count  = userFilesAmount.getOrDefault(username,0);
        return count != 2;
    }

    //adding file as a string! (actual file already added to the server)
    public synchronized void addFileString(String fileString, String username) {
        fileEntries.add(new SingleFileEntry(fileString, username));
        int count = userFilesAmount.getOrDefault(username, 0);
        userFilesAmount.put(username, count + 1);

    }

    public synchronized void createFileAdapter(String fileString , InputStream stream){
        UIAdapter adapter = new UIAdapter();
        System.out.println("works here1");
        adapter.importData2(stream);
        System.out.println("works here2");
        fileAdapter.put(fileString,adapter);
        System.out.println("works here3");
    }

    public synchronized UIAdapter getFileAdapter(String fileString){
        return fileAdapter.getOrDefault(fileString, null);
    }

    public synchronized List<SingleFileEntry> getChatEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > fileEntries.size()) {
            fromIndex = 0;
        }
        return fileEntries.subList(fromIndex, fileEntries.size());
    }

    public int getVersion() {
        return fileEntries.size();
    }


}