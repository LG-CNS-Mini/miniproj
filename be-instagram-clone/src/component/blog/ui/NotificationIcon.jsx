// 단순 문자열 보여주는 코드 
// import React, { useState } from "react";
// import { FaBell } from "react-icons/fa";
// import styled from "styled-components";

// const NotificationWrapper = styled.div`
//   position: relative;
//   display: inline-block;
//   cursor: pointer;
// `;

// const Badge = styled.span`
//   position: absolute;
//   top: -5px;
//   right: -10px;
//   background: red;
//   color: white;
//   border-radius: 50%;
//   padding: 3px 6px;
//   font-size: 12px;
//   font-weight: bold;
// `;

// const Dropdown = styled.div`
//   position: absolute;
//   top: 30px;
//   right: 0;
//   width: 200px;
//   max-height: 300px;
//   overflow-y: auto;
//   background: white;
//   border: 1px solid #ccc;
//   border-radius: 8px;
//   box-shadow: 0 5px 15px rgba(0,0,0,0.2);
//   z-index: 100;
// `;

// const NotificationItem = styled.div`
//   padding: 10px;
//   border-bottom: 1px solid #eee;
//   font-size: 14px;

//   &:last-child {
//     border-bottom: none;
//   }

//   &:hover {
//     background-color: #f5f5f5;
//   }
// `;

// const NotificationIcon = ({ count, notifications }) => {
  
//   const [open, setOpen] = useState(false);

//   const toggleDropdown = () => setOpen(prev => !prev);

//   return (
//     <NotificationWrapper>
//       <FaBell size={24} onClick={toggleDropdown} />
//       {count > 0 && <Badge>{count}</Badge>}

//       {open && (
//         <Dropdown>
//           {notifications.length === 0 ? (
//             <NotificationItem>알림이 없습니다.</NotificationItem>
//           ) : (
//             notifications.map((note, idx) => (
//               <NotificationItem key={idx}>{note}</NotificationItem>
//             ))
//           )}
//         </Dropdown>
//       )}
//     </NotificationWrapper>
//   );
// };

// export default NotificationIcon;




import React, { useState } from "react";
import { FaBell } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const NotificationWrapper = styled.div`
  position: relative;
  display: inline-block;
  cursor: pointer;
`;

const Badge = styled.span`
  position: absolute;
  top: -5px;
  right: -10px;
  background: red;
  color: white;
  border-radius: 50%;
  padding: 3px 6px;
  font-size: 12px;
  font-weight: bold;
`;

const Dropdown = styled.div`
  position: absolute;
  top: 30px;
  right: 0;
  width: 250px;
  max-height: 300px;
  overflow-y: auto;
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.2);
  z-index: 100;
`;

const NotificationItem = styled.div`
  padding: 10px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  cursor: pointer;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background-color: #f5f5f5;
  }
`;

const Modal = styled.div`
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translateX(-50%);
  width: 400px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.3);
  padding: 20px;
  z-index: 200;
`;

const GoButton = styled.button`
  background: #1976d2; color: white; border: none; padding: 8px 12px;
  border-radius: 4px; cursor: pointer;
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  gap: 10px; /* 버튼 사이 간격 */
  margin-top: 15px;
`;

// 객체 메시지로 받아서 모달 띄우는 코드
const NotificationIcon = ({ count, notifications }) => {
  const [open, setOpen] = useState(false);
  const [selectedMessage, setSelectedMessage] = useState(null);

  const toggleDropdown = () => setOpen(prev => !prev);
  const moveURL = useNavigate();

  const handleClick = (note) => {
    setSelectedMessage(note); // 클릭 시 모달로 메시지 확인
  };

  const handleGo = () => {
    console.log(">>>>>>>> hadleGo " , selectedMessage.link ); 
    moveURL(`..${selectedMessage.link}`, { state: selectedMessage });
    // onClose();
  };

  
  return (
    <NotificationWrapper>
      <FaBell size={24} onClick={toggleDropdown} />
      {count > 0 && <Badge>{count}</Badge>}

      {open && (
        <Dropdown>
          {notifications.length === 0 ? (
            <NotificationItem>알림이 없습니다.</NotificationItem>
          ) : (
            notifications.map((note, idx) => (
              <NotificationItem key={idx} onClick={() => handleClick(note)}>
                {note.title}
              </NotificationItem>
            ))
          )}
        </Dropdown>
      )}

      {selectedMessage && (
        <Modal>
          <h3>{selectedMessage.title}</h3>
          <p>{selectedMessage.content}</p>
         
            {selectedMessage.link && <GoButton onClick={handleGo}>바로가기</GoButton>}
            &nbsp;&nbsp;
            <GoButton onClick={() => setSelectedMessage(null)}>닫기</GoButton>
     
        </Modal>
      )}
    </NotificationWrapper>
  );
};

export default NotificationIcon;
