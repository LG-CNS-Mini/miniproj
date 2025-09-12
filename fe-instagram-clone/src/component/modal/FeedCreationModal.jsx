import { useState } from 'react';
import styled from 'styled-components';
import { X } from 'lucide-react';

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
  width: 384px;
  max-width: 90vw;
  padding: 32px;
  position: relative;
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

const Content = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
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

const Button = styled.button`
  background-color: #3b82f6;
  color: white;
  padding: 8px 24px;
  border-radius: 8px;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #2563eb;
  }
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

const FeedCreationModal = ({ isOpen, onClose, onCreateStory: onSelectPicture }) => {
  if (!isOpen) return null;

  return (
    <Overlay>
      <Modal>
        <CloseButton onClick={onClose}>
          <X size={20} color="#6b7280" />
        </CloseButton>
        
        <Header>새 게시물 만들기</Header>
        
        <Content>
          <IconContainer>
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
          </IconContainer>
          <Text>사진을 컴퓨터에서 선택하세요</Text>
          <Button onClick={onSelectPicture}>
            컴퓨터에서 선택
          </Button>
        </Content>
      </Modal>
    </Overlay>
  );
};

// 사용 예시
const App = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleCreateStory = () => {
    console.log('파일 선택 창 열기');
    // 실제로는 input[type="file"]을 클릭하거나 파일 선택 API 사용
  };

  return (
    <StoryCreationModal 
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onCreateStory={handleCreateStory}
    />
  );
};

export default FeedCreationModal;