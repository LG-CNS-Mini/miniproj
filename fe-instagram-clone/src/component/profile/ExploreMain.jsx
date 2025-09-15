import React, { useEffect, useState } from "react";
import styled from "styled-components";
import api from "../../api/axios";
import FeedReadModal from "./FeedReadModal";

// 스타일 컴포넌트
const Container = styled.div`
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 0;
`;

const ProfileHeader = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 40px;
`;

const ProfileImage = styled.img`
  width: 140px;
  height: 140px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 40px;
  border: 1px solid #ddd;
`;

const ProfileInfo = styled.div`
  flex: 1;
`;

const UserName = styled.h2`
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 16px;
`;

const Stats = styled.div`
  display: flex;
  gap: 32px;
  font-size: 16px;
`;

const Stat = styled.span`
  color: #222;
`;

const SettingsIcon = styled.div`
  font-size: 24px;
  margin-left: 24px;
  cursor: pointer;
`;

const PostGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
`;

const PostItem = styled.div`
  width: 100%;
  aspect-ratio: 1 / 1;
  background: #fafafa;
  border-radius: 8px;
  overflow: hidden;
`;

const PostImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const ExploreMain = () => {
  const baseURL = "http://localhost:8088"; // 실제 이미지 서버 주소로 변경 필요
  const [profile, setProfile] = useState({
    userName: "",
    postCount: 0,
    followerCount: 0,
    followingCount: 0,
    profileImage: localStorage.getItem("userImageUrl") || "",
  });
  const [posts, setPosts] = useState([]);
  const [isFeedReadOpen, setIsFeedReadOpen] = useState(false);  
  const userId = localStorage.getItem("userEmail");
  const userImageUrl = localStorage.getItem("userImageUrl");
  const [selectedPostId, setSelectedPostId] = useState(null);

  useEffect(() => {
    // 프로필 정보 가져오기 (예시: userName, 팔로워, 팔로우, 게시물 수)
    // api.get(`/api/v1/user/${userId}`)
    //   .then(res => {
    //     setProfile({
    //       userName: res.data.userName,
    //       postCount: res.data.postCount,
    //       followerCount: res.data.followerCount,
    //       followingCount: res.data.followingCount,
    //       profileImage: res.data.profileImage || userImageUrl,
    //     });
    //   });

    // 게시글 목록 페이징 처리
    selectPosts();
  }, [userId, userImageUrl]);

  const handlePostClick = (postId) => {
    console.log("Post clicked:", postId);
    // FeedReadModal 열기 로직 추가
    setSelectedPostId(postId);
    setIsFeedReadOpen(true);
  }

  const handlerPostDelete = () => {
    // 게시글 삭제 후 목록 갱신
    selectPosts();
  }

  const selectPosts = () => {
    api.get(`/api/v1/post/posts`, {
      params: { page: 1, size: 10 }
    })
      .then(res => {
        console.log(res.data);
        setPosts(res.data || []);
      });
  }

  return (
    <Container>
        <FeedReadModal
            postId={selectedPostId}
            isOpen={isFeedReadOpen}
            onClose={() => setIsFeedReadOpen(false)}
            onDelete={handlerPostDelete}
        />
      <ProfileHeader>
        <ProfileImage
          src={profile.profileImage ? `${baseURL}${profile.profileImage}` : "/default-profile.png"}
          alt="profile"
        />
        <ProfileInfo>
          <UserName>{profile.userName}</UserName>
          <Stats>
            <Stat>게시물 <b>{profile.postCount}</b></Stat>
            <Stat>팔로워 <b>{profile.followerCount}</b></Stat>
            <Stat>팔로우 <b>{profile.followingCount}</b></Stat>
          </Stats>
        </ProfileInfo>
        <SettingsIcon>⚙️</SettingsIcon>
      </ProfileHeader>
      <PostGrid>
        {posts.map(post => (
          <PostItem 
            onClick={() => handlePostClick(post.postID)} 
            key={post.postID}
          >
            <PostImage
              src={post.imageUrls && post.imageUrls.length > 0
                ? `${baseURL}${post.imageUrls[0]}`
                : "/default-post.png"}
              alt="post"
            />
          </PostItem>
        ))}
      </PostGrid>
    </Container>
  );
};

export default ExploreMain;
