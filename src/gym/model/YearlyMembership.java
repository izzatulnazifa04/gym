package gym.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/** A membership that lasts for twelve months and supports a discount. */
public final class YearlyMembership extends Membership {
    private final long yearlyRateCents;
    private final int discountPercent;

    public YearlyMembership(String memberId, String memberName,
                            String icNumber, String phoneNumber,
                            LocalDate startDate, LocalDate expiryDate,
                            MembershipStatus status, long yearlyRateCents,
                            int discountPercent) {
        super(memberId, memberName, icNumber, phoneNumber, startDate,
                expiryDate, status);
        this.yearlyRateCents = checkedRate(yearlyRateCents);

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(
                    "Discount must be between 0 and 100 percent.");
        }
        this.discountPercent = discountPercent;
    }

    public long getYearlyRateCents() {
        return yearlyRateCents;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    @Override
    protected int durationMonths() {
        return 12;
    }

    @Override
    public long calculateFeeCents() {
        BigDecimal rate = BigDecimal.valueOf(yearlyRateCents);
        BigDecimal percentage = BigDecimal.valueOf(100 - discountPercent);
        return rate.multiply(percentage)
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                .longValueExact();
    }

    @Override
    public String getMembershipType() {
        return "YEARLY";
    }
}

