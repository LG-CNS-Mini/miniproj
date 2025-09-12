package com.lgncs.inspire_restjpa.user.domain.dto;


import com.lgncs.inspire_restjpa.user.domain.entity.UserEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequestDTO {
    
    @Email(message = "이메일 형식과 맞지 않습니다.")
    private String email ; 
    
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$", message="패스워드 정책에 맞지 않습니다.")
    private String passwd;
    
    @NotNull(message = "이름을 입력해 주세요.")
    private String name  ;
    
    

    // factory method pattern 
    // dto -> entity 
    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .passwd(this.passwd)
                .name(this.name)
                .build() ; 
    }
}

