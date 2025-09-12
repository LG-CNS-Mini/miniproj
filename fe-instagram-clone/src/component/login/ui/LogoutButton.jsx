import styled from "styled-components";
import api from "../../../api/axios";
import { useNavigate } from "react-router-dom";

const StyledButton = styled.button`
    padding: 8px 16px;
    font-size: 16px;
    border-width: 1px;
    border-radius: 8px;
    cursor: pointer;
    margin-bottom: 16px;
`;

const Button = ({ title }) => {
    const moveUrl = useNavigate();

    const handleLogout = async () => {
        const token = localStorage.getItem("accessToken");

        console.log("[debug] >>> LOGOUT token: " + token);
        await api
            .post("/auth/api/v2/inspire/user/logout", null, {
                headers: { Authorization: `${token}` },
            })
            .then((response) => {
                localStorage.removeItem("accessToken");
                localStorage.removeItem("refreshToken");
                localStorage.removeItem("userInfo");
                moveUrl("/"); // 로그인 페이지로 이동
            })
            .catch((error) => {
                console.log("[debug] >>> user logout error : " + error);
            });
    };

    return (
        <StyledButton onClick={handleLogout}>
            {title || "로그아웃"}
        </StyledButton>
    );
};

export default Button;
