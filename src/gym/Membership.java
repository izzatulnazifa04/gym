package gym;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Membership is the abstract superclass representing a gym member.
 */
public abstract class Membership {
    private int memberID;
    private String memberName;
    private String icNumber;
    private String startDate;   // format: yyyy-mm-d
    private String endDate;   // format: yyyy-mm-dd
    protected double rate;      // base fee/rate entered by staff (RM)
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    
    public Membership() {
        memberName = "";
        icNumber = "";
        startDate = "";
        rate = 0.0;
    }

    public Membership(String memberName, String icNumber, String startDate, double rate) {
        this.memberName = memberName;
        this.icNumber = icNumber;
        this.startDate = startDate;
        this.rate = rate;
        this.endDate = calculateEndDate();
    }

    public Membership(int memberID, String memberName, String icNumber, String startDate, double rate) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.icNumber = icNumber;
        this.startDate = startDate;
        this.rate = rate;
        this.endDate = calculateEndDate();
    }

    public int getMemberID() {
        return memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getIcNumber() {
        return icNumber;
    }
    
    
    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String calculateEndDate() {
        if (startDate == null || startDate.isEmpty()) {
            return "";
        }
        try {
            LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
            LocalDate end = start.plusMonths(getDuration());
            return end.format(DATE_FORMAT);
        } catch (Exception e) {
            return "";
        }
    }

    public double getRate() {
        return rate;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    
    public abstract double calculateFee();

    
    public abstract String getMembershipType();

    // membership period (Monthly = 1, Yearly = 12).
    public abstract int getDuration();
    
    public double getDiscountPercent() {
        return 0.0;
    }

    @Override
    public String toString() {
        return "ID: " + memberID
             + " | Name: " + memberName
             + " | IC: " + icNumber
             + " | Type: " + getMembershipType()
             + " | Start: " + startDate
             + " | Fee: RM" + String.format("%.2f", calculateFee());
    }
}