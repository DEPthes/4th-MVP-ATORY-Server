package ATORY.atory.domain.user.dto;

import ATORY.atory.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements AuthenticatedPrincipal {
    private User user;

    public User getUser() {
        return user;
    }

    @Override
    public String getName() {
        return "";
    }
}
