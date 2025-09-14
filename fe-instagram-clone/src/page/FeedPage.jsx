import { useNavigate } from "react-router-dom";
import Navigation from "../component/nav/Navigation";
import FeedCreationModal from "../component/modal/FeedCreationModal";
import { useState } from "react";
import { useRef } from 'react';

const FeedPage = () => {
    const moveURL = useNavigate();
    const [isFeedCreateOpen, setIsFeedCreateOpen] = useState(false);
    const feedCreateModalOpen = () => setIsFeedCreateOpen(true);
    const onClickNavigate = (key) => {
        switch (key) {
            case 'home':
                moveURL('/main');
                break;
            case 'search':
                break;
            case 'create':
                feedCreateModalOpen();
                break;
            default:
                break;
        }

        console.log("Navigating to:", key);
    };
    const handleFeedCreateClose = () => {
        setIsFeedCreateOpen(false);
    }
    return (
        <>
            <Navigation onNavigate={onClickNavigate}/>
            <FeedCreationModal 
                isOpen={isFeedCreateOpen} 
                onClose={() => {handleFeedCreateClose()}}
                onCreateStory={(event) => {handleButtonClick(event)}} 
            />
            {/* Main content of the FeedPage can go here */}
            {/* <FeedMain /> */}
        </>
    )
};

export default FeedPage;