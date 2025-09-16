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

const LikeButton = ({ email, postId, initialIsLiked, initialLikeCount }) => {
    // 좋아요 상태와 개수를 이 컴포넌트 내부에서 직접 관리합니다.
    const [isLiked, setIsLiked] = useState(initialIsLiked);
    const [likeCount, setLikeCount] = useState(initialLikeCount);
    const token = localStorage.getItem("accessToken");

    const handleLikeClick = async () => {

        setIsLiked(!isLiked);
        setLikeCount(isLiked ? likeCount - 1 : likeCount + 1);

        console.log("[debug] >>> 좋아요 클릭 토큰 : " + token);
        console.log("[debug] >>> 보내는 이메일아이디 : " + email);
        console.log("[debug] >>> 보내는 게시물아이디 : " + postId);
        try {
            const endpoint = isLiked
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
                const { likeCount: newLikeCount } = response.data;
                setLikeCount(newLikeCount);
            } else {
                setIsLiked(isLiked);
                setLikeCount(likeCount);
            }
        } catch (error) {
            setIsLiked(isLiked);
            setLikeCount(likeCount);
            console.log(error);
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
