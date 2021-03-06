package cs203t10.quadrate.security.auth;

import cs203t10.quadrate.security.jwt.JwtTokenUtil;
import cs203t10.quadrate.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authenticate")
@CrossOrigin
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final AuthService authService;

    @PostMapping
    public JwtResponse AuthenticateUser(@RequestBody JwtRequest authenticationRequest) throws Exception
    {
        String username = authenticationRequest.getUsername();
        authService.authenticate(username, authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        final String token = jwtTokenUtil.generateJwtToken(userDetails);
        return new JwtResponse(username, token);
    }




}
