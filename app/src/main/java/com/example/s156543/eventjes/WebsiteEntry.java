package com.example.s156543.eventjes;

/**
 * Created by s156543 on 11-6-2018.
 */

public class WebsiteEntry {

    String url;
    String type;
    String method;

    public WebsiteEntry(String url, String type, String method) {
        this.url = url;
        this.type = type;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
