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

const FollowButton = styled.button`
  background: #3897f0;
  color: #fff;
  font-weight: 600;
  border: none;
  border-radius: 6px;
  padding: 7px 24px;
  font-size: 16px;
  margin-left: 18px;
  cursor: pointer;
  transition: background 0.2s;
  &:hover {
    background: #1877f2;
  }
`;

const Nickname = styled.div`
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #222;
`;

const ProfileMain = ({ profileUser, setUserImageUrl, userImageUrl }) => {
  const baseURL = "http://localhost:8088";
  const [posts, setPosts] = useState([]);
  const [userId, setUserId] = useState(profileUser || localStorage.getItem("userEmail"));
  const [userName, setUserName] = useState("");
  const [nickname, setNickname] = useState(""); // 닉네임 상태 추가
  const [isFeedReadOpen, setIsFeedReadOpen] = useState(false);  
  const [selectedPostId, setSelectedPostId] = useState(null);
  const [postCount, setPostCount]  = useState(0);
  const [followerCount, setFollowerCount]  = useState(0);
  const [followingCount, setFollowingCount]  = useState(0);
  const [isUploading, setIsUploading] = useState(false);
  const [isFollowing, setIsFollowing] = useState(false); // 팔로우 상태 추가
  
  useEffect(() => {
    setUserId(profileUser || localStorage.getItem("userEmail"));
  }, [profileUser]);

  useEffect(() => {
    api.get(`/api/v2/inspire/user/${userId}`)
      .then(res => {
        setUserImageUrl(res.data.userImageUrl); // 상위에도 반영
        setUserName(res.data.userName);
        setNickname(res.data.nickname); // 닉네임 셋팅
        setPostCount(res.data.postCount);
        setFollowerCount(res.data.followerCount);
        setFollowingCount(res.data.followingCount);
      });
    selectPosts();
  }, [userId]);

  const handlePostClick = (postId) => {
    setSelectedPostId(postId);
    setIsFeedReadOpen(true);
  }

  const handlerPostDelete = () => {
    selectPosts();
  }

  const selectPosts = () => {
    api.get(`/api/v1/post/posts`, {
      params: { page: 1, size: 10 }
    })
      .then(res => {
        setPosts(res.data || []);
      });
  }

  // 팔로우 버튼 클릭 핸들러 (API 연동 필요)
  const handleFollow = () => {
    api.post(`/api/v1/follow`, { targetUserId: userId })
      .then(() => {
        // 팔로우 성공 시 UI 갱신 등 처리
        alert("팔로우 완료!");
      });
  };

  const myUserId = localStorage.getItem("userEmail");

  // 프로필 이미지 업로드 핸들러
  const handleProfileImageClick = () => {
    // 유저 아이디가 내 아이디와 같을 때만 클릭 허용
    if (userId !== myUserId) return;
    document.getElementById("profile-image-input").click();
  };

  const handleProfileImageChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setIsUploading(true);
    const formData = new FormData();
    formData.append("image", file);

    try {
      const res = await api.post(`/api/v2/inspire/user/${userId}/profileimage`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      console.log(res.data.userImageUrl);
      // 업로드 성공 시 프로필 이미지 갱신
      setUserImageUrl(res.data.userImageUrl);
    } catch (err) {
      alert("이미지 업로드 실패");
    }
    setIsUploading(false);
  };

  return (
    <Container>
      <input
        type="file"
        id="profile-image-input"
        accept="image/*"
        style={{ display: "none" }}
        onChange={handleProfileImageChange}
      />
      <FeedReadModal
        userImageUrl={userImageUrl}
        postId={selectedPostId}
        isOpen={isFeedReadOpen}
        onClose={() => setIsFeedReadOpen(false)}
        onDelete={handlerPostDelete}
      />
      <ProfileHeader>
        <ProfileImage
          src={userImageUrl ? `${baseURL}${userImageUrl}` : `${baseURL}/images/default-profile.png`}
          alt="profile"
          onClick={handleProfileImageClick}
          style={{ cursor: "pointer", opacity: isUploading ? 0.5 : 1 }}
          title="프로필 이미지 변경"
        />
        <ProfileInfo>
          <UserName>
            {userName}
            {userId !== myUserId && (
              <FollowButton onClick={handleFollow}>팔로우</FollowButton>
            )}
          </UserName>
          <Nickname>{nickname}</Nickname>
          <Stats>
            <Stat>게시물 <b>{postCount}</b></Stat>
            <Stat>팔로워 <b>{followerCount}</b></Stat>
            <Stat>팔로우 <b>{followingCount}</b></Stat>
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

export default ProfileMain;
