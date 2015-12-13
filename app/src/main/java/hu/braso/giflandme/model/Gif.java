package hu.braso.giflandme.model;

/**
 * Created by Illés László on 2015.12.13..
 */
public class Gif {

    private int id;
    private String url;
    private String path;
    private boolean xxx;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isXxx() {
        return xxx;
    }

    public void setXxx(boolean xxx) {
        this.xxx = xxx;
    }
}
