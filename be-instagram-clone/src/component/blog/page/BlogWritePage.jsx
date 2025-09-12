import styled from "styled-components";
import TextArea from "../ui/TextArea";
import Button from "../ui/Button";
import { useState } from "react";

import api from '../../../api/axios' ;
import { useNavigate } from "react-router-dom";

const Wrapper = styled.div`
    padding: 16px;
    width: calc(100% - 32px);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
`;

const Container = styled.div`
    width: 100%;
    max-width: 720px;

    & > * {
        :not(:last-child) {
            margin-bottom: 16px;
        }
    }
`;

const BlogWritePage = () => {

    /*
    1. title, content 훅을 사용해서 상태관리 해야함
    2. 버튼의 이벤트 발생시 axios.js api 를 이용해서 json-server post 통신을 하고 
    데이터를 저장
    3. useNavigate() 를 이용해서 라우터에 등록된 MainPage 이동 
    */

    const [title, setTitle]     = useState('');
    const [content, setContent] = useState('');

    const moveUrl = useNavigate(); 

    const saveHandler = async (title, content) => {
        // localhost:4000/blogs
        
        // mybatis basic
        // const data = { title , content } ;

        // jpa basic 
        const authorEmail = localStorage.getItem("userEmail");
        console.log("[debug] >>> " , title, content, authorEmail);
        const data = { title , content, authorEmail } ; 

        const token = localStorage.getItem("accessToken");
        console.log("[debug] blog write api token : " , token);

        await api.post('/auth/api/v2/blog/register' , data ,{
                headers: {
                    Authorization: token ? `${token}` : ""
                }
            })
            .then( response => {
                console.log("[debug] >>> post response : " , response ); 
                moveUrl('/blog');
            })
            .catch( error => {
                console.log("[debug] >>> post error");
            });
    }

    return (
        <Wrapper>
            <Container>
                <TextArea   value={title}
                            changeHandler={(e) => {
                                setTitle(e.target.value);
                            }}
                            height={20} />

                <TextArea   value={content}
                            changeHandler={(e) => {
                                setContent(e.target.value);
                            }}
                            height={480} />

                <Button title='작성글 저장'
                        btnHandler={(e) => saveHandler(title, content) }/>
            </Container>
        </Wrapper>
    );
}

export default BlogWritePage ;