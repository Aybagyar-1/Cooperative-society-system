package model;

import java.util.Date;

public class Saving {
    private int savingId;
    private int memberId;
    private double amount;
    private Date date;
    private String description;

    public Saving(int savingId, int memberId, double amount, Date date, String description) {
        this.savingId = savingId;
        this.memberId = memberId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public int getSavingId() { return savingId; }
    public int getMemberId() { return memberId; }
    public double getAmount() { return amount; }
    public Date getDate() { return date; }
    public String getDescription() { return description; }
}