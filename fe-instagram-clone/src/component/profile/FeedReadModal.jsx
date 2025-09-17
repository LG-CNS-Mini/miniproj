import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import api from "../../api/axios";
import FeedCreateEditModal from "../modal/FeedCreateEditModal";
import LikeButton from "../like/likeButton"; // FeedMain에서 사용한 LikeButton import 필요

const baseURL = "http://localhost:8088"; // 이미지 서버 주소

// 날짜 차이 계산 함수
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

const FeedReadModal = ({ postId, isOpen, onClose, onDelete, userImageUrl }) => {
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [likeCount, setLikeCount] = useState(0);
  const [isLiked, setIsLiked] = useState(false);
  const [commentInput, setCommentInput] = useState("");
  const [replyInput, setReplyInput] = useState({});
  const [showReplyBox, setShowReplyBox] = useState({});
  const [menuOpen, setMenuOpen] = useState(false);
  const [isOpenEditModal, setIsOpenEditModal] = useState(false);
  const myEmail = localStorage.getItem("userEmail");
  const modalRef = useRef();
  // 게시글, 댓글, 좋아요 정보 불러오기
  useEffect(() => {
    if (!isOpen || !postId) return;
    api.get(`/api/v1/post/read/${postId}`)
      .then(res => {
        setPost(res.data);
        setLikeCount(res.data.likeCount || 0);
        setIsLiked(res.data.likedByMe || false);
      })
      .catch(() => setPost(null));

    fetchComments();
  }, [isOpen, postId]);

  // 댓글 목록 불러오기
  const fetchComments = () => {
    api.get(`/api/v1/comments/${postId}`)
      .then(res => setComments(res.data))
      .catch(err => setComments([]));
  };

  // 좋아요 토글
  const handleLike = () => {
    api.post("/api/v1/like/toggle", {
      postId,
      email: myEmail,
    }).then(res => {
      setIsLiked(res.data.liked);
      setLikeCount(res.data.likeCount);
    });
  };

  // 댓글 추가
  const handleAddComment = () => {
    if (!commentInput.trim()) return;
    api.post("/api/v1/comments/register", {
      postId,
      parentId: 0,
      content: commentInput,
      authorEmail: myEmail,
    })
      .then(() => {
        fetchComments();
        setCommentInput("");
      })
      .catch((error) => {
        console.error("댓글 추가 실패:", error);
      });
  };

  // 대댓글 추가
  const handleAddReply = (commentId) => {
    if (!replyInput[commentId]?.trim()) return;
    api.post("/api/v1/comments/register", {
      postId,
      parentId: commentId,
      content: replyInput[commentId],
      authorEmail: myEmail,
    })
      .then(() => {
        fetchComments();
        setReplyInput({ ...replyInput, [commentId]: "" });
        setShowReplyBox({ ...showReplyBox, [commentId]: false });
      })
      .catch((error) => {
        console.error("대댓글 추가 실패:", error);
      });
  };

  // 댓글/대댓글 삭제
  const handleDeleteComment = (commentId) => {
    api.delete(`/api/v1/comments/${commentId}`, {
      params: { authorEmail: myEmail }
    })
      .then(() => {
        fetchComments();
      })
      .catch((error) => {
        console.error("댓글 삭제 실패:", error);
      });
  };

  // ESC 키로 닫기
  useEffect(() => {
    if (!isOpen) return;
    const handleEsc = (e) => {
      if (e.key === "Escape") {
        if (menuOpen) {
          setMenuOpen(false);
          return;
        }
        if(isOpenEditModal) {
          setIsOpenEditModal(false);
          return;
        }
        onClose();
      }
    };
    window.addEventListener("keydown", handleEsc);
    return () => window.removeEventListener("keydown", handleEsc);
  }, [isOpen, onClose, isOpenEditModal, menuOpen]);

  // 바깥 클릭으로 닫기
  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      if (menuOpen) {
        setMenuOpen(false);
        return;
      }
      if(isOpenEditModal) {
        setIsOpenEditModal(false);
        return;
      }
      onClose();
    }
  };

  const handlerSaveComment = async () => {
    if (!commentInput.trim()) return;
    try {
        // TODO: 댓글 저장 API 호출
        console.log("Save comment:", commentInput);
        // const response = await api.post(`/api/v1/comment/${postId}`, {
        //     authorEmail: localStorage.getItem("userEmail"),
        //     text: commentInput
        // });
        // // 댓글 저장 성공 시, 새 댓글을 comments에 추가
        // setPost(prev => ({
        //     ...prev,
        //     comments: [...(prev.comments || []), response.data]
        // }));
        // setCommentInput(""); // 입력창 초기화
    } catch (e) {
        // 에러 처리 필요시 추가
    }
  };

  const handleMenuClick = () => {
    setMenuOpen(true);
  };

  const handleMenuClose = () => {
    setMenuOpen(false);
  };

  const handleDelete = () => {
    // TODO: 삭제 API 호출
    api.delete(`/api/v1/post/delete/${postId}`)
        .then(() => {
            setMenuOpen(false);
            onClose();
            onDelete();
        });
    
    // 삭제 후 모달 닫기 등 추가 처리
  };

  const handleEdit = () => {
    setMenuOpen(false);
    setIsOpenEditModal(true);
  };

  const handleEditClose = () => {
    setIsOpenEditModal(false);
  };

  // 메뉴 모달 바깥 클릭 시 닫기
  useEffect(() => {
    if (!menuOpen) return;
    const handleMenuOutsideClick = (e) => {
      // 메뉴 모달이 열려있고, 메뉴 영역이 아닌 곳 클릭 시 닫기
      if (
        document.getElementById("menu-modal") &&
        !document.getElementById("menu-modal").contains(e.target) &&
        !document.getElementById("menu-button").contains(e.target)
      ) {
        setMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handleMenuOutsideClick);
    return () => document.removeEventListener("mousedown", handleMenuOutsideClick);
  }, [menuOpen]);

  if (!isOpen || !post) return null;

  return (
    <Overlay onClick={handleOverlayClick}>
      {isOpenEditModal && (
        <FeedCreateEditModal
          isOpen={isOpenEditModal}
          onEditClose={handleEditClose}
          postId={postId}
          isEdit={true}
          onClose={() => setIsOpenEditModal(false)}
        />
      )}
      <Modal ref={modalRef}>
        <ImageSection>
          <MainImage
            src={post.imageUrls && post.imageUrls.length > 0
              ? `${baseURL}${post.imageUrls[0]}`
              : "/default-post.png"}
            alt="post"
          />
        </ImageSection>
        <ContentSection>
          <Header>
            {userImageUrl ? (
              <ProfileThumb src={`${baseURL}${userImageUrl}`} />
            ) : (
              <ProfileThumb src={`${baseURL}/images/default-profile.png`} />
            )}
            <MenuButton id="menu-button" onClick={handleMenuClick}>⋯</MenuButton>
            {menuOpen && (
              <MenuModal id="menu-modal">
                <MenuItem onClick={handleEdit}>수정</MenuItem>
                <MenuItemDelete onClick={handleDelete}>삭제</MenuItemDelete>
                <MenuItem onClick={handleMenuClose}>취소</MenuItem>
              </MenuModal>
            )}
          </Header>
          <PostContent>
            {post.content}
          </PostContent>
          
          {/* 좋아요 버튼 및 좋아요 수 */}
          <LikeCount>좋아요 {likeCount}개</LikeCount>

          {/* 댓글 목록 */}
          <Comments>
            <CommentInputBox>
              <CommentInput
                value={commentInput}
                onChange={e => setCommentInput(e.target.value)}
                placeholder="댓글 달기"
              />
              <PostBtn onClick={handleAddComment}>게시</PostBtn>
            </CommentInputBox>
            {comments && comments.length > 0 ? (
              <CommentList>
                {comments.map(comment => (
                  <CommentItem key={comment.commentId}>
                    <CommentProfileImg
                      src={comment.authorProfileImageUrl
                        ? `${baseURL}${comment.authorProfileImageUrl}`
                        : `${baseURL}/images/default-profile.png`}
                      alt="profile"
                    />
                    <CommentBody>
                      <CommentRow>
                        <CommentNickname>{comment.authorNickname || comment.authorEmail}</CommentNickname>
                        <CommentContent>{comment.content}</CommentContent>
                        {comment.authorEmail === myEmail && (
                          <DeleteBtn onClick={() => handleDeleteComment(comment.commentId)}>
                            삭제
                          </DeleteBtn>
                        )}
                      </CommentRow>
                      <CommentMeta>
                        <span>{getTimeAgo(comment.createdAt)}</span>
                        <ReplyBtn
                          onClick={() =>
                            setShowReplyBox({
                              ...showReplyBox,
                              [comment.commentId]: !showReplyBox[comment.commentId],
                            })
                          }
                        >
                          답글 달기
                        </ReplyBtn>
                      </CommentMeta>
                      {showReplyBox[comment.commentId] && (
                        <ReplyInputBox>
                          <ReplyInput
                            value={replyInput[comment.commentId] || ""}
                            onChange={e =>
                              setReplyInput({
                                ...replyInput,
                                [comment.commentId]: e.target.value,
                              })
                            }
                            placeholder="답글을 입력하세요"
                          />
                          <ReplyAddBtn onClick={() => handleAddReply(comment.commentId)}>
                            등록
                          </ReplyAddBtn>
                        </ReplyInputBox>
                      )}
                      {/* 대댓글 리스트 */}
                      {comment.children && comment.children.length > 0 && (
                        <ReplyList>
                          {comment.children.map(reply => (
                            <ReplyItem key={reply.commentId}>
                              <CommentProfileImg
                                src={reply.authorProfileImageUrl
                                  ? `${baseURL}${reply.authorProfileImageUrl}`
                                  : `${baseURL}/images/default-profile.png`}
                                alt="profile"
                              />
                              <ReplyText>
                                <CommentNickname>
                                  {reply.authorNickname || reply.authorEmail}
                                </CommentNickname>
                                <span style={{ marginLeft: 6 }}>
                                  {reply.content}
                                </span>
                                <span style={{
                                  marginLeft: 10,
                                  color: "#888",
                                  fontSize: 12,
                                }}>
                                  {getTimeAgo(reply.createdAt)}
                                </span>
                                {reply.authorEmail === myEmail && (
                                  <DeleteBtn onClick={() => handleDeleteComment(reply.commentId)}>
                                    삭제
                                  </DeleteBtn>
                                )}
                              </ReplyText>
                            </ReplyItem>
                          ))}
                        </ReplyList>
                      )}
                    </CommentBody>
                  </CommentItem>
                ))}
              </CommentList>
            ) : (
              <NoComment>아직 댓글이 없습니다.<br />댓글을 남겨보세요.</NoComment>
            )}
          </Comments>
          <Footer>
            <DateBox>{getTimeAgo(post.createDate)}</DateBox>
          </Footer>
        </ContentSection>
      </Modal>
    </Overlay>
  );
};

export default FeedReadModal;

// 스타일 컴포넌트
const Overlay = styled.div`
  position: fixed;
  inset: 0;
  background: rgba(60, 60, 60, 0.7);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Modal = styled.div`
  background: #fff;
  border-radius: 12px;
  display: flex;
  width: 900px;
  max-width: 98vw;
  min-height: 600px;
  position: relative;
  overflow: hidden;
`;

const ImageSection = styled.div`
  flex: 1.2;
  background: #222;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const MainImage = styled.img`
  max-width: 100%;
  max-height: 600px;
  object-fit: contain;
  border-radius: 0;
`;

const ContentSection = styled.div`
  flex: 1;
  background: #fff;
  padding: 32px 24px 24px 24px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const Header = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
`;

const ProfileThumb = styled.img`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
`;

const Author = styled.span`
  font-weight: 600;
  font-size: 18px;
`;

const FollowBtn = styled.button`
  background: none;
  border: none;
  color: #3b82f6;
  font-weight: 500;
  margin-left: auto;
  cursor: pointer;
`;

const PostContent = styled.div`
  font-size: 16px;
  margin-bottom: 24px;
`;

const Comments = styled.div`
  flex: 1;
  overflow-y: auto;
  margin-bottom: 24px;
`;

const CommentItem = styled.div`
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
`;

const NoComment = styled.div`
  color: #222;
  font-size: 18px;
  font-weight: 600;
  text-align: center;
  margin-top: 60px;
  margin-bottom: 60px;
`;

const Footer = styled.div`
  border-top: 1px solid #eee;
  padding-top: 16px;
`;

const LikeCount = styled.div`
  font-size: 15px;
  margin-bottom: 8px;
`;

const DateBox = styled.div`
  font-size: 13px;
  color: #888;
  margin-bottom: 16px;
`;

const CommentInputBox = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const CommentInput = styled.input`
  flex: 1;
  border: none;
  border-bottom: 1px solid #ddd;
  padding: 8px;
  font-size: 15px;
  outline: none;
`;

const PostBtn = styled.button`
  background: none;
  border: none;
  color: #3b82f6;
  font-weight: 500;
  cursor: pointer;
`;

const MenuButton = styled.button`
  background: none;
  border: none;
  font-size: 28px;
  color: #888;
  cursor: pointer;
  margin-left: auto;
  position: relative;
  z-index: 20;
`;

const MenuModal = styled.div`
  position: absolute;
  top: 48px;
  right: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.12);
  padding: 12px 0;
  min-width: 120px;
  z-index: 100;
  display: flex;
  flex-direction: column;
`;

const MenuItem = styled.button`
  background: none;
  border: none;
  padding: 12px 24px;
  text-align: left;
  font-size: 16px;
  color: #222;
  cursor: pointer;
  &:hover {
    background: #f3f3f3;
  }
`;

const MenuItemDelete = styled(MenuItem)`
  color: #e53935;
  font-weight: 600;
  &:hover {
    background: #ffeaea;
  }
`;

const CommentList = styled.div`
  margin-top: 16px;
`;

const CommentProfileImg = styled.img`
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 12px;
`;

const CommentBody = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
`;

const CommentRow = styled.div`
  display: flex;
  align-items: center;
`;

const CommentNickname = styled.span`
  font-weight: 600;
  font-size: 15px;
  margin-right: 6px;
  color: #222;
`;

const CommentContent = styled.span`
  font-size: 15px;
  color: #222;
`;

const CommentMeta = styled.div`
  font-size: 12px;
  color: #888;
  margin-top: 2px;
  display: flex;
  align-items: center;
  gap: 12px;
`;

const ReplyBtn = styled.button`
  background: none;
  border: none;
  color: #3b82f6;
  font-size: 12px;
  cursor: pointer;
`;

const ReplyInputBox = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
`;

const ReplyInput = styled.input`
  flex: 1;
  border: 1px solid #ddd;
  padding: 8px;
  font-size: 14px;
  border-radius: 8px;
  outline: none;
`;

const ReplyAddBtn = styled.button`
  background: #3b82f6;
  border: none;
  color: #fff;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
`;

const ReplyList = styled.div`
  margin-top: 12px;
  margin-left: 44px;
`;

const ReplyItem = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 8px;
`;

const ReplyText = styled.div`
  flex: 1;
  background: #f9f9f9;
  padding: 8px 12px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const DeleteBtn = styled.button`
  background: none;
  border: none;
  color: #e53e3e;
  font-size: 13px;
  margin-left: 8px;
  cursor: pointer;
  padding: 0;
`;