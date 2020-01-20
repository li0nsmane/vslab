package hska.iwi.authorizationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

        public ShopUser getUserByUsername(String username) {
            return restTemplate.getForObject("http://user-service:8003/users/" + username, ShopUser.class);
        }


}
