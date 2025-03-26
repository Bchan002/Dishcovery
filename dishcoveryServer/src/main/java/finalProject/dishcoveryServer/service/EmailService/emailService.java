package finalProject.dishcoveryServer.service.EmailService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import finalProject.dishcoveryServer.models.User;
import finalProject.dishcoveryServer.models.GroceryItem.GroceryItem;
import finalProject.dishcoveryServer.models.exception.EmailNotFoundException;
import finalProject.dishcoveryServer.repository.userRepository;

@Service
public class emailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private userRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String sender;

    private String userEmail;

    public String checkEmail(String email) {


        String newPassword = "";
        // Once Request -> Check whether the email exists in database
        boolean emailExists = userRepo.findEmailExists(email);

        if (emailExists) {
            this.userEmail = email;
            System.out.println("Your email is " + this.userEmail);
            // Get the username
            String username = userRepo.getUsername(email);

            // Generate a new random Password
            newPassword = UUID.randomUUID().toString().substring(0, 7);
            // System.out.println("Your new Password is " + newPassword);
            // Endcode the password
            String newPasswordEncode = passwordEncoder.encode(newPassword);

            // Update the new password in the database
            boolean updatePassword = userRepo.updatePassword(email, newPasswordEncode);

            if (!updatePassword) {
                throw new EmailNotFoundException("Password update unsuccessful!!");
            }

            // Update successful -> send the randomPassword to the user email

            try {

                SimpleMailMessage mailMessage = new SimpleMailMessage();

                mailMessage.setFrom(sender);
                mailMessage.setTo(email);
                String text = "Hello " + username + " " + """
                                this is your new password,please use this password to login and reset
                                    """;
                mailMessage.setText(text + newPassword);

                javaMailSender.send(mailMessage);
                return "Mail Sent Successfully!";

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                throw new EmailNotFoundException("There is an Error! Please check your email again!");
            }

        }
        throw new EmailNotFoundException("There is an Error! Please check your email again!");

    }


    public void sendEmail(List<GroceryItem> expiredItems, String userId) {
        try {

            //Get the email 
            String email = this.userRepo.getEmail(userId);

            System.out.println("Your email is " + email);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
    
            StringBuilder message = new StringBuilder();
            message.append("Hello,\n\n");
            message.append("The following grocery items have expired:\n\n");
    
            for (GroceryItem item : expiredItems) {
                message.append("- ")
                       .append(item.getItemName())
                       .append(" (Expired on: ")
                       .append(item.getExpirationDate()) // or format this date
                       .append(")\n");
            }
    
            message.append("\nPlease check your inventory.\n");
            message.append("\nBest regards,\nDishcovery 🍽️");
    
            mailMessage.setText(message.toString());
            javaMailSender.send(mailMessage);
    
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailNotFoundException("There is an Error! Please check your email again!");
        }
    }
    


}
