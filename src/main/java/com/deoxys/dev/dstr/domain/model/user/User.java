package com.deoxys.dev.dstr.domain.model.user;

import com.deoxys.dev.dstr.domain.model.Builder;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

import static com.deoxys.dev.dstr.domain.model.user.UserRole.ADMIN;
import static com.deoxys.dev.dstr.domain.model.user.UserRole.CUSTOMER;

public class User implements Serializable {

    /**
     * long vs Long
     *   JDBC takes and produces long primitive type
     *   thus, long will avoid unnecessary autoboxing
     *
     * id is also 
     */
    long id;

    /**
     * email is unique identifier of particular user,
     * thus, User id (primary key) could be avoided on this level
     */
    String email;

    /**
     * password is taken/given in plain text and then
     * encoded/decoded with jBcrypt-0.4 (Jan, 2015) before being persisted
     */
    String password;

    /**
     * errType is one of
     *   EMAIL_WRONG
     *   PSSWD_WRONG
     *   ACC_CLOSED ...
     *
     * User#hasValidCredentials, Customer#isValidForInput for further reference
     *
     * TODO: a worth applicant to make it enum
     */
    String errType;

    /**
     * User role can be Admin or Customer
     *
     * Customer extends User
     * as it implies more use cases in addition to authentication
     */
    UserRole role;

    /**
     * User cannot login if enabled is false (account was disabled)
     *
     * errType in this particular case is ACC_CLOSED
     *
     * boolean vs Boolean
     *   JDBC methods take and produce boolean primitive type,
     *   thus, boolean will avoid unnecessary autoboxing
     */
    boolean enabled;

    public static class UserBuilder<T extends UserBuilder<T>> implements Builder<User> {
        // required fields
        String email;

        // optional fields with their default values
        long id;
        String password;
        String errType = null;
        UserRole role;
        boolean enabled = true;

        public UserBuilder(String email) {
            this.email = email;
        }

        public T withId(long id) {
            this.id = id;
            return (T) this;
        }
        public T withPassword(String password) {
            this.password = password;
            return (T) this;
        }
        public T withErrType(String errType) {
            this.errType = errType;
            return (T) this;
        }
        public T withRole(UserRole role) {
            this.role = role;
            return (T) this;
        }
        public T enabled(boolean enabled) {
            this.enabled = enabled;
            return (T) this;
        }
        public T enabled() {
            this.enabled = true;
            return (T) this;
        }
        public T disabled() {
            this.enabled = false;
            return (T) this;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }

    /**
     * Default constructor is required for subclasses
     */
    protected User() { }

    User(UserBuilder builder) {
        email = builder.email;
        password = builder.password;
        errType = builder.errType;
        role = builder.role;
        enabled = builder.enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasValidPassword(String hashFromDb) {
        return BCrypt.checkpw(password, hashFromDb);
    }

    /**
     * Customer password is not required for view purposes
     */
    public User withoutPassword() {
        password = null;
        return this;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) { this.role = role; }

    public boolean isCustomer() {
        return role.equals(CUSTOMER);
    }

    public boolean isAdmin() {
        return role.equals(ADMIN);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User withSwappedStatus() {
        enabled = ! enabled;
        return this;
    }

    public String getErrType() {
        return errType;
    }

    public void setErrType(String errType) {
        this.errType = errType;
    }

    /**
     * Validate user before login and set an appropriate errType
     */
    public boolean hasValidCredentials(User shouldBe) {
        if (shouldBe == null)
            errType = "EMAIL_WRONG";
        else if (! hasValidPassword(shouldBe.getPassword()))
            errType = "PSSWD_WRONG";
        else if ( ! shouldBe.isEnabled())
            errType = "ACC_CLOSED";
        else return true;   // login succeeded
        return false;
    }

    /**
     * Validate new Customer before persisting in database.
     *
     * Is required only in the case JS was disabled.
     * Otherwise, validation will be performed on client side
     * and it's redundant to make an extra call to isValidForInput.
     */
    public boolean isValidForInput(String psswd2) {
        if (email == null || email.isEmpty())
            errType = "EMAIL_EMPTY";
        else if (! email.matches("\\S+@\\w+\\.\\w+"))
            errType = "EMAIL_WRONG";
        else if (password == null || password.isEmpty())
            errType = "PSSWD_EMPTY";
        else if (password.length() < 8)
            errType = "PSSWD_WEAK";
        else if (! password.equals(psswd2))
            errType = "PSSWD2_WRONG";
        else return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email != null ? email.equals(user.email) : user.email == null;

    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}