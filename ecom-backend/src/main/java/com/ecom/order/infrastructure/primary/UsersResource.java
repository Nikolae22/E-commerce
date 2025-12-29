package com.ecom.order.infrastructure.primary;

import com.ecom.order.application.UsersApplicationService;
import com.ecom.order.domain.user.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersResource {

    private final UsersApplicationService usersApplicationService;

    @GetMapping("/authenticated")
    public ResponseEntity<RestUser> getAuthenticatedUser(
            @AuthenticationPrincipal Jwt jwtToken,
            @RequestParam boolean forceResync
            ){
        User authenticatedUserWithSync = usersApplicationService.getAuthenticatedUserWithSync(jwtToken, forceResync);
        RestUser restUser = RestUser.from(authenticatedUserWithSync);
        return ResponseEntity.ok(restUser);
    }
}
