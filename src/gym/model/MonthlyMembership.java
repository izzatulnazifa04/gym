package gym.model;

import java.time.LocalDate;

/** A membership that lasts for one month. */
public final class MonthlyMembership extends Membership {
    private final long monthlyRateCents;

    public MonthlyMembership(String memberId, String memberName,
                             String icNumber, String phoneNumber,
                             LocalDate startDate, LocalDate expiryDate,
                             MembershipStatus status, long monthlyRateCents) {
        super(memberId, memberName, icNumber, phoneNumber, startDate,
                expiryDate, status);
        this.monthlyRateCents = checkedRate(monthlyRateCents);
    }

    public long getMonthlyRateCents() {
        return monthlyRateCents;
    }

    @Override
    protected int durationMonths() {
        return 1;
    }

    @Override
    public long calculateFeeCents() {
        return monthlyRateCents;
    }

    @Override
    public String getMembershipType() {
        return "MONTHLY";
    }
}
