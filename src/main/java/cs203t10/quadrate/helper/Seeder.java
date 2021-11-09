package cs203t10.quadrate.helper;

import cs203t10.quadrate.user.User;
import cs203t10.quadrate.user.UserService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class Seeder {
    private final UserService userService;

    public Seeder(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    @Order(0)
    public void createGenesisOnStart(ApplicationStartedEvent event){
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");
        user.setRole("ROLE_ADMIN");
        userService.createUser(user);
    }
}