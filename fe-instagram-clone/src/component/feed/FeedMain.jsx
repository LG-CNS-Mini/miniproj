import { use, useEffect } from "react";


const FeedMain = () => {
    const [] = useState(null);
    
    const getPostIdsFromServer = async () => {
        try {
            const userId = localStorage.getItem("userInfo").userId;
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
        
    )
}

export default FeedMain;