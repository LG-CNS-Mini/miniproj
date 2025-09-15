import { useState, useEffect } from "react";
import ProfileMain from "../profile/ProfileMain";


const FeedMain = ({feedPage}) => {
    
    useEffect(() => {
        
    }, []);

    return (
        <>
            {feedPage === 'feed'    ? <FeedMain/> : null}
            {feedPage === 'profile' ? <ProfileMain/>: null}
        </>
    )
}

export default FeedMain;