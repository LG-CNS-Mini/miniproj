import { useState, useEffect } from "react";
import ProfileMain from "../profile/ProfileMain";


const FeedMain = ({feedPage, profileUser}) => {
    return (
        <>
            {feedPage === 'feed'    ? <FeedMain/> : null}
            {feedPage === 'profile' ? <ProfileMain/>: null}
            {feedPage === 'explore' ? <ExploreMain profileUser={profileUser}/> : null}
        </>
    )
}

export default FeedMain;