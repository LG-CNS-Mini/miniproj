import React, { useState, useEffect } from "react";
import styled from "styled-components";
import api from "../../api/axios";
import { FaHeart, FaRegHeart } from "react-icons/fa";

const StyledButton = styled.button`
    display: flex;
    align-items: center;
    background: none;
    border: none;
    font-size: 17px;
    cursor: pointer;
    margin-bottom: 10px;
`;

const LikeButton = ({ email, postId }) => {
    // 좋아요 상태와 개수를 이 컴포넌트 내부에서 직접 관리합니다.
    const [isLiked, setIsLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(0);
    const token = localStorage.getItem("accessToken");

    // 컴포넌트가 처음 렌더링될 때, 서버에서 초기 좋아요 상태와 개수를 가져옵니다.
    useEffect(() => {
        const fetchLikeStatus = async () => {
            try {
                const response = await api.get("/api/v2/inspire/like/status", {
                    params: { email, postId },
                    headers: { Authorization: `${token}` },
                });
                if (response.status === 200) {
                    const {
                        isLiked: initialIsLiked,
                        likeCount: initialLikeCount,
                    } = response.data;
                    setIsLiked(initialIsLiked);
                    setLikeCount(initialLikeCount);
                }
            } catch (error) {
                console.error(
                    ">>> 좋아요 상태를 가져오는 중 에러가 발생했습니다:",
                    error
                );
                // 에러 발생 시 초기 상태(0개)가 표시됩니다.
                setIsLiked(false);
                setLikeCount(0);
            }
        };

        // email과 postId가 유효할 때만 API 호출
        if (email && postId) {
            fetchLikeStatus();
        }
    }, [email, postId]);

    const handleLikeClick = async () => {
        // Optimistic Update: API 호출 전에 UI를 먼저 업데이트하여 사용자 경험을 향상시킵니다.
        const prevIsLiked = isLiked;
        const prevLikeCount = likeCount;

        setIsLiked(!prevIsLiked);
        setLikeCount(prevIsLiked ? prevLikeCount - 1 : prevLikeCount + 1);

        console.log("[debug] >>> 좋아요 클릭 토큰 : " + token);
        console.log("[debug] >>> 보내는 이메일아이디 : " + email);
        console.log("[debug] >>> 보내는 게시물아이디 : " + postId);
        try {
            const endpoint = prevIsLiked
                ? "/api/v2/inspire/like/unlike"
                : "/api/v2/inspire/like/like";
            const response = await api.post(
                endpoint,
                { email, postId },
                {
                    headers: { Authorization: `${token}` },
                }
            );

            if (response.status === 200) {
                // 성공 시 서버로부터 받은 최신 좋아요 개수로 UI를 최종 동기화합니다.
                const { likeCount: newLikeCount } = response.data;
                setLikeCount(newLikeCount);
                console.log(">>> 좋아요 상태 변경 성공!");
            } else {
                // 서버 응답이 실패 상태일 경우, 상태를 원래대로 되돌립니다.
                setIsLiked(prevIsLiked);
                setLikeCount(prevLikeCount);
                console.error(
                    ">>> API 호출은 성공했으나, 서버 응답이 실패 상태입니다."
                );
            }
        } catch (error) {
            // 네트워크 에러 등으로 API 호출이 실패할 경우, 상태를 원래대로 되돌립니다.
            setIsLiked(prevIsLiked);
            setLikeCount(prevLikeCount);
            console.error(">>> API 호출 중 에러가 발생했습니다:", error);
        }
    };

    return (
        <StyledButton onClick={handleLikeClick}>
            {isLiked ? <FaHeart color="#e53e3e" /> : <FaRegHeart />}
            {/* 좋아요 개수가 0일 경우 '0개'로 표시됩니다. */}
            <span style={{ marginLeft: 6 }}>{likeCount}개</span>
        </StyledButton>
    );
};

export default LikeButton;
