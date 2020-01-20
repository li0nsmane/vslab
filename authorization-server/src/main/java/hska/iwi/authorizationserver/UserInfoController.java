package hska.iwi.authorizationserver;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoController {
    @RequestMapping(value = "/user")
    public Principal userInfo(@AuthenticationPrincipal Principal user) {
        return user;
    }
}
