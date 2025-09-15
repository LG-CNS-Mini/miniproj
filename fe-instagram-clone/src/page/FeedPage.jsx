import { useNavigate } from "react-router-dom";
import Navigation from "../component/nav/Navigation";
import { useState } from "react";
import { useRef } from 'react';
import FeedMainPage from "../component/feed/FeedMainPage";
import FeedCreateEditModal from "../component/modal/FeedCreateEditModal";

const FeedPage = () => {
    const moveURL = useNavigate();
    const [feedPage, setFeedPage] = useState('feed');
    const [profileUser, setProfileUser] = useState(null);
    const [isFeedCreateOpen, setIsFeedCreateOpen] = useState(false);
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
            />
            <FeedCreateEditModal
                isOpen={isFeedCreateOpen}
                onClose={handleFeedCreateClose}
            />
            <FeedMainPage feedPage={feedPage} profileUser={profileUser} />
        </>
    )
};

export default FeedPage;