import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import SignUpPage from "./component/login/page/SignUpPage";
import SignInPage from "./component/login/page/SignInPage";
import styled from "styled-components";
import FeedPage from "./page/FeedPage";
import TestPage from "./component/login/page/TestPage";
import AuthRedirectHandler from "./component/login/auth/AuthRedirectHandler";

const DivTitleText = styled.p`
    font-size: 24px;
    font-weight: bold;
    text-align: center;
`;

function App() {
    return (
        <BrowserRouter>
            <DivTitleText>미니프로젝트7조 인스타그램</DivTitleText>
            <Routes>
                <Route path="/" element={<SignUpPage />} />
                <Route path="/signin" element={<SignInPage />} />
                <Route path="/main" element={<FeedPage />} />
                <Route path="/test" element={<TestPage />} />

                {/* ⭐ 중간 경유지 경로 ⭐ */}
                <Route
                    path="/oauth2/redirect"
                    element={<AuthRedirectHandler />}
                />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
