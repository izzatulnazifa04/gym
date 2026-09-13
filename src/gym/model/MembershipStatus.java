package gym.model;

public enum MembershipStatus {
    ACTIVE,
    EXPIRED,
    SUSPENDED;

    public static MembershipStatus fromDatabase(String value) {
        if (value == null) {
            return ACTIVE;
        }

        try {
            return MembershipStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ACTIVE;
        }
    }
}