package gym;

public class YearlyMembership extends Membership {

    private double discountPercent;

    public YearlyMembership() {
        super(); //nak panggil constructor dari superclass Membership
        discountPercent = 0.0;
    }

    public YearlyMembership(String memberName, String icNumber, String startDate, double rate, double discountPercent) {
        super(memberName, icNumber, startDate, rate); //hantar ke superclass
        this.discountPercent = discountPercent;
    }

    public YearlyMembership(int memberID, String memberName, String icNumber, String startDate, double rate, double discountPercent) {
        super(memberID, memberName, icNumber, startDate, rate);
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculateFee() {
        
        // "rate" = harga sebelum diskaun
        // kira harga lepas diskaun: rate × (1 - diskaun%)
        double discountedPrice = rate * (1 - discountPercent / 100.0);
        return Math.round(discountedPrice * 100.0) / 100.0;
    }

    @Override
    public String getMembershipType() {
        return "Yearly";
    }

    @Override
    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public int getDuration() {
        return 12; 
    }
    
}