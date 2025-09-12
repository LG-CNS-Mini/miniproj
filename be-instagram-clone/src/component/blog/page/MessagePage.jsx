import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styled from "styled-components";
import Button from "../ui/Button";

const Container = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 50px;
`;

const Card = styled.div`
  width: 90%;
  max-width: 600px;
  padding: 24px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
  border: 1px solid #eee;
`;

const Title = styled.h2`
  margin-bottom: 16px;
  color: #333;
  font-size: 24px;
`;

const Content = styled.p`
  margin-bottom: 12px;
  font-size: 16px;
  color: #555;
`;

const Type = styled.span`
  display: inline-block;
  padding: 4px 8px;
  background-color: #f0f0f0;
  color: #666;
  border-radius: 6px;
  font-size: 14px;
  margin-bottom: 12px;
`;

const LinkButton = styled.a`
  display: inline-block;
  padding: 8px 16px;
  background-color: #1976d2;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  margin-top: 12px;

  &:hover {
    background-color: #1565c0;
  }
`;

const MessagePage = () => {
    const moveURL = useNavigate();
    const location = useLocation();
    const message = location.state; // 모달에서 전달된 메시지 객체

    if (!message) return <Container>메시지 데이터가 없습니다.</Container>;

    return (
        <Container>
        <Card>
            <Title>{message.title}</Title>
            <Content>{message.content}</Content>
            <Type>타입: {message.type}</Type>
            {message.link && (
            <div>
                <Button title="메인페이지 이동"
                        btnHandler={() => {
                            moveURL("/blog");
                        }} />
            </div>
            )}
        </Card>
        </Container>
    );
};

export default MessagePage;