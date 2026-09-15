package gym;

public class YearlyMembership extends Membership {

    private double discountPercent;

    public YearlyMembership() {
        super();
        discountPercent = 0.0;
    }

    public YearlyMembership(String memberName, String icNumber, String startDate, double rate, double discountPercent) {
        super(memberName, icNumber, startDate, rate);
        this.discountPercent = discountPercent;
    }

    public YearlyMembership(int memberID, String memberName, String icNumber, String startDate, double rate, double discountPercent) {
        super(memberID, memberName, icNumber, startDate, rate);
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculateFee() {
        // "rate" here represents the FULL yearly price (before discount)
        // when the type is Yearly, so the discount is applied directly.
        double discountedPrice = rate * (1 - discountPercent / 100.0);
        return Math.round(discountedPrice * 100.0) / 100.0;
    }
    
}