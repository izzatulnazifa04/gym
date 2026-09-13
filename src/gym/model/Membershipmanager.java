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

    // Tambah membership baru: insert ke DB dan tambah ke senarai local
    public void addMembership(Membership membership) throws SQLException {
        repository.insert(membership);   // simpan dalam DB
        memberList.add(membership);      // simpan dalam senarai memory
    }

    // Update membership sedia ada: update DB dan replace dalam senarai local
    public void updateMembership(Membership membership) throws SQLException {
        repository.update(membership);   // update dalam DB

        // Cari dalam senarai ikut memberId
        for (int index = 0; index < memberList.size(); index++) {
            Membership current = memberList.get(index);
            if (current.getMemberId().equals(membership.getMemberId())) {
                memberList.set(index, membership); // replace dengan object baru
                return;
            }
        }

        // Kalau tak jumpa dalam senarai, throw error
        throw new SQLException("Member is not loaded.");
    }


}    