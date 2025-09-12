import LogoutButton from "../ui/LogoutButton";
import styled from "styled-components";

// Container
const Container = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f0f2f5;
`;

const MainPage = () => {
    return (
        <Container>
            <LogoutButton></LogoutButton>
        </Container>
    );
};

export default MainPage;
