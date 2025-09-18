import React, { useState } from "react";
import styled from "styled-components";
import api from "../../../api/axios";
import { Link, useNavigate } from "react-router-dom";
// import SignUpPage from "./SignUpPage"; // 사용되지 않는 컴포넌트는 제거했습니다.
import FacebookButton from "../ui/FacebookButton";

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
    margin-bottom: 30px;
    color: #262626;
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

// Link
const StyledLink = styled(Link)`
    text-decoration: none;
    color: #0095f6;
    font-weight: 500;
    font-size: 14px;
`;

const TextDivider = styled.div`
    width: 100%;
    display: flex;
    align-items: center;
    text-align: center;
    margin: 20px 0;
    color: #8e8e8e;
    font-size: 13px;

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

const SignInPage = () => {
    const access = localStorage.getItem("accessToken");
    const refresh = localStorage.getItem("refreshToken");

    console.log("[debug] SignInPage token acc : ", access);
    console.log("[debug] SignInPage token ref : ", refresh);

    const [email, setEmail] = useState("");
    const [passwd, setPasswd] = useState("");

    const moveUrl = useNavigate();

    const handleSubmit = async () => {
        // 이벤트 객체는 더 이상 필요 없으므로 제거했습니다.
        const data = { email, passwd };
        console.log(">>>>>>>>>>> SignInPage handleSubmit :", data);

        try {
            const response = await api.get("/api/v2/inspire/user/signin", {
                params: data,
            });

            console.log("[debug] >>> post response : ", response);
            console.log("accessToken", response.headers.get("authorization"));
            console.log("refreshToken", response.headers.get("refresh-token"));

            localStorage.setItem(
                "accessToken",
                response.headers.get("authorization")
            );
            localStorage.setItem(
                "refreshToken",
                response.headers.get("refresh-token")
            );

            console.log(response.data);
            localStorage.setItem("userInfo", response.data.name);
            localStorage.setItem("userEmail", response.data.email);
            localStorage.setItem(
                "userImageUrl",
                response.data.userImageUrl || ""
            );

            moveUrl("/main");
        } catch (error) {
            console.log("[debug] >>> post error", error);
            // 에러 발생 시 사용자에게 알림을 주는 로직을 추가할 수 있습니다.
            alert(
                "로그인에 실패했습니다. 이메일과 비밀번호를 다시 확인해주세요."
            );
        }
    };

    return (
        <Container>
            <div>
                <FormWrapper>
                    <InstagramTitle>Instagram</InstagramTitle>
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
                        type="button"
                        onClick={handleSubmit}
                        disabled={!email || !passwd} // 이메일과 비밀번호가 모두 입력되어야 버튼 활성화
                    >
                        로그인
                    </Button>
                    <TextDivider>또는</TextDivider>
                    <FacebookButton></FacebookButton>
                    <StyledLink to="/accounts/password/reset">
                        비밀번호를 잊으셨나요?
                    </StyledLink>
                </FormWrapper>
                <BottomBox>
                    계정이 없으신가요? <StyledLink to="/">가입하기</StyledLink>
                </BottomBox>
            </div>
        </Container>
    );
};

export default SignInPage;
