import React, { useState } from "react";
import styled from "styled-components";
import api from "../../../api/axios";
import { Link, useNavigate } from "react-router-dom";
// import FacebookButton from "../ui/FacebookButton"; // 이 컴포넌트는 사용자의 프로젝트에 맞게 구성 필요

// 인스타그램 컨테이너 스타일
const Container = styled.div`
    display: flex;
    flex-direction: column;
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
    margin-bottom: 10px;
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
    box-sizing: border-box;

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

// 구분선 스타일
const Separator = styled.div`
    display: flex;
    align-items: center;
    text-align: center;
    margin: 15px 0;
    width: 100%;

    &::before,
    &::after {
        content: "";
        flex: 1;
        border-bottom: 1px solid #dbdbdb;
    }

    &::before {
        margin-right: 0.25em;
    }

    &::after {
        margin-left: 0.25em;
    }
`;

const SeparatorText = styled.span`
    font-size: 13px;
    color: #8e8e8e;
    font-weight: 600;
`;

// 하단 링크 박스 스타일
const LinkBox = styled(FormWrapper)`
    margin-top: 0;
    padding: 20px;
    width: 350px;
    font-size: 14px;
    text-align: center;
    display: block;
`;

const SignUpLinkText = styled.span`
    a {
        color: #0095f6;
        text-decoration: none;
        font-weight: 600;
        &:hover {
            text-decoration: underline;
        }
    }
`;

// 기타 텍스트 링크
const OtherLinkText = styled.p`
    font-size: 12px;
    color: #00376b;
    margin-top: 12px;
    text-align: center;
    cursor: pointer;
    font-weight: 600;
    &:hover {
        text-decoration: underline;
    }
`;

const SignInPage = () => {
    const [email, setEmail] = useState("");
    const [passwd, setPasswd] = useState("");
    const moveUrl = useNavigate();

    // 입력 필드가 모두 채워졌는지 확인하는 상태
    const isFormValid = email && passwd;

    const handleSubmit = async (e) => {
        e.preventDefault();

        const data = { email, passwd };

        try {
            const response = await api.get("/api/v2/inspire/user/signin", {
                params: data,
            });

            // API 호출 성공 시
            localStorage.setItem(
                "accessToken",
                response.headers.get("authorization")
            );
            localStorage.setItem(
                "refreshToken",
                response.headers.get("refresh-token")
            );
            localStorage.setItem("userInfo", response.data.name);
            localStorage.setItem("userEmail", response.data.email);
            localStorage.setItem("userImageUrl", response.data.userImageUrl);

            moveUrl("/main");
        } catch (error) {
            console.error("[debug] >>> post error", error);
            alert(
                "로그인에 실패했습니다. 이메일과 비밀번호를 다시 확인해주세요."
            );
        }
    };

    return (
        <Container>
            <div>
                <FormWrapper>
                    <Title>Instagram</Title>
                    <Input
                        type="email"
                        name="email"
                        placeholder="전화번호, 사용자 이름 또는 이메일"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <Input
                        type="password"
                        name="password"
                        placeholder="비밀번호"
                        value={passwd}
                        onChange={(e) => setPasswd(e.target.value)}
                        required
                    />
                    <Button
                        type="submit"
                        onClick={handleSubmit}
                        disabled={!isFormValid}
                    >
                        로그인
                    </Button>
                    <Separator>
                        <SeparatorText>또는</SeparatorText>
                    </Separator>
                    {/* <FacebookButton /> */}
                    <OtherLinkText>비밀번호를 잊으셨나요?</OtherLinkText>
                </FormWrapper>
                <LinkBox>
                    계정이 없으신가요?{" "}
                    <SignUpLinkText>
                        <Link to="/">가입하기</Link>
                    </SignUpLinkText>
                </LinkBox>
            </div>
        </Container>
    );
};

export default SignInPage;
