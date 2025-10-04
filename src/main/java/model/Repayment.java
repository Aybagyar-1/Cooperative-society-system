package model;

import java.util.Date;

public class Repayment {
    private int repaymentId;
    private int loanId;
    private double amount;
    private Date date;

    public Repayment(int repaymentId, int loanId, double amount, Date date) {
        this.repaymentId = repaymentId;
        this.loanId = loanId;
        this.amount = amount;
        this.date = date;
    }

    public int getRepaymentId() { return repaymentId; }
    public int getLoanId() { return loanId; }
    public double getAmount() { return amount; }
    public Date getDate() { return date; }
}
