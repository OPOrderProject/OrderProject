package kr.co.order.app.domain.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.order.app.domain.user.application.port.in.UserUseCase;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.domain.user.presentation.dto.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "사용자 관련 서비스")
public class UserController {
    private final UserUseCase userUseCase;

    @PostMapping("/save")
    @Operation(summary = "사용자 등록", description = "사용자 정보를 입력 받아 저장")
    public ResponseEntity<User> save(@Valid @RequestBody CreateUserDTO createUserDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userUseCase.save(createUserDTO));
    }
}
