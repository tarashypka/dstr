package com.deoxys.dev.dstr.persistence.dao.postgres;

import com.deoxys.dev.dstr.domain.model.user.User;
import com.deoxys.dev.dstr.domain.model.user.UserRoles;

import javax.sql.DataSource;

public class UserDAO extends PostgresDAO<User> {

    public UserDAO(DataSource ds) { super(ds); }

    private static final String
            USERS_COLLECTION,
            SELECT_USER_BY_ID,
            SELECT_USER_BY_EMAIL,
            INSERT_USER,
            UPDATE_USER_PASSWORD_BY_ID,     // different update clauses
            UPDATE_USER_STATUS_BY_ID,       // allows to avoid performing
            SWAP_STATUS_BY_ID,              // unnecessary updates
            DELETE_USER_BY_ID,
            FIND_USER_BY_EMAIL,
            COUNT_ALL_USERS;

    private static final ResultSetRetriever<User> userRetriever;

    /**
     *
     * The queries SELECT_???_BY_ID, DELETE_???_BY_ID, COUNT_ALL_???
     * are almost (instead of collection name they contain)
     * the same for all subclasses of PostgresDAO.
     *
     * But passing collection name to some and exact query to another
     * methods of PostgresDAO will make the design clumsy.
     *
     * Thus, for the sake of clarity, code duplication was chosen.
     *
     */
    static {
        USERS_COLLECTION = "users";

        SELECT_USER_BY_ID =
                "SELECT * FROM " + USERS_COLLECTION + " " +
                "WHERE id = ?";

        // Is required on authentication when id is not known yet
        SELECT_USER_BY_EMAIL =
                "SELECT * FROM " + USERS_COLLECTION + " " +
                        "WHERE email = ?";

        INSERT_USER =
                "INSERT INTO " + USERS_COLLECTION + "(email, password, role) " +
                "VALUES (?, ?, ?)";

        // It could be Customer trying to change his password
        UPDATE_USER_PASSWORD_BY_ID =
                "UPDATE " + USERS_COLLECTION + " " +
                "SET password = ? " +
                "WHERE id = ?";

        // It could be admin trying to ban/unban customer
        UPDATE_USER_STATUS_BY_ID =
                "UPDATE " + USERS_COLLECTION + " " +
                "SET enabled = ? " +
                "WHERE id = ?";

        SWAP_STATUS_BY_ID =
                "UPDATE " + USERS_COLLECTION + " " +
                "SET enabled = NOT enabled " +
                "WHERE id = ?";

        DELETE_USER_BY_ID =
                "DELETE FROM " + USERS_COLLECTION + " " +
                "WHERE id = ?";

        // Used on registration to check whether email is reserved
        FIND_USER_BY_EMAIL =
                "SELECT FROM " + USERS_COLLECTION + " " +
                "WHERE email = ? " +
                "LIMIT 1";

        COUNT_ALL_USERS =
                "SELECT COUNT(*) FROM " + USERS_COLLECTION;

        // Static retriever to avoid code duplication in get(id), get(email)
        userRetriever = rs ->
                new User.UserBuilder(rs.getString("email"))
                        .withId(rs.getLong("id"))
                        .withPassword(rs.getString("password"))
                        .withRole(UserRoles.roleOf(rs.getString("role")))
                        .enabled(rs.getBoolean("enabled"))
                        .build();
    }

    public User get(long id) {
        return get(SELECT_USER_BY_ID, stmt -> stmt.setLong(1, id), userRetriever);
    }

    public User get(String email) {
        return get(SELECT_USER_BY_EMAIL, stmt -> stmt.setString(1, email), userRetriever);
    }

    public boolean add(User user) {
        return add(INSERT_USER,
                stmt -> {
                    stmt.setString(1, user.getEmail());
                    stmt.setString(2, user.getPassword());
                    stmt.setString(3, user.getRole().toString());
                }
        );
    }

    public boolean updatePassword(User user) {
        return update(UPDATE_USER_PASSWORD_BY_ID,
                stmt -> {
                    stmt.setString(1, user.getPassword());
                    stmt.setLong(2, user.getId());
                }
        );
    }

    public boolean updateStatus(User user) {
        return update(UPDATE_USER_STATUS_BY_ID,
                stmt -> {
                    stmt.setBoolean(1, user.isEnabled());
                    stmt.setLong(2, user.getId());
                }
        );
    }

    public boolean swapStatus(long id) {
        return update(SWAP_STATUS_BY_ID, stmt -> stmt.setLong(1, id));
    }

    public boolean delete(long id) {
        return delete(DELETE_USER_BY_ID, stmt -> stmt.setLong(1, id));
    }

    public boolean exists(User user) {
        return exists(FIND_USER_BY_EMAIL, stmt -> stmt.setString(1, user.getEmail()));
    }

    public long count() {
        return count(COUNT_ALL_USERS);
    }
}