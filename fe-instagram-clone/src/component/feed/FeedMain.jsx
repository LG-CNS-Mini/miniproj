import { useState, useEffect } from "react";
import ProfileMain from "../profile/ProfileMain";


const FeedMain = ({feedPage}) => {
    const getPostIdsFromServer = async () => {
        try {
            const userId = localStorage.getItem("userEmail");
            const response = await api.get(`/posts/${userId}`,{
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                }
            });
            return response.data.map(post => post.id);
        } catch (error) {
            console.error("Error fetching post IDs:", error);
            return [];
        }
    };

    useEffect(() => {
        
    }, []);

    return (
        <>
            {feedPage === 'feed' ? <FeedMain/> : null}
            {feedPage === 'profile' ? <ProfileMain/>: null}
        </>
    )
}

export default FeedMain;