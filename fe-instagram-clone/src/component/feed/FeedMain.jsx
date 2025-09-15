import { useEffect } from "react";
import api from "../../api/axios";

const FeedMain = ({ feedPage, profileUser }) => {
    useEffect(() => {
        // 피드 메인 페이지를 로드합니다.
        api.get("api/v1/post/posts", { params: { page: 1, size: 10 } })
          .then(res => {
            console.log(res.data);
          });
          /**
            authorEmail:"jhs7251@naver.com"
            content:"게시물 등록"
            createDate:"2025-09-14T21:09:38.522564"
            hashtags:[]
            imageUrls:['/images/2025/09/14/27cb76f2-d97b-4e54-8777-60efa5e16186.webp']
            postID:11
           */
    }, [])
    return (
        <>
           
        </>
    );
};

export default FeedMain;
