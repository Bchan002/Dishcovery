package finalProject.dishcoveryServer.service.authenticationService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import finalProject.dishcoveryServer.models.User;
import finalProject.dishcoveryServer.models.UserInfoDetails;
import finalProject.dishcoveryServer.service.userService;

@Component
public class UserInfoService implements UserDetailsService{
    
    @Autowired
    private userService userSvc;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetails = userSvc.findByUsername(username);

        //Map to spring security user
        return userDetails.map(UserInfoDetails::new) 
            .orElseThrow( ()-> new UsernameNotFoundException("User not found " + username));

    }

}
