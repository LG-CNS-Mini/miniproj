import { useRef } from 'react';
import styled from 'styled-components';
import api from '../../api/axios';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 50px;
`;

const Title = styled.h2`
  margin: 0;
  color: #333;
  font-size: 24px;
`;

const HiddenFileInput = styled.input`
  display: none;
`;

const SelectButton = styled.button`
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #0056b3;
  }

  &:active {
    transform: translateY(1px);
  }
`;

const Description = styled.p`
  color: #666;
  font-size: 14px;
  margin: 0;
  text-align: center;
`;

const FileUploadComponent = (props) => {
  const fileInputRef = useRef(null);

  const handleButtonClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = (event) => {
    const files = event.target.files;
    if (files && files.length > 0) {
      props.setFiles((prev) => [...prev, ...files]);
      props.setPreviewURL(URL.createObjectURL(files[0]));
    }
  };

  return (
    <Container>
      <HiddenFileInput
        ref={fileInputRef}
        multiple
        type="file"
        accept="image/*,video/*"
        onChange={handleFileChange}
      />
      
      <SelectButton onClick={handleButtonClick}>
        컴퓨터에서 파일 선택
      </SelectButton>
    </Container>
  );
};

export default FileUploadComponent;