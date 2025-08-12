package ATORY.atory.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "oauth.google")
public class GoogleOAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri = "https://oauth2.googleapis.com/token";
    private String userInfoUri = "https://www.googleapis.com/oauth2/v3/userinfo";

    public void setClientId(String v) { this.clientId = v; }
    public void setClientSecret(String v) { this.clientSecret = v; }
    public void setRedirectUri(String v) { this.redirectUri = v; }
    public void setTokenUri(String v) { this.tokenUri = v; }
    public void setUserInfoUri(String v) { this.userInfoUri = v; }
}