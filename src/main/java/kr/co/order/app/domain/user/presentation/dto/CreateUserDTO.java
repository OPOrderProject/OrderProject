package kr.co.order.app.domain.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "username은 필수 입력 항목입니다.")
    private String username;
    @NotBlank(message = "password는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 8자리 이상만 가능합니다.")
    private String password;
    @NotBlank(message = "nickname은 필수 입력 항목입니다.")
    private String nickname;
}
