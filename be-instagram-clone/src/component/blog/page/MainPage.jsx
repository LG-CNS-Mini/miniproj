import { useNavigate } from "react-router-dom";

import styled from "styled-components";
import Button from "../ui/Button";
import BlogList from "../list/BlogList";
import { useEffect, useState } from "react";
import api from "../../../api/axios";
import NotificationIcon from "../ui/NotificationIcon";

const Wrapper = styled.div`
    padding: 16px;
    width: calc(100% - 32px);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
`;

const Container = styled.div`
    width: 100%;
    max-width: 720px;

    & > * {
        :not(:last-child) {
            margin-bottom: 16px;
        }
    }
`;

const WelcomeMessage = styled.div`
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 16px;
    color: #333;
`;

const LogoutButton = styled(Button)`
    background-color: #f44336;
    color: white;

    &:hover {
        background-color: #d32f2f;
    }
`;


const MainPage = () => {
    console.log("[debug] MainPage start : ");
    
    const email = localStorage.getItem("userEmail"); 
    console.log("[debug] MainPage localStorage eamil  : " + email );
    
    const moveUrl = useNavigate();

    // sse
    // const [notification, setNotification] = useState("");
    const [count, setCount] = useState(0);
    const [notifications, setNotifications] = useState([]);

    const [ary , setAry] = useState([]);

    const [username, setUsername] = useState("");
    const [useremail, setUseremail] = useState("");
    const getData = async () => {
        // api 이용해서 통신 상태데이터로 할당
        // backend 서버와 통신 (Swagger API 보면서 작업) 
        console.log("[debug] MainPage getData call : ");
        
        console.log("[debug] api baseURL : " , api.defaults.baseURL);
        const token = localStorage.getItem("accessToken");
        console.log("[debug] api token : " , token);

        await api.get('/auth/api/v2/blog/blogs',{
                headers: {
                    Authorization: token ? `${token}` : ""
                }
            })
            .then( response => {    
                console.log("[debug] >>> post response      : " , response );
                console.log("[debug] >>> post response data : " , response.data );
                setAry(response.data) ;   
            })
            .catch( error => {
                console.log("[debug] >>> blogs get error");
            });
    }

    useEffect(() => {
        getData() ;
        const user = localStorage.getItem("userInfo"); 
        const email = localStorage.getItem("userEmail"); 
        if(user && email ) {
            setUsername(user);
            setUseremail(email);
        }

        // SSE 구독 (로그인 후 실행됨)
        if(email) {
            // 1. 기존 알림 가져오기
            api.get(`/api/v2/inspire/sse/notifications/${email}`)
                .then(res => {
                    setNotifications(res.data);
                    setCount(res.data.length);
                })
                .catch(err => console.error("알림 조회 실패", err));

            // 2. SSE 구독
            console.log("[debug] >>> MainPage login 이후 구독 ");
            const evtSource = new EventSource(`http://localhost:8088/api/v2/inspire/sse/subscribe/${email}`);
            
            evtSource.addEventListener("login", (event) => {
                console.log("[SSE] 로그인 푸시 메시지:", event.data);
                setCount(prev => prev + 1); // 카운트 증가
                setNotifications(prev => [event.data, ...prev]); // 최신 알림 위로
                
                // setTimeout(() => {
                //     alert(event.data);
                // }, 0);

                // React에서 alert보다 **브라우저 알림(Notification API)**를 
                // 쓰면 더 자연스럽게 알림 가능:
                if (Notification.permission === "granted") {
                    new Notification("새 알림", { body: event.data });
                } else if (Notification.permission !== "denied") {
                    Notification.requestPermission().then(permission => {
                        if (permission === "granted") {
                            new Notification("새 알림", { body: event.data });
                        }
                    });
                }

            });

            evtSource.onerror = (err) => {
                console.error("[SSE] 에러 발생:", err);
                evtSource.close();
            };

            return () => {
                evtSource.close();
            };
        }
    }, [email ]); 


    const handleLogout = async () => {
        const token = localStorage.getItem("accessToken");
        await api.post('/auth/api/v2/inspire/user/logout', null, {
                headers: { Authorization: `${token}` }
            })
            .then( response => {    
                localStorage.removeItem("accessToken");
                localStorage.removeItem("refreshToken");
                localStorage.removeItem("userInfo");
                moveUrl("/"); // 로그인 페이지로 이동
            })
            .catch( error => {
                console.log("[debug] >>> user logout error");
            });
        
        
    };


    return(
        <Wrapper>
            <Container>
                {username && <WelcomeMessage>{username}님 환영합니다.</WelcomeMessage>}
                
                

                <Button title="글 작성하기" btnHandler={() => {
                        moveUrl('/blog-write') ;   
                }} />
               
               <LogoutButton
                    title="로그아웃"
                    btnHandler={handleLogout}
                />

                <NotificationIcon count={count} notifications={notifications} />

                
               <BlogList blogs={ary} />

            </Container>
        </Wrapper>
    );
}

export default MainPage ;