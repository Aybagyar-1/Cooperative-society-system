package model;

import java.util.Date;

public class Notice {
    private int noticeId;
    private String title;
    private String description;
    private Date date;

    public Notice(int noticeId, String title, String description, Date date) {
        this.noticeId = noticeId;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public int getNoticeId() { return noticeId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getDate() { return date; }
}