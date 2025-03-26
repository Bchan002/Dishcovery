package finalProject.dishcoveryServer.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserInfoDetails implements UserDetails {
    
    private String username; 
    private String password;
    private List<GrantedAuthority> authorities;
    
    public UserInfoDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        
        // If user has roles, use them; otherwise, assign a default ROLE_USER
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            this.authorities = Arrays.stream(user.getRoles().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            // Assign default role
            this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // @Override
    // public boolean isAccountNonExpired() {
    //     return true; // Implement your logic if you need this
    // }

    // @Override
    // public boolean isAccountNonLocked() {
    //     return true; // Implement your logic if you need this
    // }

    // @Override
    // public boolean isCredentialsNonExpired() {
    //     return true; // Implement your logic if you need this
    // }

    // @Override
    // public boolean isEnabled() {
    //     return true; // Implement your logic if you need this
    // }

}
