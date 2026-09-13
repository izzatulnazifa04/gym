package gym.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Parent class untuk semua jenis membership gym.
 * - MonthlyMembership & YearlyMembership akan override rules fee masing2.
 */
public abstract class Membership {
    // data ahli gym
    private final String memberId;     
    private final String memberName;    
    private final String icNumber;   
    private final LocalDate startDate;  
    private final LocalDate expiryDate; 
    private MembershipStatus status;    

    //Constructor
    protected Membership(String memberId, String memberName, String icNumber,
                         String phoneNumber, LocalDate startDate,
                         LocalDate expiryDate, MembershipStatus status) {
        // Pastikan semua text wajib diisi & tak lebih panjang
        this.memberId = requiredText(memberId, "Member ID", 30);
        this.memberName = requiredText(memberName, "Member name", 80);
        this.icNumber = requiredText(icNumber, "IC number", 30);
        this.phoneNumber = requiredText(phoneNumber, "Phone number", 30);

        // Validate tarikh
        if (startDate == null || expiryDate == null) {
            throw new IllegalArgumentException("Start dan expiry date wajib.");
        }
        if (expiryDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Expiry tak boleh sebelum start.");
        }

        this.startDate = startDate;
        this.expiryDate = expiryDate;
        // Default status = ACTIVE kalau null
        this.status = status == null ? MembershipStatus.ACTIVE : status;
    }

    //method untuk check text wajib yang isi takleh tinggal
    private static String requiredText(String value, String label, int maximumLength) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " wajib diisi.");
        }

        String cleanValue = value.trim();
        if (cleanValue.length() > maximumLength) {
            throw new IllegalArgumentException(label + " max " + maximumLength + " huruf.");
        }
        return cleanValue;
    }

    
    public String getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public String getIcNumber() { return icNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public MembershipStatus getStatus() { return status; }

    // Setter → ubah status membership
    public void setStatus(MembershipStatus status) {
        this.status = status == null ? MembershipStatus.ACTIVE : status;
    }

    // check membership dah expired ke tak
    public boolean checkExpiry() {
        return expiryDate.isBefore(LocalDate.now());
    }

    
    protected abstract int durationMonths();     
    public abstract long calculateFeeCents();  
    public abstract String getMembershipType();  

    
    protected long checkedRate(long rateCents) {
        if (rateCents < 1 || rateCents > 100_000_000L) {
            throw new IllegalArgumentException("Rate mesti RM0.01 - RM1,000,000.00.");
        }
        return rateCents;
    }

    
    public static Membership create(String memberId, String memberName,
                                    String icNumber, String phoneNumber,
                                    LocalDate startDate, String type,
                                    long rateCents, int discountPercent) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date wajib.");
        }

        String membershipType = type == null ? "" : type.trim().toUpperCase();
        if ("MONTHLY".equals(membershipType)) {
            return new MonthlyMembership(memberId, memberName, icNumber,
                    phoneNumber, startDate, startDate.plusMonths(1),
                    MembershipStatus.ACTIVE, rateCents);
        }

        if ("YEARLY".equals(membershipType)) {
            return new YearlyMembership(memberId, memberName, icNumber,
                    phoneNumber, startDate, startDate.plusMonths(12),
                    MembershipStatus.ACTIVE, rateCents, discountPercent);
        }

        throw new IllegalArgumentException("Type mesti MONTHLY atau YEARLY.");
    }

    
    public static Membership fromDatabase(String memberId, String memberName,
                                          String icNumber, String phoneNumber,
                                          LocalDate startDate,
                                          LocalDate expiryDate,
                                          MembershipStatus status,
                                          String type, long rateCents,
                                          int discountPercent) {
        String membershipType = type == null ? "" : type.trim().toUpperCase();
        if ("MONTHLY".equals(membershipType)) {
            return new MonthlyMembership(memberId, memberName, icNumber,
                    phoneNumber, startDate, expiryDate, status, rateCents);
        }

        if ("YEARLY".equals(membershipType)) {
            return new YearlyMembership(memberId, memberName, icNumber,
                    phoneNumber, startDate, expiryDate, status, rateCents,
                    discountPercent);
        }

        throw new IllegalArgumentException("Unknown type dalam DB: " + type);
    }

    
    public static long parseRate(String text) {
        if (text == null || !text.trim().matches("[0-9]+(\\.[0-9]{1,2})?")) {
            throw new IllegalArgumentException("Masukkan fee macam 80.00 (max 2 decimal).");
        }

        try {
            return new BigDecimal(text.trim())
                    .movePointRight(2) // tukar RM → cents
                    .longValueExact();
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Fee terlalu besar.");
        }
    }

    
    public static String money(long cents) {
        return BigDecimal.valueOf(cents, 2)
                .setScale(2, RoundingMode.UNNECESSARY)
                .toPlainString();
    }

    
    public int getDuration() {
        return durationMonths();
    }
}
