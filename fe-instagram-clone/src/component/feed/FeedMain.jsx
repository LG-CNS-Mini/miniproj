import React, { useEffect, useState } from "react";
import styled from "styled-components";
import api from "../../api/axios";
import { FaRegHeart, FaHeart } from "react-icons/fa"; // 아이콘 추가
import FeedImageSlider from "./FeedImageSlider";
import LikeButton from "../like/likeButton";

const baseURL = import.meta.env.VITE_APP_JSON_SERVER_URL;

const FeedMain = ({ feedPage, profileUser }) => {
    const [feeds, setFeeds] = useState([]);
    const [comments, setComments] = useState([]);
    const [isLiked, setIsLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(0);

    useEffect(() => {
        api.get("api/v1/post/following", {
            params: {
                page: 0,
                size: 5,
                email: localStorage.getItem("userEmail"),
            },
        }).then((res) => {
            console.log(res.data.items);
            setFeeds(res.data.items || []);
        });
    }, []);

    return (
        <FeedContainer>
            {feeds.map((feed) => (
                <FeedCard key={feed.postId}>
                    <FeedHeader>
                        <ProfileThumb
                            src={feed.authorProfileImageUrl
                                ? `${baseURL}${feed.authorProfileImageUrl}`
                                : `${baseURL}/images/default-profile.png`}
                        />
                        <AuthorEmail>{feed.authorEmail}</AuthorEmail>
                    </FeedHeader>
                    <FeedImageSlider images={feed.imageUrls} />
                    <FeedContent>
                        <FeedText>{feed.content}</FeedText>
                        <FeedMeta>
                            <FeedDate>{getTimeAgo(feed.createDate)}</FeedDate>
                            <FeedHashtags>
                                {feed.hashtags &&
                                    feed.hashtags.map((tag, idx) => (
                                        <Hashtag key={idx}>#{tag}</Hashtag>
                                    ))}
                            </FeedHashtags>
                        </FeedMeta>
                        <LikeAndCommentSection
                            feed={feed}
                            likeCount={feed.likeCount}
                            likedByMe={feed.likedByMe}
                            initialComments={feed.comments}
                        />
                    </FeedContent>
                </FeedCard>
            ))}
        </FeedContainer>
    );
};

export default FeedMain;

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
const LikeAndCommentSection = ({ feed, likeCount, likedByMe, initialComments }) => {
    const [comments, setComments] = useState(initialComments || []);
    const [isLiked, setIsLiked] = useState(likedByMe || false);
    const [currentLikeCount, setCurrentLikeCount] = useState(likeCount || 0);
    const [commentInput, setCommentInput] = useState("");
    const [replyInput, setReplyInput] = useState({});
    const [showReplyBox, setShowReplyBox] = useState({});
    const myEmail = localStorage.getItem("userEmail");

    // 댓글 목록 가져오기
    const fetchComments = () => {
        api.get(`/api/v1/comments/${feed.postId}`)
            .then(res => setComments(res.data))
            .catch(err => console.error("댓글 목록 불러오기 실패:", err));
    };

    useEffect(() => {
        fetchComments();
    }, [feed.postId]);

    // 좋아요 버튼 클릭 핸들러
    const handleLike = () => {
        api.post("/api/v1/like/toggle", {
            postId: feed.postId,
            email: myEmail,
        }).then(res => {
            setIsLiked(res.data.liked);
            setCurrentLikeCount(res.data.likeCount);
        });
    };

    // 댓글 추가
    const handleAddComment = () => {
        if (!commentInput.trim()) return;
        api.post("/api/v1/comments/register", {
            postId: feed.postId,
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
            postId: feed.postId,
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
        api.delete(`/api/v1/comments/${commentId}`,{
          params: {
            authorEmail: myEmail
          }
        })
            .then(() => {
                fetchComments();
            })
            .catch((error) => {
                console.error("댓글 삭제 실패:", error);
            });
    };

    return (
        <div>
            <LikeButton
                isLiked={isLiked}
                likeCount={currentLikeCount}
                onClick={handleLike}
            />
            <CommentSection>
                <CommentInputBox>
                    <CommentInput
                        value={commentInput}
                        onChange={(e) => setCommentInput(e.target.value)}
                        placeholder="댓글을 입력하세요"
                    />
                    <CommentAddBtn onClick={handleAddComment}>
                        등록
                    </CommentAddBtn>
                </CommentInputBox>
                <CommentList>
                    {comments.map((comment) => (
                        <React.Fragment key={comment.commentId}>
                            <CommentItem>
                                <CommentProfileImg
                                    src={
                                        comment.authorProfileImageUrl
                                            ? `${baseURL}${comment.authorProfileImageUrl}`
                                            : `${baseURL}/images/default-profile.png`
                                    }
                                    alt="profile"
                                />
                                <CommentBody>
                                    <CommentTop>
                                        <CommentNickname>
                                            {comment.authorNickname || comment.authorEmail}
                                        </CommentNickname>
                                        {/* 내가 쓴 댓글만 삭제 버튼 노출 */}
                                        {comment.authorEmail === myEmail && (
                                            <DeleteBtn onClick={() => handleDeleteComment(comment.commentId)}>
                                                삭제
                                            </DeleteBtn>
                                        )}
                                    </CommentTop>
                                    <CommentContent>
                                        {comment.content}
                                    </CommentContent>
                                    <CommentMeta>
                                        <span>
                                            {getTimeAgo(comment.createdAt)}
                                        </span>
                                        <ReplyBtn
                                            onClick={() =>
                                                setShowReplyBox({
                                                    ...showReplyBox,
                                                    [comment.commentId]:
                                                        !showReplyBox[comment.commentId],
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
                                                onChange={(e) =>
                                                    setReplyInput({
                                                        ...replyInput,
                                                        [comment.commentId]: e.target.value,
                                                    })
                                                }
                                                placeholder="답글을 입력하세요"
                                            />
                                            <ReplyAddBtn
                                                onClick={() => handleAddReply(comment.commentId)}
                                            >
                                                등록
                                            </ReplyAddBtn>
                                        </ReplyInputBox>
                                    )}
                                    {/* 대댓글 리스트 */}
                                    {comment.children && comment.children.length > 0 && (
                                        <ReplyList>
                                            {comment.children.map((reply) => (
                                                <ReplyItem key={reply.commentId}>
                                                    <CommentProfileImg
                                                        src={
                                                            reply.authorProfileImageUrl
                                                                ? `${baseURL}${reply.authorProfileImageUrl}`
                                                                : `${baseURL}/images/default-profile.png`
                                                        }
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
                                                        {/* 내가 쓴 대댓글만 삭제 버튼 노출 */}
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
                        </React.Fragment>
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
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
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
    margin-bottom: 14px;
    display: flex;
    align-items: flex-start;
    gap: 10px;
`;

const CommentProfileImg = styled.img`
    width: 34px;
    height: 34px;
    border-radius: 50%;
    object-fit: cover;
    background: #eee;
`;

const CommentBody = styled.div`
    flex: 1;
`;

const CommentTop = styled.div`
    display: flex;
    align-items: center;
    gap: 8px;
`;

const CommentNickname = styled.span`
    font-weight: 600;
    font-size: 15px;
    color: #222;
`;

const CommentContent = styled.div`
    font-size: 15px;
    margin: 2px 0 4px 0;
`;

const CommentMeta = styled.div`
    font-size: 13px;
    color: #888;
    display: flex;
    gap: 12px;
    align-items: center;
`;

const ReplyBtn = styled.button`
    background: none;
    border: none;
    color: #3b82f6;
    font-size: 14px;
    margin-left: 10px;
    cursor: pointer;
    padding: 0;
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

const DeleteBtn = styled.button`
    background: none;
    border: none;
    color: #e53e3e;
    font-size: 13px;
    margin-left: 8px;
    cursor: pointer;
    padding: 0;
`;
