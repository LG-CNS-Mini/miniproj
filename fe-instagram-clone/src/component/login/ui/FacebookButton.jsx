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

    /* 페이스북 스타일 추가 ✨ */
    background-color: #1877f2; /* 페이스북 파란색 */
    color: #ffffff; /* 하얀색 글씨 */
    border: none; /* 테두리는 없애서 더 깔끔하게 */

    &:hover {
        opacity: 0.9;
    }
`;

const FacebookButton = ({ title }) => {
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
            {title || "페이스북 로그인"}
        </StyledButton>
    );
};

export default FacebookButton;
