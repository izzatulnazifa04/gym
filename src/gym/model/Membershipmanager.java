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

}    