package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.dto.CadastrarSenhaDTO;
import com.jocivaldias.nossobancodigital.dto.SolicitarTokenDTO;
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

    private AuthService authService;

    @Autowired
    public AuthResource(JWTUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @ApiOperation(value = "Cliente solicita um token para cadastrar uma nova senha")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Envia um token ao e-mail do cliente"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value="/solicitar-token", method= RequestMethod.POST)
    public ResponseEntity<Void> solicitarToken(@Valid @RequestBody SolicitarTokenDTO solicitarTokenDTO){
        authService.solicitarToken(solicitarTokenDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Cliente cadastra uma nova senha")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Senha criada e e-mail enviado ao cliente"),
            @ApiResponse(code = 400, message = "Token já expirado ou inválido"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value="/cadastrar-senha", method= RequestMethod.POST)
    public ResponseEntity<Void> cadastrarSenha(@Valid @RequestBody CadastrarSenhaDTO cadastrarSenhaDTO){
        authService.cadastrarSenha(cadastrarSenhaDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Cliente renova o token de acesso ao sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Token é enviado no header da resposta"),
            @ApiResponse(code = 403, message = "Token já expirado ou inválido"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value="/renovar-token", method= RequestMethod.POST)
    public ResponseEntity<Void> renovarToken(HttpServletResponse response){
        UserSS user = UserService.authenticated();
        String token = jwtUtil.generateToken(user.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Cliente solicita uma nova senha")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Senha criada e e-mail enviado ao cliente"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value="/esqueceu-senha", method= RequestMethod.POST)
    public ResponseEntity<Void> esqueceuSenha(@Valid @RequestBody SolicitarTokenDTO solicitarTokenDTO){
        authService.solicitarToken(solicitarTokenDTO);
        return ResponseEntity.noContent().build();
    }

}
