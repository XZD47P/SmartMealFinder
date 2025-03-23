package hu.project.smartmealfinderb.Oauth2;

import hu.project.smartmealfinderb.Model.AppRole;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.RoleRepository;
import hu.project.smartmealfinderb.Security.JWT.JwtUtils;
import hu.project.smartmealfinderb.Security.Service.UserDetailsImpl;
import hu.project.smartmealfinderb.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${frontend.url}")
    private String frontendURL;

    private String username;
    private String idAttributeKey;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("github") || oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("google")) {
            //Mivel nem standard a "name" megszerzése, ezért kell egy key, ahonnan ki tudja olvasni a nevet,
            //ezért kell a DefaultOAuth2User-t beállítani
            DefaultOAuth2User principal = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();

            //A Provider szerinti beállítás
            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("github")) {
                this.username = attributes.getOrDefault("login", "").toString();
                this.idAttributeKey = "id";
            } else if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("google")) {
                this.username = email.split("@")[0];
                idAttributeKey = "sub";
            } else {
                username = "";
                idAttributeKey = "id";
            }

            //Vizsgáljuk, hogy az adott felhasználó létezik-e, ha nem, akkor létrehozzuk
            this.userService.findByEmail(email)
                    .ifPresentOrElse(user -> { //Létező felhasználó esetén beállítjuk a SecurityContextet
                                DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                                        List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                                        attributes,
                                        idAttributeKey
                                );
                                Authentication securityAuth = new OAuth2AuthenticationToken(
                                        oAuth2User,
                                        List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                                );
                                SecurityContextHolder.getContext().setAuthentication(securityAuth);
                            }, () -> {
                                User newUser = new User();
                                Optional<Role> userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER); //Alapértelmezett "user" role adás
                                if (userRole.isPresent()) {
                                    newUser.setRole(userRole.get());
                                } else {
                                    throw new RuntimeException("Default role not found");
                                }
                                newUser.setEmail(email);
                                newUser.setUserName(username);
                                newUser.setSignUpMethod(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());

                                this.userService.registerUser(newUser);

                                //SecurityContext beállítása
                                DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                                        List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                                        attributes,
                                        idAttributeKey
                                );
                                Authentication securityAuth = new OAuth2AuthenticationToken(
                                        oAuth2User,
                                        List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                                );
                                SecurityContextHolder.getContext().setAuthentication(securityAuth);
                            }
                    );
        }
        this.setAlwaysUseDefaultTargetUrl(true);

        //JWT token generálása az adott felhasználóhoz

        //Felhasználó adatainak begyűjtése
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        //Roleok megszerzése
        Set<SimpleGrantedAuthority> authorities = new HashSet<>(oAuth2User.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList()));

        User user = this.userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName().name()));

        //UserDetailsImpl példány létrehozása
        UserDetailsImpl userDetails = new UserDetailsImpl(
                null,
                username,
                email,
                null,
                true,
                authorities
        );

        //JWT Token generálás
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        //Visszaküldés a frontendnek
        String targetURL = UriComponentsBuilder.fromUriString(this.frontendURL + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build().toUriString();
        this.setDefaultTargetUrl(targetURL);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
