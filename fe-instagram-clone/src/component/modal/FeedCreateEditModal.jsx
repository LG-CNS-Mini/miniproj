import React, { useEffect, useRef, useState } from 'react';
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

const HashtagDropdown = styled.div`
  position: relative;
  width: 100%;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-top: 8px;
  max-height: 180px;
  overflow-y: auto;
  z-index: 100;
`;

const HashtagResult = styled.div`
  padding: 10px 16px;
  border-bottom: 1px solid #f3f3f3;
  cursor: pointer;
  display: flex;
  align-items: center;
`;

const HashtagName = styled.span`
  font-weight: 500;
  color: #3b82f6;
`;

const HashtagCount = styled.span`
  margin-left: 8px;
  color: #888;
  font-size: 13px;
`;

const HashtagEmpty = styled.div`
  padding: 12px;
  color: #888;
`;

const FeedCreateEditModal = ({ isOpen, onClose, postId }) => {
  const modalRef = useRef(null);
  const textAreaRef = useRef(null);
  const [files, setFiles] = useState([]);
  const [step, setStep] = useState(postId ? 2: 1);
  const [content, setContent] = useState("");
  const [currentIndex, setCurrentIndex] = useState(0);
  const [previewURL, setPreviewURL] = useState("");
  const [hashtagActive, setHashtagActive] = useState(false);
  const [hashtagQuery, setHashtagQuery] = useState("");
  const [hashtagSearchResults, setHashtagSearchResults] = useState([]);
  const [hashtagResults, setHashtagResults] = useState([]);

  const handleClose = () => {
      setFiles([]);
      setStep(1);
      setContent("");
      setHashtagResults([]);
      onClose();
  }

  // ESC 키로 닫기
  useEffect(() => {
    if (!isOpen) return;
    
    const handleEsc = (e) => {
      if (e.key === 'Escape') handleClose();
    };
    window.addEventListener('keydown', handleEsc);
    return () => window.removeEventListener('keydown', handleEsc);
  }, [isOpen]);

  // 바깥 클릭으로 닫기 + 해시태그 검색 비활성화
  useEffect(() => {
    if (!isOpen) return;
    const handleClickOutside = (e) => {
      if (
        modalRef.current &&
        !modalRef.current.contains(e.target) &&
        hashtagActive
      ) {
        setHashtagActive(false);
        setHashtagQuery("");
        setHashtagSearchResults([]);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [hashtagActive, isOpen]);

  // 해시태그 검색 API 호출
  useEffect(() => {
    if (hashtagActive && hashtagQuery.length > 0) {
      // TODO : 해시태그 검색 API 호출
      console.log("[debug] FeedCreationModal hashtagQuery : ", hashtagQuery);
      // api.get("/api/v1/hashtag", { params: { hashtag: hashtagQuery } })
      //   .then(res => setHashtagSearchResults(res.data))
      //   .catch(() => setHashtagSearchResults([]));
      // 해시태그 결과물 임시 데이터

      setHashtagSearchResults(prev => [...prev, { id: 1, name: "example", count: 1234 }]);
    } else {
      setHashtagSearchResults([]);
    }
  }, [hashtagQuery, hashtagActive]);

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

  useEffect(() => {
    if(postId){
      api.get(`/api/v1/post/read/${postId}`)
        .then(res => {
          setContent(res.data.content);
          // TODO : 해시태그 가져와서 content에 반영
          if(res.data.imageUrls && res.data.imageUrls.length > 0){
            setFiles(res.data.imageUrls.map(imgUrl => `${import.meta.env.VITE_APP_JSON_SERVER_URL}${imgUrl}`));
            setPreviewURL(`${import.meta.env.VITE_APP_JSON_SERVER_URL}${res.data.imageUrls[0]}`);
          }
        });
    }
  }, []);

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

  const handleSave = () => {
    if(postId){
      updatePost();
    }else{
      insertPost();
    }
    handleClose();
  };

  const insertPost = () => {
    const formData = new FormData();

    // content 추가
    formData.append("content", content);
    formData.append("authorEmail", localStorage.getItem("userEmail"));
    // files 배열을 FormData에 추가 (Blob -> File 변환 필요 없으면 바로 추가)
    files.forEach((file, i) => {
      // 파일 이름 지정 (예: file0.png, file1.png ...)
      formData.append(`postImages[${i}]`, file);
    });
    formData.append("hashtags", hashtagResults.map(tag => tag.name));
    console.log("[debug] insertPost formData:", formData.get("hashtags"));
    api.post("/api/v1/post/register", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
  const updatePost = () => {
    const formData = new FormData();
    formData.append("content", content);
    formData.append("authorEmail", localStorage.getItem("userEmail"));
    files.forEach((file, i) => {
      formData.append(`postImages[${i}]`, file);
    });
    formData.append("hashtags", hashtagResults.map(tag => tag.name));
    api.put(`/api/v1/post/update/${postId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
  const closeHashtagSearch = () => {
    setHashtagActive(false);
    setHashtagQuery("");
    setHashtagSearchResults([]);
  };
  const handleContentChange = (e) => {
    const value = e.target.value;
    setContent(value);
    const cursorPosition = e.target.selectionStart;
    const hashIndex = value.lastIndexOf('#', cursorPosition - 1);

    // 해시태그 삭제 감지 및 hashtagResults에서 제거
    setHashtagResults(prev => {
      // 현재 content에서 남아있는 해시태그 목록 추출
      const matches = value.match(/#([\u3131-\u3163\uac00-\ud7a3\w]+)/g) || [];
      const currentTags = matches.map(tag => tag.replace('#', ''));
      // prev에 있던 태그 중 현재 content에 없는 태그만 제거
      return prev.filter(tagObj => currentTags.includes(tagObj.name));
    });

    if (hashIndex !== -1) {
      const beforeCursor = value.substring(hashIndex, cursorPosition);
      if (value[cursorPosition] === ' ') {
        closeHashtagSearch();
        const hashtagMatch = beforeCursor.match(/^#([\u3131-\u3163\uac00-\ud7a3\w]+)$/);
        if (hashtagMatch && hashtagMatch[1]) {
          setHashtagResults(prev =>
            prev.some(tag => tag.name === hashtagMatch[1])
              ? prev
              : [...prev, { name: hashtagMatch[1] }]
          );
        }
        return;
      }
      if (beforeCursor.includes(' ')) {
        closeHashtagSearch();
        return;
      }
      const hashtagMatch = beforeCursor.match(/^#([\u3131-\u3163\uac00-\ud7a3\w]+)$/);
      setHashtagActive(true);
      setHashtagQuery(hashtagMatch ? hashtagMatch[1] : "");
    } else {
      setHashtagActive(false);
      setHashtagQuery("");
      setHashtagSearchResults([]);
    }
  };

  // 공백, 줄바꿈 입력 시 해시태그 결과에 추가
  const handleContentKeyDown = (e) => {
    if (hashtagActive && (e.key === " " || e.key === "Enter")) {
      if (hashtagQuery) {
        setHashtagResults(prev =>
          prev.some(tag => tag.name === hashtagQuery)
            ? prev
            : [...prev, { name: hashtagQuery }]
        );
      }
      setHashtagActive(false);
      setHashtagQuery("");
      setHashtagSearchResults([]);
    }
  };

  const handleOverlayClick = (e) => {
    // Overlay 영역 클릭 시 모달 닫기 및 해시태그 검색 비활성화
    if (e.target === e.currentTarget) {
      setHashtagActive(false);
      setHashtagQuery("");
      setHashtagSearchResults([]);
      handleClose();
    }
  };

  if (!isOpen) return null;

  const handlerGetHashTag = () => {
    const formData = new FormData();

    files.forEach((file, i) => {
      formData.append(`images[${i}]`, file);
    });

    // TODO : AI를 통한 해시태그 추출 API 호출
    api.post("/api/v1/post/ai/hashtag", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      // 추출된 해시태그를 content에 추가 
      const hashtags = response.data.map(item => item.hashtags).flat();
      const uniqueHashtags = Array.from(new Set(hashtags));
      const hashtagString = uniqueHashtags.join(" ");
      setContent(prev => prev + (prev ? " " : "") + hashtagString);
    });
  }

  

  return (
    <Overlay ref={modalRef} onClick={handleOverlayClick}>
      <Modal step={step} ref={modalRef}>
        {step === 2 ? (
          <TopBar>
            <BackButton onClick={handleBackToStep1} title="뒤로가기">
              이전
            </BackButton>
            <Header style={{ margin: 0, flex: 1, textAlign: 'center' }}>
              새 게시물 만들기
            </Header>
            <ShareButton onClick={handleSave}>
              {postId ? "수정하기" : "공유하기"}
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
                      {previewURL && 
                        <img
                          src={previewURL}
                          alt="preview"
                          style={{ width: "100%", height: "auto" }}
                        />  
                      }
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
                  ref={textAreaRef}
                  placeholder="문구를 입력하세요..."
                  value={content}
                  onChange={(e) => handleContentChange(e)}
                  onKeyDown={handleContentKeyDown}
                />
                {/* 해시태그 검색 영역 */}
                {hashtagActive && (
                  <HashtagDropdown>
                    {hashtagSearchResults.length === 0 ? (
                      <HashtagEmpty>
                        검색 결과가 없습니다.
                      </HashtagEmpty>
                    ) : (
                      hashtagSearchResults.map((tag, idx) => (
                        <HashtagResult
                          key={tag.id || idx}
                          onMouseDown={() => {
                            const before = content.replace(/#\w*$/, `#${tag.name} `);
                            setContent(before);
                            setHashtagActive(false);
                            setHashtagQuery("");
                            setHashtagSearchResults([]);
                            setTimeout(() => textAreaRef.current?.focus(), 0);
                          }}
                        >
                          <HashtagName>#{tag.name}</HashtagName>
                          <HashtagCount>
                            게시물 {tag.count?.toLocaleString() || 0}개
                          </HashtagCount>
                        </HashtagResult>
                      ))
                    )}
                  </HashtagDropdown>
                )}
              <Button onClick={handlerGetHashTag}>해시태그 추출</Button>
              </RightBox>
            </>
          )}
        </Content>
      </Modal>
    </Overlay>
  );
};

export default FeedCreateEditModal;