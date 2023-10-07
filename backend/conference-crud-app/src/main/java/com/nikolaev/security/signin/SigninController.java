package com.nikolaev.security.signin;

import com.nikolaev.security.JwtTokenUtil;
import com.nikolaev.security.JwtUser;
import com.nikolaev.security.service.JwtAuthenticationRequest;
import com.nikolaev.security.service.JwtAuthenticationResponse;
import com.nikolaev.user.User;
import com.nikolaev.user.UserService;
import com.nikolaev.user.dto.UserMapper;
import com.nikolaev.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "api/auth/signin")
@RequiredArgsConstructor
public class SigninController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //    @Value("${jwt.header}")
    private String tokenHeader = "authorization";


    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final SigninService signinService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        JwtAuthenticationResponse response = signinService.signin(authenticationRequest);

        // Return the token
        return ResponseEntity.ok(response);
    }

//    @RequestMapping(value = "/confirmation", method = RequestMethod.POST)
//    public ResponseEntity<?> confirmEmail(@RequestBody String token, Device device) throws UserNotFoundException {
//        User user = userService.confirmEmail(token);
//
//        String jwtToken = jwtTokenUtil.generateToken(user, device);
////        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, user.getUsername()));
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));
//
//    }

    @RequestMapping(value = "/confirmation", method = RequestMethod.POST)
    public ResponseEntity<?> confirmEmail(@RequestBody String token) throws UserNotFoundException {
        User user = userService.confirmEmail(token);

        String jwtToken = jwtTokenUtil.generateToken(user);
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, user.getUsername()));
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));

    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
//            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, user.getUsername()));
//            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, user));
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, UserMapper.toDto(user)));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}