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