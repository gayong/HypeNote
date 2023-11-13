import { Button, Modal, Space } from "antd";
import { useState } from "react";

export default function DeleteBtn() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };
  const handleCancel = () => {
    setIsModalOpen(false);
  };
  const handleDelete = () => {
    console.log(1111);
  };

  return (
    <>
      <Button onClick={openModal}>
        <Space>공유목록</Space>
      </Button>
      <Modal open={isModalOpen}>
        정말로 삭제 하시겠습니까
        <Button key="back" onClick={handleCancel}>
          취소
        </Button>
        ,
        <Button key="submit" onClick={handleDelete}>
          확인
        </Button>
        ,
      </Modal>
    </>
  );
}
