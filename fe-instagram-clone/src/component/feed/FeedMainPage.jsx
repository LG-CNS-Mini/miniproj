import { useState, useEffect } from "react";
import ProfileMain from "../profile/ProfileMain";
import FeedMain from "./FeedMain";


const FeedMainPage = ({feedPage, profileUser}) => {
    return (
        <>
            {feedPage === 'feed'    ? <FeedMain/> : null}
            {feedPage === 'profile' ? <ProfileMain/>: null}
            {feedPage === 'explore' ? <ProfileMain profileUser={profileUser}/> : null}
        </>
    )
}

export default FeedMainPage;