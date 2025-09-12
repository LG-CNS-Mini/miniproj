import axios from "axios"; 

const url = process.env.REACT_APP_JSON_SERVER_URL;
console.log("[debug] >>> ref env : " , url);

const api = axios.create({
    baseURL : 'http://localhost:8088',
    withCredentials : true 
});



export default api ; 

/*
비동기 통신을 할 때 브라우저 보안 정책으로 인한 CORS 문제가 발생한다 
Cross-Origin-Resource Sharing 

인터셉터(request , response) 개념을 이용해서 
- 인증된 사용자의 액세스토큰 확인
- 토큰이 만료된 경우 자동 재발급 요청
*/

