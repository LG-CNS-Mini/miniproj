import LikeButton from "../like/likeButton";
import LogoutButton from "../login/ui/LogoutButton";
import styled from "styled-components";

// Container
const Container = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f0f2f5;
`;

const TestPage = () => {
    return (
        <Container>
            <LikeButton></LikeButton>
            <LogoutButton></LogoutButton>
        </Container>
    );
};

export default TestPage;
