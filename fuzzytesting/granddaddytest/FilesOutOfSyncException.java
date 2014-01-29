package fuzzytesting.granddaddytest;

public class FilesOutOfSyncException extends Exception {

    public FilesOutOfSyncException(String message){
        super(message);
    }

    public FilesOutOfSyncException(){
        super();
    }
}