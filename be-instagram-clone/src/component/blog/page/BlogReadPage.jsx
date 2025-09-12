import { useNavigate, useParams } from "react-router-dom";

import styled, { keyframes } from "styled-components";
import Button from "../ui/Button";
import { useEffect, useState } from "react";
import api from "../../../api/axios";
import TextArea from "../ui/TextArea";
import BlogCommentList from "../list/BlogCommentList";


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

const PostContainer = styled.div`
    padding: 8px 16px;
    border: 1px solid grey;
    border-radius: 8px;
`;

const TitleText = styled.p`
    font-size: 28px;
    font-weight: 500;
`;

const ContentText = styled.p`
    font-size: 20px;
    line-height: 32px;
    white-space: pre-wrap;
`;

const CommentLabel = styled.p`
    font-size: 16px;
    font-weight: 500;
`;

const spin = keyframes`
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
`;

const Spinner = styled.div`
  border: 6px solid #f3f3f3;
  border-top: 6px solid #3498db;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  animation: ${spin} 1s linear infinite;
  margin: 100px auto;
`;

const WelcomeMessage = styled.div`
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 16px;
    color: #333;
`;


const BlogReadPage = () => {
    
    const {blogId} = useParams();
    const moveURL = useNavigate();
    const [blog, setBlog] = useState({}) ;
    const [comment, setComment] = useState('');

    const [username,  setUsername] = useState("");
    
    
    //////////// 블로그, 댓글을 분리해서 댓글만 리렌더링하고 싶다
    const [comments, setComments] = useState([]);

    

    const getBlog = async () => {

        // api get() 데이터 가져온 후 해당 데이터 state 상태로 만들어보자. 
        // queryString   : http://xxxxxxxx/blogs?id=1
        // const response = await api.get(`/blogs?id=${blogId}`);
        // const response = await api.get(`/blogs` , {
        //     params : {id : blogId} 
        // });
        

        // path variable : http://xxxxxxxx/blogs/1
        // embed 이용해서 특정블로그에 comments 함께 가져와 본다면?
        // const response = await api.get(`/blogs/${blogId}?_embed=comments`);
        
        //////////////////////// token get 
        const token = localStorage.getItem("accessToken");
        console.log("[debug] read page api token : " , token);
        console.log("[debug] >>> params blogId : " , blogId );  
        ////////////////
        

        const response = await api.get(`/auth/api/v2/blog/read/${blogId}` , {
                headers: {
                    Authorization: token ? `${token}` : ""
                }
            });
        console.log("[debug] >>> getBlog response  : " , response );   
        console.log("[debug] >>> getBlog get data  : " , response.data );   

        setBlog({
            blogId : response.data.blogId,
            title : response.data.title,
            content : response.data.content 
        });
        console.log("[debug] >>> params blog.blogId : " , blog.blogId );  
        setComments(response.data.comments || [] ) ;
        
    }

    /*
    Quiz-01)
    - 전달받은 인자를 comments 에 입력하고 
    - 메인페이지로 이동 후 
    - 블로그를 선택하여 상세보기로 이동했을 때 댓글이 정상적으로 출력되는지 확인 
    
    Quiz-02)
    - 전달받은 인자를 comments 에 등록하고
    - 메인페이지로 이동이 아닌
    - 현재 화면에서 comments 를 갱신하여 바로 반영되도록 해 본다면?
    */
    const commentHandler = async (blogId, comment) => {
        const token = localStorage.getItem("accessToken");
        console.log("[debug] read page api token : " , token);
        console.log("[debug] >>> comment post  : " , blogId, comment );   
        await api.post('auth/api/v2/blog/comment/register' , { comment , blogId} , {
                headers: {
                    Authorization: token ? `${token}` : ""
                }
            })  
                .then( response => {
                    
                    console.log("[debug] >>> response  : " , response);
                    if( response.status === 201 ) {
                        // getBlog();
                        const newComment = response.data[response.data.length -1] ;
                        setComments((ary) => {
                            return [...ary , newComment] 
                        })
                        setComment('');
                    }
                      
                    
                })
                .catch( error => {
                    console.log("[debug] >>> error  : ");  
                });
    }

    
    useEffect(() => {
        getBlog();
        const user = localStorage.getItem("userInfo"); 
        
        if(user) {
            setUsername(user);
            
        }
    }, [] ); 

    ////////// comment delete
    const commentDelete = async (blogId, commentId) => {
        const token = localStorage.getItem("accessToken");
        console.log("[debug] read page api token : " , token);

        console.log(">>>>>>>>>>>>>>>>>>>> comment delete blogId" , blogId); 
        console.log(">>>>>>>>>>>>>>>>>>>> comment delete commentId " , commentId); 
        await api.delete(`/auth/api/v2/blog/comment/delete/${blogId}/${commentId}`,{
                headers: {
                    Authorization: token ? `${token}` : ""
                }
            })  
                .then( response => {
                    console.log("[debug] >>> response  : " , response);
                    if( response.status === 204 ) {
                        setComments((ary) => {
                            return ary.filter((c) => c.commentId !== commentId ); 
                        })
                        setComment('');
                    }
                })
                .catch( error => {
                    console.log("[debug] >>> delete error  : ");  
                });

    }

    return(
        <Wrapper>
            { !blog.blogId && <Spinner /> }
            {  blog.blogId &&
            <Container>
                {username && <WelcomeMessage>{username}님 환영합니다.</WelcomeMessage>}
                
                
            
                <Button title="메인페이지 이동"
                        btnHandler={() => {
                            moveURL("/blog");
                        }} />

                <PostContainer>
                    <TitleText>{blog.title}</TitleText>
                    <ContentText>{blog.content}</ContentText>
                </PostContainer>

                {/* 댓글 UI 설계 */}
                <CommentLabel>한 줄 댓글</CommentLabel>

                <BlogCommentList    comments={comments || [] }
                                    commentDeleteHandler={commentDelete} />

                <TextArea   height={40}
                            value={comment}
                            changeHandler={(e) => {
                                setComment(e.target.value);
                            }}  />

                <Button title="댓글 작성"
                        btnHandler={() => commentHandler(blog.blogId, comment) } />
                        
            </Container>
            }
        </Wrapper>
    ) ;

}

export default BlogReadPage ;