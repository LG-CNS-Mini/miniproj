import styled from "styled-components";

const StyledButton = styled.button`
    padding: 8px 16px;
    font-size: 16px;
    border-width: 1px;
    border-radius: 8px;
    cursor: pointer;
    margin-bottom : 16px;
`;

const Button = ({ title, btnHandler}) => {
    return (
        <StyledButton onClick={btnHandler}>{title || "BUTTON" }</StyledButton> 
    );
}

export default Button ; 
