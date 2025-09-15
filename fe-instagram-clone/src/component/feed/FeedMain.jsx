import React, { useEffect, useState } from "react";
import styled from "styled-components";
import api from "../../api/axios";
import { FaRegHeart, FaHeart } from "react-icons/fa"; // 아이콘 추가

const baseURL = import.meta.env.VITE_APP_JSON_SERVER_URL; // 이미지 서버 주소

const FeedMain = ({ feedPage, profileUser }) => {
  const [feeds, setFeeds] = useState([]);

  useEffect(() => {
    api.get("api/v1/post/posts", { params: { page: 1, size: 5 } })
      .then(res => {
        setFeeds(res.data || []);
      });
  }, []);

  return (
    <FeedContainer>
      {feeds.map(feed => (
        <FeedCard key={feed.postId}>
          <FeedHeader>
            <ProfileThumb src={`${baseURL}/images/default-profile.png`} />
            <AuthorEmail>{feed.authorEmail}</AuthorEmail>
          </FeedHeader>
          <FeedImageSlider images={feed.imageUrls} />
          <FeedContent>
            <FeedText>{feed.content}</FeedText>
            <FeedMeta>
              <FeedDate>{getTimeAgo(feed.createDate)}</FeedDate>
              <FeedHashtags>
                {feed.hashtags && feed.hashtags.map((tag, idx) => (
                  <Hashtag key={idx}>#{tag}</Hashtag>
                ))}
              </FeedHashtags>
            </FeedMeta>
            <LikeAndCommentSection postId={feed.postId} />
          </FeedContent>
        </FeedCard>
      ))}
    </FeedContainer>
  );
};

export default FeedMain;

// 이미지 슬라이더 컴포넌트
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

// 시간 표시 함수
function getTimeAgo(dateString) {
  const now = new Date();
  const postDate = new Date(dateString);
  const diffMs = now - postDate;
  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHour = Math.floor(diffMin / 60);
  const diffDay = Math.floor(diffHour / 24);

  if (diffDay > 0) return `${diffDay}일 전`;
  if (diffHour > 0) return `${diffHour}시간 전`;
  if (diffMin > 0) return `${diffMin}분 전`;
  return "방금 전";
}

// 좋아요 & 댓글 컴포넌트
const LikeAndCommentSection = ({ postId }) => {
  const [liked, setLiked] = useState(false);
  const [comments, setComments] = useState([]);
  const [commentInput, setCommentInput] = useState("");
  const [likeCount, setLikeCount] = useState(0);
  // 대댓글 입력 상태 관리
  const [replyInput, setReplyInput] = useState({});
  const [showReplyBox, setShowReplyBox] = useState({});

  // 댓글 추가
  const handleAddComment = () => {
    if (!commentInput.trim()) return;
    setComments([
      ...comments,
      { id: Date.now(), text: commentInput, replies: [] }
    ]);
    setCommentInput("");
  };

  // 대댓글 추가
  const handleAddReply = (commentId) => {
    if (!replyInput[commentId]?.trim()) return;
    setComments(comments.map(c =>
      c.id === commentId
        ? { ...c, replies: [...c.replies, { id: Date.now(), text: replyInput[commentId] }] }
        : c
    ));
    setReplyInput({ ...replyInput, [commentId]: "" });
    setShowReplyBox({ ...showReplyBox, [commentId]: false });
  };

  return (
    <div>
      <LikeButton onClick={() => setLiked(l => !l)}>
        {liked ? <FaHeart color="#e53e3e" /> : <FaRegHeart />}
        <span style={{ marginLeft: 6 }}>{likeCount}개</span>
      </LikeButton>
      <CommentSection>
        <CommentInputBox>
          <CommentInput
            value={commentInput}
            onChange={e => setCommentInput(e.target.value)}
            placeholder="댓글을 입력하세요"
          />
          <CommentAddBtn onClick={handleAddComment}>등록</CommentAddBtn>
        </CommentInputBox>
        <CommentList>
          {comments.map(comment => (
            <CommentItem key={comment.id}>
              <CommentText>{comment.text}</CommentText>
              <ReplyBtn onClick={() =>
                setShowReplyBox({ ...showReplyBox, [comment.id]: !showReplyBox[comment.id] })
              }>
                답글
              </ReplyBtn>
              {showReplyBox[comment.id] && (
                <ReplyInputBox>
                  <ReplyInput
                    value={replyInput[comment.id] || ""}
                    onChange={e =>
                      setReplyInput({ ...replyInput, [comment.id]: e.target.value })
                    }
                    placeholder="답글을 입력하세요"
                  />
                  <ReplyAddBtn onClick={() => handleAddReply(comment.id)}>등록</ReplyAddBtn>
                </ReplyInputBox>
              )}
              {/* 대댓글 리스트 */}
              {comment.replies && comment.replies.length > 0 && (
                <ReplyList>
                  {comment.replies.map(reply => (
                    <ReplyItem key={reply.id}>
                      <ReplyText>{reply.text}</ReplyText>
                    </ReplyItem>
                  ))}
                </ReplyList>
              )}
            </CommentItem>
          ))}
        </CommentList>
      </CommentSection>
    </div>
  );
};

// styled-components
const FeedContainer = styled.div`
  max-width: 480px;
  margin: 40px auto;
  display: flex;
  flex-direction: column;
  gap: 32px;
`;

const FeedCard = styled.div`
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  padding: 0 0 16px 0;
  overflow: hidden;
`;

const FeedHeader = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
`;

const ProfileThumb = styled.img`
  width: 38px;
  height: 38px;
  border-radius: 50%;
  object-fit: cover;
  background: #eee;
`;

const AuthorEmail = styled.span`
  font-weight: 600;
  font-size: 16px;
`;

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

const FeedContent = styled.div`
  padding: 16px 18px 0 18px;
`;

const FeedText = styled.div`
  font-size: 16px;
  margin-bottom: 8px;
`;

const FeedMeta = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 8px;
`;

const FeedDate = styled.div`
  font-size: 13px;
  color: #888;
`;

const FeedHashtags = styled.div`
  display: flex;
  gap: 8px;
`;

const Hashtag = styled.span`
  color: #3b82f6;
  font-size: 14px;
  font-weight: 500;
`;

// 좋아요 & 댓글 styled-components
const LikeButton = styled.button`
  display: flex;
  align-items: center;
  background: none;
  border: none;
  font-size: 17px;
  cursor: pointer;
  margin-bottom: 10px;
`;

const CommentSection = styled.div`
  margin-top: 12px;
`;

const CommentInputBox = styled.div`
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
`;

const CommentInput = styled.input`
  flex: 1;
  padding: 7px 10px;
  border-radius: 6px;
  border: 1px solid #ddd;
  font-size: 15px;
`;

const CommentAddBtn = styled.button`
  background: #3b82f6;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0 14px;
  font-size: 15px;
  cursor: pointer;
`;

const CommentList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
`;

const CommentItem = styled.li`
  margin-bottom: 10px;
`;

const CommentText = styled.span`
  font-size: 15px;
`;

const ReplyBtn = styled.button`
  background: none;
  border: none;
  color: #3b82f6;
  font-size: 14px;
  margin-left: 10px;
  cursor: pointer;
`;

const ReplyInputBox = styled.div`
  display: flex;
  gap: 6px;
  margin-top: 6px;
`;

const ReplyInput = styled.input`
  flex: 1;
  padding: 6px 9px;
  border-radius: 6px;
  border: 1px solid #eee;
  font-size: 14px;
`;

const ReplyAddBtn = styled.button`
  background: #6366f1;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0 10px;
  font-size: 14px;
  cursor: pointer;
`;

const ReplyList = styled.ul`
  list-style: none;
  padding-left: 18px;
  margin-top: 4px;
`;

const ReplyItem = styled.li`
  margin-bottom: 4px;
`;

const ReplyText = styled.span`
  font-size: 14px;
  color: #555;
`;
