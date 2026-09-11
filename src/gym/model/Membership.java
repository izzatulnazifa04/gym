package gym.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Parent class for every type of gym membership.
 *
 * MonthlyMembership and YearlyMembership provide their own fee rules.
 */
public abstract class Membership {
    private final String memberId;
    private final String memberName;
    private final String icNumber;
    private final String phoneNumber;
    private final LocalDate startDate;
    private final LocalDate expiryDate;
    private MembershipStatus status; 

    protected Membership(String memberId, String memberName, String icNumber,
                         String phoneNumber, LocalDate startDate,
                         LocalDate expiryDate, MembershipStatus status) {
        this.memberId = requiredText(memberId, "Member ID", 30);
        this.memberName = requiredText(memberName, "Member name", 80);
        this.icNumber = requiredText(icNumber, "IC number", 30);
        this.phoneNumber = requiredText(phoneNumber, "Phone number", 30);

        if (startDate == null || expiryDate == null) {
            throw new IllegalArgumentException("Start and expiry dates are required.");
        }
        if (expiryDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Expiry date cannot be before start date.");
        }

        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.status = status == null ? MembershipStatus.ACTIVE : status;
    }

}