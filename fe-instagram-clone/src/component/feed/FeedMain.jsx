import React, { useEffect, useState, useRef, useCallback } from "react";
import styled from "styled-components";
import api from "../../api/axios";
import FeedImageSlider from "./FeedImageSlider";
import LikeButton from "../like/likeButton";
import Spinner from "../common/Spinner"; // Spinner import
import LikeAndCommentSection from "../comment/LikeAndCommentSection";

const baseURL = import.meta.env.VITE_APP_JSON_SERVER_URL;

const FeedMain = ({ feedPage, profileUser }) => {
    const [feeds, setFeeds] = useState([]);
    const [page, setPage] = useState(0);
    const [hasNext, setHasNext] = useState(true);
    const [loading, setLoading] = useState(false);
    const observer = useRef();

    // 첫 페이지 로드
    useEffect(() => {
        setFeeds([]);
        setPage(0);
        setHasNext(true);
        fetchFeeds(0, true);
    }, []);

    // 다음 페이지 불러오기
    const fetchFeeds = async (pageNum, isInit = false) => {
        setLoading(true);
        try {
            const res = await api.get("api/v1/post/following", {
                params: {
                    page: pageNum,
                    size: 5,
                    email: localStorage.getItem("userEmail"),
                },
            });
            const items = res.data.items || [];
            setHasNext(res.data.hasNext);
            if (isInit) {
                setFeeds(items);
            } else {
                setFeeds((prev) => [...prev, ...items]);
            }
        } finally {
            setLoading(false);
        }
    };

    // 스크롤 감지 ref
    const lastFeedRef = useCallback(
        (node) => {
            if (loading) return;
            if (observer.current) observer.current.disconnect();
            observer.current = new window.IntersectionObserver((entries) => {
                if (entries[0].isIntersecting && hasNext) {
                    fetchFeeds(page + 1);
                    setPage((prev) => prev + 1);
                }
            });
            if (node) observer.current.observe(node);
        },
        [loading, hasNext, page]
    );

    return (
        <FeedContainer>
            {feeds.map((feed, idx) => (
                <FeedCard
                    key={feed.postId}
                    ref={idx === feeds.length - 1 ? lastFeedRef : null}
                >
                    <FeedHeader>
                        <ProfileThumb
                            src={
                                feed.authorProfileImageUrl
                                    ? `${baseURL}${feed.authorProfileImageUrl}`
                                    : `${baseURL}/images/default-profile.png`
                            }
                        />
                        <AuthorEmail>{feed.authorName}</AuthorEmail>
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
            {loading && (
                <SpinnerWrapper>
                    <Spinner />
                </SpinnerWrapper>
            )}
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
    padding-left: 44px; // 좌측 여백을 늘려줌
    margin-top: 4px;
`;

const ReplyItem = styled.li`
    margin-bottom: 4px;
    display: flex;
    align-items: flex-start;
    gap: 10px;
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

const SpinnerWrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 24px 0;
`;
