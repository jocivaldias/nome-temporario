package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.dto.registerPasswordDTO;
import com.jocivaldias.nossobancodigital.dto.RequestTokenDTO;
import com.jocivaldias.nossobancodigital.security.JWTUtil;
import com.jocivaldias.nossobancodigital.security.UserSS;
import com.jocivaldias.nossobancodigital.services.AuthService;
import com.jocivaldias.nossobancodigital.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value="/auth")
public class AuthResource {

    JWTUtil jwtUtil;

    private final AuthService authService;

    @Autowired
    public AuthResource(JWTUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @ApiOperation(value = "Customer requests a token to register a new password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Send a token to the client's email"),
            @ApiResponse(code = 404, message = "Client not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value="/request-token", method= RequestMethod.POST)
    public ResponseEntity<Void> requestToken(@Valid @RequestBody RequestTokenDTO requestTokenDTO){
        authService.solicitarToken(requestTokenDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Customer registers a new password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password created and email sent to the customer"),
            @ApiResponse(code = 400, message = "Token already expired or invalid"),
            @ApiResponse(code = 404, message = "Client not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value="/create-password", method= RequestMethod.POST)
    public ResponseEntity<Void> createPassword(@Valid @RequestBody registerPasswordDTO registerPasswordDTO){
        authService.savePassword(registerPasswordDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Client renews the system access token")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Token is sent in the response header"),
            @ApiResponse(code = 403, message = "Token already expired or invalid"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value="/renew-token", method= RequestMethod.POST)
    public ResponseEntity<Void> renewToken(HttpServletResponse response){
        UserSS user = UserService.authenticated();
        String token = jwtUtil.generateToken(user.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Customer requests a new password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password created and email sent to the customer"),
            @ApiResponse(code = 404, message = "Client not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value="/forgot-password", method= RequestMethod.POST)
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody RequestTokenDTO requestTokenDTO){
        authService.solicitarToken(requestTokenDTO);
        return ResponseEntity.noContent().build();
    }

}
