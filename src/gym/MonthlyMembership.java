package gym;

/**
 * MonthlyMembership - a member who pays a rate entered by staff,
 * for one month.
 */
public class MonthlyMembership extends Membership {

    public MonthlyMembership() {
        super();
    }

    public MonthlyMembership(String memberName, String icNumber, String startDate, double rate) {
        super(memberName, icNumber, startDate, rate);
    }

    public MonthlyMembership(int memberID, String memberName, String icNumber, String startDate, double rate) {
        super(memberID, memberName, icNumber, startDate, rate);
    }

    @Override
    public double calculateFee() {
        // Monthly fee uses one month of the entered rate.
        return rate;
    }

    @Override
    public String getMembershipType() {
        return "Monthly";
    }

    @Override
    public int getDuration() {
        return 1; 
    }
}