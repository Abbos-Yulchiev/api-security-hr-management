package uz.pdp.apisecurityhrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue()
    private UUID id; //user's unique id

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email; //user's email(used like an username)

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt; //Registered time

    @UpdateTimestamp
    private Timestamp updateAt; //Last active time

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private boolean accountNonExpired = true; //user expired date available

    private boolean accountNonLocked = true; // user unblocked;

    private boolean credentialsNonExpired = true; //

    private boolean enabled = false; //userni tizimdan foydalana olishi

    private String emailCode;

    private String position;

    /**
     * UserDetails' methods
     */

    /*Userning huquqlari*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    /*Usrening usernamini qytaruvchi methodi*/
    @Override
    public String getUsername() {
        return this.email;
    }

    /*Userning amal qilish mudatini qaytaruvchi methodi*/
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    /*Accountni bloklanmaganligini qaytaradi*/
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    /*Accountni ishonchliligini qaytaradi*/
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    /*Accountni active actieligini ifodalaydi*/
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


}
