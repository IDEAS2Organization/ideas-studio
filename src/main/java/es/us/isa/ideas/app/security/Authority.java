package es.us.isa.ideas.app.security;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

@Embeddable
@Access(AccessType.PROPERTY)
public class Authority implements GrantedAuthority {

    // Constructors -----------------------------------------------------------
    private static final long serialVersionUID = 1L;

    public Authority() {
        super();
    }
    // Values -----------------------------------------------------------------
    public static final String ADMIN = "ADMIN";
    public static final String RESEARCHER = "RESEARCHER";
    public static final String REVIEWER = "REVIEWER";
    // Attributes -------------------------------------------------------------
    private String authority;

    @NotBlank
    @Pattern(regexp = "^" + ADMIN + "|" + RESEARCHER + "|" + REVIEWER + "$")
    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public static Collection<Authority> listAuthorities() {
        Collection<Authority> result;
        Authority authority;

        result = new ArrayList<Authority>();

        authority = new Authority();
        authority.setAuthority(ADMIN);
        result.add(authority);

        authority = new Authority();
        authority.setAuthority(RESEARCHER);
        result.add(authority);

        authority = new Authority();
        authority.setAuthority(REVIEWER);
        result.add(authority);

        return result;
    }

    // Equality ---------------------------------------------------------------
    @Override
    public int hashCode() {
        return this.getAuthority().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        boolean result;

        if (this == other) {
            result = true;
        } else if (other == null) {
            result = false;
        } else if (!this.getClass().isInstance(other)) {
            result = false;
        } else {
            result = (this.getAuthority().equals(((Authority) other)
                    .getAuthority()));
        }

        return result;
    }

    public static Authority get(String authority) {
        Authority result = null;
        if (ADMIN.equalsIgnoreCase(authority)) {
            result =new Authority();
            result.setAuthority(ADMIN);
        } else if (RESEARCHER.equalsIgnoreCase(authority)) {
            result=new Authority();
            result.setAuthority(RESEARCHER);
        } else if (REVIEWER.equalsIgnoreCase(authority)) {
            result=new Authority();
            result.setAuthority(REVIEWER);
        }
        return result;
    }
}
