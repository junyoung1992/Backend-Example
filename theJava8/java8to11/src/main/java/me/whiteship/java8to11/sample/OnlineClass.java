package me.whiteship.java8to11.sample;

import java.util.Optional;

public class OnlineClass {

    private Integer id;

    private String title;

    private boolean closed;

    public Progress progress;

    public OnlineClass(Integer id, String title, boolean closed) {
        this.id = id;
        this.title = title;
        this.closed = closed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    // null 을 리턴할 수 있는 경우 Optional로 감싼다
    public Optional<Progress> getProgress() {
        return Optional.ofNullable(progress);
//        return Optional.of(progress); // of는 null이 절대 리턴되지 않을 것으로 생각한다.
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

}
