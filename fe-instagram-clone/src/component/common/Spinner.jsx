import styled, { keyframes } from "styled-components";

const spin = keyframes`
  0% { transform: rotate(0deg);}
  100% { transform: rotate(360deg);}
`;

const SpinnerWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

const SpinnerIcon = styled.div`
  border: 4px solid #e5e7eb;
  border-top: 4px solid #3b82f6;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  animation: ${spin} 0.8s linear infinite;
`;

const Spinner = () => (
  <SpinnerWrapper>
    <SpinnerIcon />
  </SpinnerWrapper>
);

export default Spinner;