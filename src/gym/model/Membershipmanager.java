package gym.model;

import gym.data.MembershipRepository;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Coordinates the membership objects and the database repository. */
public final class MembershipManager {
    private final List<Membership> memberList = new ArrayList<>(); // Senarai membership yang disimpan dalam memory (local cache)
    private final MembershipRepository repository;     // Repository untuk berhubung dengan database

    // Constructor: terima repository dan terus refresh senarai dari database
    public MembershipManager(MembershipRepository repository) throws SQLException {
        this.repository = repository;
        refresh(); // load semua data dari DB ke dalam memberList
    }

    // Dapatkan senarai membership (read-only, tak boleh ubah dari luar)
    public List<Membership> getMemberList() {
        return Collections.unmodifiableList(memberList);
    }

    // Dapatkan lokasi database (Path)
    public Path getDatabase() {
        return repository.getDatabase();
    }


}    