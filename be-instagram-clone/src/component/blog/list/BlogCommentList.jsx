
import styled from "styled-components";
import BlogCommentListItem from "../item/BlogCommentListItem";
import Button from "../ui/Button";
import api from "../../../api/axios";
const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;

    & > * {
        :not(:last-child) {
            margin-bottom: 16px;
        }
    }
`;

const BlogCommentList = ({comments, commentDeleteHandler}) => {
    
    return(
        <Wrapper>
            {
                comments.map((comment, idx) => {
                    return (
                        <BlogCommentListItem    key={comment.commentId}
                                                comment={comment} 
                                                onDelete={commentDeleteHandler}>
                            
                        </BlogCommentListItem>
                                   
                    )
                })
            }
        </Wrapper>
    );
}

export default BlogCommentList ;
