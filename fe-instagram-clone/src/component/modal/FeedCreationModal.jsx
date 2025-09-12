import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { X } from 'lucide-react';
import FileUploadComponent from '../upload/FileUploadComponent';
import api from '../../api/axios';

const Overlay = styled.div`
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
`;
const Modal = styled.div`
  background-color: white;
  border-radius: 16px;
  width: ${props => props.step === 2 ? '900px' : '384px'};
  max-width: 90vw;
  min-height: 630px;
  padding: 32px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  height: 630px;
  transition: width 0.3s;
`;

const Content = styled.div`
  flex: 1;
  display: flex;
  flex-direction: ${props => props.step === 2 ? 'row' : 'column'};
  align-items: center;
  justify-content: ${props => props.step === 2 ? 'center' : 'space-between'};
  width: 100%;
  transition: flex-direction 0.3s;
`;

const LeftBox = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const RightBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  padding: 32px;
`;

const PreviewBox = styled.div`
  margin-top: 20px;
  width: 300px;
  height: 300px;
  border: 1px solid #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
`;

const TextArea = styled.textarea`
  width: 100%;
  min-height: 120px;
  margin-top: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 12px;
  font-size: 16px;
  resize: vertical;
`;

const Button = styled.button`
  background-color: #3b82f6;
  color: white;
  padding: 8px 24px;
  border-radius: 8px;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 24px;

  &:hover {
    background-color: #2563eb;
  }
`;
const CloseButton = styled.button`
  position: absolute;
  top: 16px;
  right: 16px;
  padding: 4px;
  border: none;
  background: none;
  border-radius: 50%;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #f3f4f6;
  }
`;

const Header = styled.h2`
  font-size: 18px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 32px;
  color: #111827;
  margin: 0 0 32px 0;
`;

const IconContainer = styled.div`
  margin-bottom: 24px;
`;

const IconWrapper = styled.div`
  position: relative;
`;

const PhotoIcon = styled.div`
  width: 64px;
  height: 48px;
  border: 2px solid #d1d5db;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9fafb;
`;

const PhotoCircle = styled.div`
  width: 24px;
  height: 24px;
  background-color: #d1d5db;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const PhotoDot = styled.div`
  width: 8px;
  height: 8px;
  background-color: white;
  border-radius: 50%;
`;

const MountainIcon = styled.div`
  position: absolute;
  top: -4px;
  right: -4px;
  width: 16px;
  height: 16px;
  background-color: #d1d5db;
  border-radius: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const MountainShape = styled.div`
  width: 8px;
  height: 8px;
  border-left: 2px solid white;
  border-bottom: 2px solid white;
  transform: rotate(45deg);
`;

const VideoIcon = styled.div`
  position: absolute;
  bottom: -8px;
  right: -8px;
  width: 32px;
  height: 32px;
  background-color: #1f2937;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const PlayTriangle = styled.div`
  width: 0;
  height: 0;
  border-left: 6px solid white;
  border-top: 4px solid transparent;
  border-bottom: 4px solid transparent;
  margin-left: 1px;
`;

const Text = styled.p`
  color: #6b7280;
  text-align: center;
  margin-bottom: 32px;
  line-height: 1.6;
  margin: 0 0 32px 0;
`;

const DemoButton = styled.button`
  background-color: #3b82f6;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
`;

const Container = styled.div`
  min-height: 100vh;
  background-color: #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const TopBar = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`;

const BackButton = styled.button`
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #111827;
  padding: 4px 8px;
`;

const ShareButton = styled.button`
  background-color: #3b82f6;
  color: white;
  padding: 8px 18px;
  border-radius: 8px;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 16px;

  &:hover {
    background-color: #2563eb;
  }
`;

const NextButton = styled.button`
  background: none;
  border: none;
  color: #111827;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  padding: 8px 18px;

  &:hover {
    color: #3b82f6;
  }
`;

const SlideButton = styled.button`
  background: none;
  border: none;
  color: #111827;
  font-size: 32px;
  font-weight: bold;
  cursor: pointer;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  padding: 0 12px;
  opacity: 0.7;
  width: 50px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;

  &:hover {
    color: #3b82f6;
    opacity: 1;
  }
`;

const PreviewWrapper = styled.div`
  position: relative;
  width: 300px;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

// 이미지 양쪽 절반에 버튼이 위치하도록 PreviewWrapper 바깥에 SlideArea 추가
const SlideArea = styled.div`
  position: absolute;
  top: 0;
  bottom: 0;
  width: 50px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 3;
`;

const SlideAreaLeft = styled(SlideArea)`
  left: 0;
`;

const SlideAreaRight = styled(SlideArea)`
  right: 0;
`;

const FeedCreationModal = ({ isOpen, onClose, onCreateStory: onSelectPicture }) => {
  if (!isOpen) return null;
  const [files, setFiles] = useState([]);
  const [step, setStep] = useState(1);
  const [caption, setCaption] = useState("");
  const [currentIndex, setCurrentIndex] = useState(0);
  const [previewURL, setPreviewURL] = useState("");

  useEffect(() => {
    if (files.length === 0) {
      setCurrentIndex(0);
      setPreviewURL("");
    } else {
      const safeIndex = Math.min(currentIndex, files.length - 1);
      setCurrentIndex(safeIndex);
      const file = files[safeIndex];
      if (file) {
        if (typeof file === "string") {
          setPreviewURL(file);
        } else {
          setPreviewURL(URL.createObjectURL(file));
        }
      }
    }
  }, [files, currentIndex]);

  const handlePrev = () => {
    if (currentIndex > 0) setCurrentIndex(currentIndex - 1);
  };

  const handleNext = () => {
    if (currentIndex < files.length - 1) setCurrentIndex(currentIndex + 1);
  };

  const handleFeedCreateNext = () => {
    setStep(2);
  };

  const handleBackToStep1 = () => {
    setStep(1);
  };

  const handleShare = () => {
    // TODO : post create api 연동
    api.post("/api/v2/posts/create" , {
      files : files,
      userId : {
        localStorage.getItem("userInfo").userId
      }
    })
    onClose();
  };

  return (
    <Overlay>
      <Modal step={step}>
        <CloseButton onClick={onClose}>
          <X size={20} color="#6b7280" />
        </CloseButton>
        {step === 2 ? (
          <TopBar>
            <BackButton onClick={handleBackToStep1} title="뒤로가기">
              이전
            </BackButton>
            <Header style={{ margin: 0, flex: 1, textAlign: 'center' }}>
              새 게시물 만들기
            </Header>
            <ShareButton onClick={handleShare}>
              공유하기
            </ShareButton>
          </TopBar>
        ) : (
          <TopBar>
            <div style={{ width: 40 }} /> {/* 좌측 공간 확보 */}
            <Header style={{ margin: 0, flex: 1, textAlign: 'center' }}>
              새 게시물 만들기
            </Header>
            {files.length > 0 ? (
              <NextButton onClick={handleFeedCreateNext}>
                다음
              </NextButton>
            ) : (
              <div style={{ width: 60 }} /> // 우측 공간 확보
            )}
          </TopBar>
        )}
        <Content step={step}>
          {step === 1 && (
            <>
              <IconContainer>
                {files.length === 0 && (
                  <IconWrapper>
                    <PhotoIcon>
                      <PhotoCircle>
                        <PhotoDot />
                      </PhotoCircle>
                      <MountainIcon>
                        <MountainShape />
                      </MountainIcon>
                    </PhotoIcon>
                    <VideoIcon>
                      <PlayTriangle />
                    </VideoIcon>
                  </IconWrapper>
                )}
                {files.length !== 0 && (
                  <PreviewWrapper>
                    {currentIndex > 0 && (
                      <SlideAreaLeft>
                        <SlideButton onClick={handlePrev}>
                          &#60;
                        </SlideButton>
                      </SlideAreaLeft>
                    )}
                    <PreviewBox style={{ marginTop: 0 }}>
                      <img
                        src={previewURL}
                        alt="preview"
                        style={{ width: "100%", height: "auto" }}
                      />
                    </PreviewBox>
                    {currentIndex < files.length - 1 && (
                      <SlideAreaRight>
                        <SlideButton onClick={handleNext}>
                          &#62;
                        </SlideButton>
                      </SlideAreaRight>
                    )}
                  </PreviewWrapper>
                )}
              </IconContainer>
              <FileUploadComponent 
                setPreviewURL={setPreviewURL}
                setFiles={setFiles}  
              />
            </>
          )}
          {step === 2 && (
            <>
              <LeftBox>
                <PreviewWrapper>
                  {currentIndex > 0 && (
                    <SlideAreaLeft>
                      <SlideButton onClick={handlePrev}>
                        &#60;
                      </SlideButton>
                    </SlideAreaLeft>
                  )}
                  <PreviewBox style={{ marginTop: 0 }}>
                    <img
                      src={previewURL}
                      alt="preview"
                      style={{ width: "100%", height: "auto" }}
                    />
                  </PreviewBox>
                  {currentIndex < files.length - 1 && (
                    <SlideAreaRight>
                      <SlideButton onClick={handleNext}>
                        &#62;
                      </SlideButton>
                    </SlideAreaRight>
                  )}
                </PreviewWrapper>
              </LeftBox>
              <RightBox>
                <TextArea
                  placeholder="문구를 입력하세요..."
                  value={caption}
                  onChange={e => setCaption(e.target.value)}
                />
              </RightBox>
            </>
          )}
        </Content>
      </Modal>
    </Overlay>
  );
};

export default FeedCreationModal;