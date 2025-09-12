import styled from "styled-components";

const StyledTextarea = styled.textarea`
    width: calc(100% - 32px);
    ${(props) =>
        props.height &&
        `
        height: ${props.height}px;
    `}
    padding: 16px;
    font-size: 16px;
    line-height: 20px;
    margin-down : 16px ;
`;

const TextArea = ({height, value, changeHandler}) => {
    return (
        <StyledTextarea height={height} 
                        value={value}
                        onChange={changeHandler} />
    ) ;
}

export default TextArea ;