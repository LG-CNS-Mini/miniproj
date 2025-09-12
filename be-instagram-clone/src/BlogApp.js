import React, { useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import styled           from "styled-components";
import MainPage         from "./component/blog/page/MainPage";
import BlogWritePage    from "./component/blog/page/BlogWritePage";
import BlogReadPage     from "./component/blog/page/BlogReadPage";
import SignUpPage       from "./component/blog/page/SignUpPage";
import LoginPage        from "./component/blog/page/LoginPage";
import MessagePage from "./component/blog/page/MessagePage";

const DivTitleText = styled.p`
    font-size   : 24px;
    font-weight : bold;
    text-align  : center ;
`;

const BlogApp = ()  => {

    useEffect(() => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        console.log(">>>>>>>>>>.. 앱 시작: 토큰 초기화");
    }, []); 

    return (
        <BrowserRouter>
            <DivTitleText>
                AM Inspire Camp 3th
            </DivTitleText>
            <Routes>
                <Route path="/"             element={ <SignUpPage /> } />
                <Route path="/login"        element={ <LoginPage /> } />
                <Route path="/blog"         element={ <MainPage /> } />
                <Route path="/blog-write"   element={ <BlogWritePage />} />
                <Route path="/blog-read/:blogId" element={ <BlogReadPage />} />
                
                <Route path="/notifications/:id" element={<MessagePage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default BlogApp ; 