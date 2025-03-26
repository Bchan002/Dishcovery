package finalProject.dishcoveryServer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import finalProject.dishcoveryServer.models.User;
import finalProject.dishcoveryServer.repository.userRepository;

@Service
public class ChangePasswordService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private userRepository userRepo;

    public void changePassword(String username, String newChangePassword){

        //Get the user details
        Optional<User> user= userRepo.findByUserName(username);

        //Take the password out 
        // 1. Decrpyt the password 
        if(user.isPresent()){

            User userDetails = user.get();

            String email = userDetails.getEmail();

            // Endcode the password
            String newPasswordEncode = passwordEncoder.encode(newChangePassword);

            // Update the new password in the database
            boolean updatePassword = userRepo.updatePassword(email, newPasswordEncode);
        }
        

    }
}
