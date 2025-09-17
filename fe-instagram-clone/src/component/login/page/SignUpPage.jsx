import React, { useState } from "react";
import styled from "styled-components";
import api from "../../../api/axios";
import { Link, useNavigate } from "react-router-dom";

// 인스타그램 컨테이너 스타일
const Container = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #fafafa;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
        "Helvetica Neue", Arial, sans-serif;
`;

// 인스타그램 폼 박스 스타일
const FormWrapper = styled.div`
    background-color: white;
    border: 1px solid #dbdbdb;
    border-radius: 1px;
    padding: 20px 40px;
    width: 350px;
    display: flex;
    flex-direction: column;
    align-items: center;
`;

// 인스타그램 로고나 제목 스타일
const Title = styled.h1`
    font-family: "Billabong", "Segoe UI", serif; /* 인스타그램 로고 폰트 대체 */
    font-size: 50px;
    margin: 22px auto 12px;
    text-align: center;
    color: #262626;
    font-weight: 400;
`;

// 인풋 스타일
const Input = styled.input`
    width: 100%;
    padding: 9px 8px;
    margin-bottom: 8px;
    background-color: #fafafa;
    border: 1px solid #dbdbdb;
    border-radius: 3px;
    font-size: 14px;
    box-sizing: border-box; /* 패딩이 너비에 포함되도록 */

    &:focus {
        outline: none;
        border-color: #a8a8a8;
    }
`;

// 버튼 스타일
const Button = styled.button`
    width: 100%;
    padding: 8px;
    background-color: #0095f6;
    color: white;
    border: none;
    font-size: 14px;
    font-weight: 600;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 8px;

    &:hover {
        background-color: #007bb6;
    }

    &:disabled {
        background-color: #b2dffc;
        cursor: not-allowed;
    }
`;

// 하단 링크 박스 스타일
const LinkBox = styled(FormWrapper)`
    margin-top: 10px;
    padding: 20px;
    width: 350px;
    font-size: 14px;
    text-align: center;
`;

const LinkText = styled.span`
    a {
        color: #0095f6;
        text-decoration: none;
        font-weight: 600;
        &:hover {
            text-decoration: underline;
        }
    }
`;

// SignUp Component
const SignUpPage = () => {
    const [email, setEmail] = useState("");
    const [passwd, setPasswd] = useState("");
    const [confirmPasswd, setConfirmPasswd] = useState("");
    const [name, setName] = useState("");
    const [nickname, setNickname] = useState("");

    const moveUrl = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (passwd !== confirmPasswd) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        console.log("회원가입 정보:", { email, passwd, name, nickname });

        const data = { email, passwd, name, nickname };
        try {
            const response = await api.post(
                "/api/v2/inspire/user/signup",
                data
            );
            console.log("[debug] >>> post response:", response);
            moveUrl("/signin");
        } catch (error) {
            console.log("[debug] >>> post error:", error);
            // 에러 처리 로직 추가 (예: alert로 에러 메시지 표시)
            alert("회원가입에 실패했습니다. 다시 시도해주세요.");
        }
    };

    const isFormValid =
        email &&
        passwd &&
        confirmPasswd &&
        name &&
        nickname &&
        passwd === confirmPasswd;

    return (
        <Container>
            <div>
                <FormWrapper>
                    <Title>Instagram</Title>
                    <Input
                        type="email"
                        name="email"
                        placeholder="이메일"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <Input
                        type="password"
                        name="passwd"
                        placeholder="비밀번호"
                        value={passwd}
                        onChange={(e) => setPasswd(e.target.value)}
                        required
                    />
                    <Input
                        type="password"
                        name="confirmPasswd"
                        placeholder="비밀번호 확인"
                        value={confirmPasswd}
                        onChange={(e) => setConfirmPasswd(e.target.value)}
                        required
                    />
                    <Input
                        type="text"
                        name="name"
                        placeholder="이름"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                    <Input
                        type="text"
                        name="nickname"
                        placeholder="닉네임"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                        required
                    />
                    <Button
                        type="submit"
                        onClick={handleSubmit}
                        disabled={!isFormValid}
                    >
                        가입하기
                    </Button>
                </FormWrapper>
                <LinkBox>
                    <LinkText>
                        계정이 있으신가요? <Link to="/signin">로그인</Link>
                    </LinkText>
                </LinkBox>
            </div>
        </Container>
    );
};

export default SignUpPage;
