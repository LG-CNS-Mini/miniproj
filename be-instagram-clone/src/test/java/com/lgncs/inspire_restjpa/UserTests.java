package com.lgncs.inspire_restjpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import com.lgncs.inspire_restjpa.user.domain.dto.UserRequestDTO;
import com.lgncs.inspire_restjpa.user.domain.dto.UserResponseDTO;
import com.lgncs.inspire_restjpa.user.domain.entity.UserEntity;
import com.lgncs.inspire_restjpa.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class UserTests {
    
    @Autowired
    private UserRepository userRepository ; 

    
    @Test
    @Transactional
    @Commit
    public void insertUser() {
        // givn
        UserRequestDTO request = UserRequestDTO.builder()
                                    .email("jslim9413@naver.com")
                                    .passwd("1234")
                                    .name("임섭순")
                                    .build() ;
        // when
        UserEntity entity = userRepository.save(request.toEntity());
        
        UserResponseDTO response = UserResponseDTO.fromEntity(entity);
        
        // then
        System.out.println(">>>> entity   "+entity);
        System.out.println(">>>> response "+response);  
         
    }
}
