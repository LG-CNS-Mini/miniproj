import React from "react";
import Notification from "../../component/hook/Notification" ; 

const lst = [
    {
        id : 1 , 
        msg : "한 주 고생많이 하셨고 즐거운 주말 보내세요"
    },
    {
        id : 2 , 
        msg : "말복만 남았네요...말복엔 닭이죠 ㅋㅋ"
    },
    {
        id : 3 , 
        msg : "불금을 즐겁게~~~ 화이팅!!"
    },
]

var timer;

class NotificationPage extends React.Component {
    constructor(props) {
        super(props);

        // 비어 있는 리스트를 만들어 상태를 관리한다.
        this.state = {
            notifications: [],
        };
    }

    
    componentDidMount() {
        const { notifications } = this.state;
        timer = setInterval(() => {
            if (notifications.length < lst.length) {
                const index = notifications.length;
                notifications.push(lst[index]);
                
                // 상태를 업데이트하기 위해서  setState() 함수를 사용하고 있는것에 주의
                this.setState({
                    notifications: notifications,
                });

            } else {
                this.setState({
                    notifications : [] , // notifications 비우기
                })
                clearInterval(timer);
            }
        }, 5000);
    }

    componentWillUnmount() {
        if (timer) {
            clearInterval(timer);
        }
    }

    render() {
        return (
            <div>
                {this.state.notifications.map((notification) => {
                    return (
                        <Notification
                            key={notification.id}
                            id={notification.id}
                            msg={notification.msg}
                        />
                    );
                })}
            </div>
        );
    }
}

export default NotificationPage ;