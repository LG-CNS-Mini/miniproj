import React from 'react';
import styled from 'styled-components';

// Styled Components
const NavigationContainer = styled.nav`
  width: 250px;
  height: 100vh;
  background-color: #ffffff;
  border-right: 1px solid #dbdbdb;
  padding: 20px 0;
  position: fixed;
  left: 0;
  top: 0;
  display: flex;
  flex-direction: column;
`;

const Logo = styled.h1`
  font-family: 'Instagram', cursive;
  font-size: 24px;
  font-weight: normal;
  padding: 25px 24px;
  margin: 0;
  color: #000;
  border-bottom: 1px solid #efefef;
  margin-bottom: 20px;
`;

const NavList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
  flex: 1;
`;

const NavItem = styled.li`
  width: 100%;
`;

const NavLink = styled.a`
  display: flex;
  align-items: center;
  padding: 12px 24px;
  text-decoration: none;
  color: #262626;
  font-size: 16px;
  font-weight: 400;
  transition: background-color 0.2s ease;
  cursor: pointer;

  &:hover {
    background-color: #fafafa;
  }

  &.active {
    font-weight: 600;
  }
`;

const IconContainer = styled.div`
  width: 24px;
  height: 24px;
  margin-right: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ProfileImage = styled.img`
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
`;

// Icon Components (simplified SVG icons)
const HomeIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 2.1L1 12h3v9h6v-6h4v6h6v-9h3L12 2.1z"/>
  </svg>
);

const SearchIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <circle cx="11" cy="11" r="8"></circle>
    <path d="m21 21-4.35-4.35"></path>
  </svg>
);

const ExploreIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <polygon points="3 11 22 2 13 21 11 13 3 11"></polygon>
  </svg>
);

const ReelsIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
    <rect x="2" y="3" width="20" height="14" rx="2" ry="2" fill="none" stroke="currentColor" strokeWidth="2"/>
    <line x1="8" y1="21" x2="16" y2="21" stroke="currentColor" strokeWidth="2"/>
    <line x1="12" y1="17" x2="12" y2="21" stroke="currentColor" strokeWidth="2"/>
  </svg>
);

const MessagesIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <path d="m3 21 1.9-5.7a8.5 8.5 0 1 1 3.8 3.8z"></path>
  </svg>
);

const LikeIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
  </svg>
);

const CreateIcon = () => (
  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
    <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
    <line x1="12" y1="8" x2="12" y2="16"></line>
    <line x1="8" y1="12" x2="16" y2="12"></line>
  </svg>
);

// Main Navigation Component
const Navigation = ({ activeItem = 'home', onNavigate, profileImage }) => {
  const navItems = [
    { key: 'home', label: '홈', icon: <HomeIcon /> },
    { key: 'search', label: '검색', icon: <SearchIcon /> },
    // { key: 'explore', label: '탐색 탭', icon: <ExploreIcon /> },
    // { key: 'reels', label: '릴스', icon: <ReelsIcon /> },
    // { key: 'messages', label: '메시지', icon: <MessagesIcon /> },
    // { key: 'notifications', label: '알림', icon: <LikeIcon /> },
    { key: 'create', label: '만들기', icon: <CreateIcon /> },
  ];

  const handleItemClick = (key) => {
    if (onNavigate) {
      onNavigate(key);
    }
  };

  return (
    <NavigationContainer>
      <Logo>Instagram</Logo>
      <NavList>
        {navItems.map((item) => (
          <NavItem key={item.key}>
            <NavLink
              className={activeItem === item.key ? 'active' : ''}
              onClick={() => handleItemClick(item.key)}
            >
              <IconContainer>{item.icon}</IconContainer>
              {item.label}
            </NavLink>
          </NavItem>
        ))}
        <NavItem>
          <NavLink
            className={activeItem === 'profile' ? 'active' : ''}
            onClick={() => handleItemClick('profile')}
          >
            <IconContainer>
              {profileImage ? (
                <ProfileImage src={profileImage} alt="프로필" />
              ) : (
                <div style={{ 
                  width: '24px', 
                  height: '24px', 
                  borderRadius: '50%', 
                  backgroundColor: '#dbdbdb',
                  border: '2px solid #262626'
                }} />
              )}
            </IconContainer>
            프로필
          </NavLink>
        </NavItem>
      </NavList>
    </NavigationContainer>
  );
};

export default Navigation;