package org.berendeev.roma.rssreader.data.entity;

public class HttpRssItem {
    private   String title;

    private  String link;

    private  String author;

    private String description;

    private  String pubDate;

    private  String thumbnail;

    private  String enclosure;

    public HttpRssItem() {
        title = "";
        link = "";
        description = "";
        author = "";
        pubDate = "";
        thumbnail = "";
        enclosure = "";
    }

    public void setField(String fieldName, String value) {
        switch (fieldName){
            case "title" :{
                title = value;
                break;
            }
            case "link" :{
                link = value;
                break;
            }
            case "description" :{
                description = value;
                break;
            }
            case "author" :{
                author = value;
                break;
            }
            case "pubDate" :{
                pubDate = value;
                break;
            }
            case "thumbnail" :{
                thumbnail = value;
                break;
            }
            case "enclosure" :{
                enclosure = value;
                break;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getEnclosure() {
        return enclosure;
    }
}
