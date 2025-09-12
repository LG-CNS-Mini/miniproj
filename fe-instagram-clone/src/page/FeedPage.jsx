import { useNavigate } from "react-router-dom";
import Navigation from "../component/nav/Navigation";
import FeedCreationModal from "../component/modal/FeedCreationModal";
import { useState } from "react";

const FeedPage = () => {
    const moveURL = useNavigate();
    const {isFeedCreateOpen, setIsFeedCreateOpen} = useState(false);
    const feedCreateModalOpen = () => {
        console.log("Feed Create Modal Open");
        setIsFeedCreateOpen(true);    
    }
    const onClickNavigate = (key) => {
        switch (key) {
            case 'home':
                moveURL('/');
                break;
            case 'search':
                // Handle search navigation
                break;
            case 'create':
                feedCreateModalOpen();
                break;
            default:
                break;
        }

        console.log("Navigating to:", key);
    };

    return (
        <>
            <Navigation onNavigate={onClickNavigate}/>
            <FeedCreationModal 
                isOpen={isFeedCreateOpen} 
                onClose={() => setIsFeedCreateOpen(false)} 
                onCreateStory={() => {}} 
            />
        </>
    )
};

export default FeedPage;