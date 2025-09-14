import { useNavigate } from "react-router-dom";
import Navigation from "../component/nav/Navigation";
import FeedCreationModal from "../component/modal/FeedCreationModal";
import { useState } from "react";
import { useRef } from 'react';
import FeedMain from "../component/feed/FeedMain";

const FeedPage = () => {
    const moveURL = useNavigate();
    const [feedPage, setFeedPage] = useState('feed');
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
            />
            <FeedCreationModal
                isOpen={isFeedCreateOpen}
                onClose={handleFeedCreateClose}
                onCreateStory={(event) => { handleButtonClick(event) }}
            />
            <FeedMain feedPage={feedPage} />
        </>
    )
};

export default FeedPage;