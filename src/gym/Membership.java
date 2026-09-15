package gym;

/**
 * Membership is the abstract superclass representing a gym member.
 */
public abstract class Membership {
    private int memberID;
    private String memberName;
    private String icNumber;
    private String phoneNumber;
    private String startDate;   // format: yyyy-MM-dd
    protected double rate;      // base fee/rate entered by staff (RM)

    private String expiryDate = ""; //cek expired membership
    private String status = "";
    
    public Membership() {
        memberName = "";
        icNumber = "";
        phoneNumber = "";
        startDate = "";
        rate = 0.0;
    }

    public Membership(String memberName, String icNumber, String phoneNumber, String startDate, double rate) {
        this.memberName = memberName;
        this.icNumber = icNumber;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.rate = rate;
    }

    public Membership(int memberID, String memberName, String icNumber, String phoneNumber, String startDate, double rate) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.icNumber = icNumber;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.rate = rate;
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
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getStartDate() {
        return startDate;
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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