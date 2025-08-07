package ATORY.atory.domain.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class LoginController {
    
    @Value("${google.client.id}")
    private String googleClientId;
    
    @Value("${google.client.pw}")
    private String googleClientPw;

    @RequestMapping(value="/api/v1/oauth2/google", method = RequestMethod.POST)
    public String loginUrlGoogle(){
        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?client_id=" + googleClientId
                + "&redirect_uri=https%3A%2F%2Foauth.pstmn.io%2Fv1%2Fcallback&response_type=code&scope=openid%20email%20profile&access_type=offline&prompt=consent&service=lso&o2v=2&flowName=GeneralOAuthFlow";
        return reqUrl;
    }
} 