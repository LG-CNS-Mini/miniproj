// 이미지 슬라이더 컴포넌트
import React, { useState } from "react";
import styled from "styled-components";

const ImageSlider = styled.div`
  position: relative;
  width: 100%;
  height: 400px;
  background: #fafafa;
  display: flex;
  align-items: center;
  justify-content: center;
`;


const ImageBox = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const FeedImage = styled.img`
  max-width: 100%;
  max-height: 400px;
  object-fit: contain;
  border-radius: 0;
`;


const SlideButtonLeft = styled.button`
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255,255,255,0.7);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  font-size: 22px;
  cursor: pointer;
  z-index: 2;
`;

const SlideButtonRight = styled(SlideButtonLeft)`
  left: auto;
  right: 12px;
`;


const baseURL = import.meta.env.VITE_APP_JSON_SERVER_URL; // 이미지 서버 주소
const FeedImageSlider = ({ images }) => {
  const [current, setCurrent] = useState(0);

  if (!images || images.length === 0) return null;

  const handlePrev = () => {
    if (current > 0) setCurrent(current - 1);
  };
  const handleNext = () => {
    if (current < images.length - 1) setCurrent(current + 1);
  };

  return (
    <ImageSlider>
      {current > 0 && (
        <SlideButtonLeft onClick={handlePrev}>&lt;</SlideButtonLeft>
      )}
      <ImageBox>
        <FeedImage src={`${baseURL}${images[current]}`} alt="feed" />
      </ImageBox>
      {current < images.length - 1 && (
        <SlideButtonRight onClick={handleNext}>&gt;</SlideButtonRight>
      )}
    </ImageSlider>
  );
};

export default FeedImageSlider;