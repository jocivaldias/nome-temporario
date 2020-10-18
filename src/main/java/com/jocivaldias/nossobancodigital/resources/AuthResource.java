package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.dto.CadastrarSenhaDTO;
import com.jocivaldias.nossobancodigital.dto.SolicitarTokenDTO;
import com.jocivaldias.nossobancodigital.security.JWTUtil;
import com.jocivaldias.nossobancodigital.security.UserSS;
import com.jocivaldias.nossobancodigital.services.AuthService;
import com.jocivaldias.nossobancodigital.services.UserService;
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

    private AuthService authService;

    @Autowired
    public AuthResource(JWTUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @RequestMapping(value="/solicitar-token", method= RequestMethod.POST)
    public ResponseEntity<Void> solicitarToken(@Valid @RequestBody SolicitarTokenDTO solicitarTokenDTO){
        authService.solicitarToken(solicitarTokenDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/cadastrar-senha", method= RequestMethod.POST)
    public ResponseEntity<Void> cadastrarSenha(@Valid @RequestBody CadastrarSenhaDTO cadastrarSenhaDTO){
        authService.cadastrarSenha(cadastrarSenhaDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/renovar-token", method= RequestMethod.POST)
    public ResponseEntity<Void> renovarToken(HttpServletResponse response){
        UserSS user = UserService.authenticated();
        String token = jwtUtil.generateToken(user.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/esqueceu-senha", method= RequestMethod.POST)
    public ResponseEntity<Void> esqueceuSenha(@Valid @RequestBody SolicitarTokenDTO solicitarTokenDTO){
        authService.solicitarToken(solicitarTokenDTO);
        return ResponseEntity.noContent().build();
    }

}
