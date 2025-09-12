import React, { useState } from "react";
import styled from "styled-components";
import api from "../../../api/axios";
import { Link, useNavigate } from "react-router-dom";

// Container
const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
`;

// Form Box
const FormWrapper = styled.div`
  background-color: #fff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
  width: 350px;
`;

// Title
const Title = styled.h2`
  text-align: center;
  margin-bottom: 25px;
  color: #333;
`;

// Input
const Input = styled.input`
  width: 100%;
  padding: 12px;
  margin-bottom: 15px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 16px;

  &:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 5px rgba(0,123,255,0.3);
  }
`;

// Button
const Button = styled.button`
  width: 100%;
  padding: 12px;
  background-color: #007bff;
  color: white;
  border: none;
  font-size: 16px;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 10px;

  &:hover {
    background-color: #0056b3;
  }

  &:disabled {
    background-color: #aaa;
    cursor: not-allowed;
  }
`;

// Link
const TextLink = styled.p`
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
  color: #007bff;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
`;

const LoginPage = () => {

  const access  = localStorage.getItem("accessToken");
  const refresh = localStorage.getItem("refreshToken");
  
  console.log("[debug] LoginPage token acc : " , access);
  console.log("[debug] LoginPage token ref : " , refresh);
  
  const [email, setEmail]     = useState('');
  const [passwd, setPasswd]   = useState('');

  const moveUrl = useNavigate();

  
  const handleSubmit = async (e, email, passwd) => {
  
    
    // API 호출 가능
    const data = {email, passwd} ;
    console.log(">>>>>>>>>>> LoginPage handleSubmit :");
    
    await api.get('/api/v2/inspire/user/signin' , {params :  data} )
        .then( response => {
            
            console.log("[debug] >>> post response : " , response ); 
            console.log("accessToken", response.headers.get("authorization"));
            console.log("refreshToken", response.headers.get("refresh-token"));
            // token
            localStorage.setItem("accessToken", response.headers.get("authorization"));
            localStorage.setItem("refreshToken", response.headers.get("refresh-token"));
            // user 
            localStorage.setItem("userInfo" , response.data.name) ;
            localStorage.setItem("userEmail" , response.data.email) ;
            

            moveUrl('/blog');
        })
        .catch( error => {
            console.log("[debug] >>> post error");
        });
  };

  return (
    <Container>
      <FormWrapper>
        <Title>로그인</Title>
        
          <Input
            type="email"
            name="email"
            placeholder="이메일"
            value={email}
            onChange={ (e) => {
              setEmail(e.target.value);
            } }
            required
          />
          <Input
            type="password"
            name="password"
            placeholder="비밀번호"
            value={passwd}
            onChange={ (e) => {
              setPasswd(e.target.value);
            } }
            required
          />
          <Button type="button"
                  onClick={(e) => handleSubmit(e, email, passwd) }>로그인</Button>
        
        <TextLink>비밀번호를 잊으셨나요?</TextLink>
        <TextLink><Link to="/">회원가입</Link></TextLink>
      </FormWrapper>
    </Container>
  );
};

export default LoginPage;