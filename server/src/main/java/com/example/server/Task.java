package com.example.server;

public class Task {
    private long id;
    private String content;
    private int progress;
    private boolean completed;
    public Task(){
        
    }
    public Task(long id, String content, int progress, boolean completed) {
        this.id = id;
        this.content = content;
        this.progress = progress;
        this.completed = completed;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public long getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public int getProgress() {
        return progress;
    }
    public boolean isCompleted() {
        return completed;
    }
    @Override
    public String toString(){
        return "ID: "+this.id+" Content: "+this.content+" Progress: "+this.progress+" Completed: "+this.completed;
    }
}
