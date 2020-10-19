package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.services.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/accounts")
public class AccountResource {

    private final AccountService service;

    @Autowired
    public AccountResource(AccountService service) {
        this.service = service;
    }

    @ApiOperation(value = "Returns the account of the authenticated client on the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account returned in response"),
            @ApiResponse(code = 403, message = "Unauthenticated client"),
            @ApiResponse(code = 404, message = "Account not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> findAccount(@PathVariable Integer id) {
        Account obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

}
