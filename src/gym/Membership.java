package gym;

/**
 * Membership is the abstract superclass representing a gym member.
 * Common attributes are stored here; each subclass provides its own
 * fee calculation rule (polymorphism).
 */
public abstract class Membership {
    private int memberID;
    private String memberName;
    private String icNumber;
    private String startDate;   // format: yyyy-MM-dd
    protected double rate;      // base fee/rate entered by staff (RM)

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
    }

    public Membership(int memberID, String memberName, String icNumber, String startDate, double rate) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.icNumber = icNumber;
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

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    // Abstract method - each subclass MUST provide its own fee calculation.
    public abstract double calculateFee();

    // Abstract method - each subclass identifies its own type string.
    public abstract String getMembershipType();

    // Returns the discount percentage used (0 for Monthly, staff-entered value for Yearly).
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