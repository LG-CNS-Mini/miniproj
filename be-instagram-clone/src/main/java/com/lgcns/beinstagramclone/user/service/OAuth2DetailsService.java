package com.lgcns.beinstagramclone.user.service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.UserRepository;
import com.lgcns.beinstagramclone.util.CustomOAuth2User;

@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        try {
            OAuth2User oauth2User = super.loadUser(userRequest);

            Map<String, Object> userInfo = oauth2User.getAttributes();
            System.out.println("[debug]>>> OAuth2 User Info: " + userInfo); 
            
            //String username = "facebook_"+(String) userInfo.get("id");
            //String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
            String password = "Face" + userInfo.get("id").toString();
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");
            
            Optional<UserEntity> userEntity = userRepository.findByEmail(email);
            
            if(userEntity.isEmpty()) { // 회원이 아닐시
                System.out.println("[debug]>>> OAuth2 회원 가입"); 
                UserEntity user = UserEntity.builder()
                        .passwd(password)
                        .email(email)
                        .name(name)
                        .nickname(name+randomNumberString())
                        .build();
                userRepository.save(user);
                return new CustomOAuth2User(user, oauth2User.getAttributes());
            }else { // 회원일시
                System.out.println("[debug]>>> OAuth2 회원 로그인"); 
                return new CustomOAuth2User(userEntity.get(), oauth2User.getAttributes());
            }
        }
        catch(Exception e) {    
            System.out.println(">>> loadUser method failed: "+e);
        }
		
        return null;
	
	}

    public String randomNumberString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10); // 10자리를 담을 공간을 미리 만들어 줘.
        
        // 첫 번째 숫자는 0이 오면 안 되니까 1부터 9 사이에서 선택해.
        sb.append(random.nextInt(9) + 1);
        
        // 나머지 9자리는 0부터 9 사이에서 랜덤하게 선택해.
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
}