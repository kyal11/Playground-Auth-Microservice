package com.microservice.users.infrastructure.OAuth2;

import com.microservice.users.domain.service.AuthService;
import com.microservice.users.dto.ApiResponse;
import com.microservice.users.dto.auth.request.LoginWithOauthReq;
import com.microservice.users.dto.auth.response.LoginRes;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class OAuthHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");
        LoginWithOauthReq req = new LoginWithOauthReq();
        req.setEmail(email);
        req.setName(name);
        req.setAvatarUrl(picture);
        req.setProviderId(providerId);

        LoginRes res = authService.registerOrLoginGoogle(req).getData();

        String token = URLEncoder.encode(res.getToken(), StandardCharsets.UTF_8);
        response.sendRedirect("/home.html?token=" + token);
    }
}
