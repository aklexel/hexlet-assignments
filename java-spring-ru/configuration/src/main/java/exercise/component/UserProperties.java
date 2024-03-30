package exercise.component;

import exercise.model.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "users")
@Getter
@Setter
public class UserProperties {
    private List<User> admins;
}
