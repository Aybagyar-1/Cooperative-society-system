package model;

import java.util.Date;

public class Loan {
    private int loanId;
    private int memberId;
    private double amountRequested;
    private Double amountApproved;
    private Integer repaymentMonths;
    private String status;
    private Date applicationDate;

    public Loan(int loanId, int memberId, double amountRequested, Double amountApproved, Integer repaymentMonths, String status, Date applicationDate) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.amountRequested = amountRequested;
        this.amountApproved = amountApproved;
        this.repaymentMonths = repaymentMonths;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    public int getLoanId() { return loanId; }
    public int getMemberId() { return memberId; }
    public double getAmountRequested() { return amountRequested; }
    public Double getAmountApproved() { return amountApproved; }
    public Integer getRepaymentMonths() { return repaymentMonths; }
    public String getStatus() { return status; }
    public Date getApplicationDate() { return applicationDate; }
}
