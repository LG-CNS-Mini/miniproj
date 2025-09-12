

import styled from "styled-components";
import Button from "../ui/Button";

const Wrapper = styled.div`
    width: calc(100% - 32px);
    padding: 8px 16px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    border: 1px solid grey;
    border-radius: 8px;
    cursor: pointer;
    background: white;
    :hover {
        background: lightgrey;
    }
`;

const ContentText = styled.p`
    font-size: 16px;
    white-space: pre-wrap;
`;

const BlogCommentListItem = ({comment, onDelete}) => {
    return (
        <Wrapper>
            
            <ContentText>{comment.comment}</ContentText>
            <Button title="삭제" btnHandler={() => onDelete(comment.blogId , comment.commentId) } /> 

        </Wrapper>
        
        
    );
}

export default BlogCommentListItem ;







