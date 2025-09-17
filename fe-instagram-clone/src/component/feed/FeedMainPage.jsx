import { useState, useEffect } from "react";
import ProfileMain from "../profile/ProfileMain";
import FeedMain from "./FeedMain";


const FeedMainPage = ({feedPage, profileUser, userImageUrl, setUserImageUrl}) => {
    return (
        <>
            {feedPage === 'feed'    ? <FeedMain/> : null}
            {feedPage === 'profile' ? <ProfileMain 
                                        setUserImageUrl={setUserImageUrl} 
                                        userImageUrl={userImageUrl}/> : null}
            {feedPage === 'explore' ? <ProfileMain 
                                        setUserImageUrl={setUserImageUrl}
                                        userImageUrl={userImageUrl}
                                        profileUser={profileUser}/> : null}
        </>
    )
}

export default FeedMainPage;