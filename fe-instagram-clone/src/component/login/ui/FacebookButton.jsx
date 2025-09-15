import styled from "styled-components";
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
    // useNavigate는 이 컴포넌트에서는 필요 없어서 제거했어요.
    // 하지만 만약 로그아웃 후 다른 페이지로 이동하려면 필요해요.

    const handleFacebookLogin = () => {
        // ⭐ API 호출 대신, 백엔드의 OAuth2 로그인 URL로 직접 리다이렉트합니다. ⭐
        window.location.href =
            "http://localhost:8088/oauth2/authorization/facebook";
    };

    return (
        <StyledButton onClick={handleFacebookLogin}>
            {title || "페이스북 로그인"}
        </StyledButton>
    );
};

export default FacebookButton;
