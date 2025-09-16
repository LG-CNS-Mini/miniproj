import { useNavigate } from "react-router-dom";
import Navigation from "../component/nav/Navigation";
import { useState } from "react";
import FeedMainPage from "../component/feed/FeedMainPage";
import FeedCreateEditModal from "../component/modal/FeedCreateEditModal";

const FeedPage = () => {
    const moveURL = useNavigate();
    const [feedPage, setFeedPage] = useState('feed');
    const [profileUser, setProfileUser] = useState(null);
    const [isFeedCreateOpen, setIsFeedCreateOpen] = useState(false);
    const [userImageUrl, setUserImageUrl] = useState(localStorage.getItem("userImageUrl")); // 추가

    const feedCreateModalOpen = () => setIsFeedCreateOpen(true);
    const handleFeedCreateClose = () => {
        setIsFeedCreateOpen(false);
    }

    return (
        <>
            <Navigation 
                setFeedPage={setFeedPage}
                feedCreateModalOpen={feedCreateModalOpen} 
                setProfileUser={setProfileUser}
                profileUser={profileUser}
                userImageUrl={userImageUrl} // 전달
            />
            <FeedCreateEditModal
                isOpen={isFeedCreateOpen}
                onClose={handleFeedCreateClose}
            />
            <FeedMainPage 
                feedPage={feedPage} 
                profileUser={profileUser} 
                setUserImageUrl={setUserImageUrl}
                userImageUrl={userImageUrl}
            />
        </>
    )
};

export default FeedPage;