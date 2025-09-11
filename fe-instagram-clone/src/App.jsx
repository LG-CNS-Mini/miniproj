import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import SignUpPage from "./component/login/page/SignUpPage";
import SignInPage from "./component/login/page/SignInPage";
import styled from "styled-components";

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
            </Routes>
        </BrowserRouter>
    );
}

export default App;
