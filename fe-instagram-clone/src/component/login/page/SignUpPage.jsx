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
    background-color: #fafafa; // 인스타그램 배경색
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
        Helvetica, Arial, sans-serif;
`;

// Form Box
const FormWrapper = styled.div`
    background-color: #fff;
    padding: 30px;
    border-radius: 8px;
    border: 1px solid #dbdbdb; // 인스타그램 스타일의 얇은 경계선
    width: 350px;
    display: flex;
    flex-direction: column;
    align-items: center;
`;

// Title
const InstagramTitle = styled.h1`
    font-family: "Billabong", cursive; // 인스타그램 로고 폰트 (별도 폰트 필요)
    font-size: 50px;
    margin-bottom: 20px;
    color: #262626;
`;

// Description Text
const DescriptionText = styled.p`
    text-align: center;
    color: #8e8e8e;
    font-size: 17px;
    font-weight: 600;
    line-height: 20px;
    margin-bottom: 20px;
`;

// Input
const Input = styled.input`
    width: 100%;
    padding: 12px;
    margin-bottom: 8px;
    border-radius: 4px;
    border: 1px solid #dbdbdb;
    background-color: #fafafa;
    font-size: 14px;
    color: #262626;

    &:focus {
        outline: none;
        border-color: #a8a8a8;
    }
`;

// Button
const Button = styled.button`
    width: 100%;
    padding: 10px;
    // 인스타그램 버튼 그라데이션
    background: linear-gradient(
        to right,
        #405de6,
        #5851db,
        #833ab4,
        #c13584,
        #e1306c,
        #fd1d1d,
        #f56040,
        #ff8c00
    );
    color: white;
    border: none;
    font-size: 14px;
    font-weight: bold;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 10px;

    &:hover {
        opacity: 0.9;
    }

    &:disabled {
        background-color: #b2dffc;
        cursor: not-allowed;
    }
`;

// Link Text
const BottomBox = styled.div`
    background-color: #fff;
    border: 1px solid #dbdbdb;
    padding: 20px;
    margin-top: 10px;
    width: 350px;
    text-align: center;
    border-radius: 8px;
    font-size: 14px;
`;

const StyledLink = styled(Link)`
    text-decoration: none;
    color: #0095f6;
    font-weight: 500;
`;

const SignUpPage = () => {
    const [email, setEmail] = useState("");
    const [passwd, setPasswd] = useState("");
    const [confirmPasswd, setConfirmPasswd] = useState("");
    const [name, setName] = useState("");
    const [nickname, setNickname] = useState("");

    const moveUrl = useNavigate();

    const handleSubmit = async (e) => {
        // 이벤트 객체만 받도록 수정
        e.preventDefault();
        console.log(">>>>>>>>>>> ", email, passwd, name, nickname);

        if (passwd !== confirmPasswd) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        const data = { email, passwd, name, nickname };

        try {
            await api.post("/api/v2/inspire/user/signup", data);
            moveUrl("/signin");
        } catch (error) {
            console.log("[debug] >>> post error :" + error);
            alert("회원가입에 실패했습니다. 입력 정보를 확인해주세요.");
        }
    };

    return (
        <Container>
            <div>
                <FormWrapper>
                    <InstagramTitle>Instagram</InstagramTitle>
                    <DescriptionText>
                        친구들의 사진과 동영상을 보려면 가입하세요.
                    </DescriptionText>
                    {/* <FacebookButton /> // 페이스북 버튼 추가를 원하시면 주석 해제 */}

                    <Input
                        type="email"
                        name="email"
                        placeholder="휴대폰 번호 또는 이메일 주소"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
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
                    <Button
                        type="button"
                        onClick={handleSubmit}
                        disabled={
                            !email ||
                            !passwd ||
                            !confirmPasswd ||
                            !name ||
                            !nickname
                        }
                    >
                        가입하기
                    </Button>
                </FormWrapper>
                <BottomBox>
                    계정이 있으신가요?{" "}
                    <StyledLink to="/signin">로그인</StyledLink>
                </BottomBox>
            </div>
        </Container>
    );
};

export default SignUpPage;
