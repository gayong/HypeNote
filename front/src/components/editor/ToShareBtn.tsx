"use client";

import { Alert, Button, Flex, FloatButton, List, Modal } from "antd";
import Search, { SearchProps } from "antd/es/input/Search";
import { useState } from "react";
import { BsFillShareFill } from "react-icons/bs";
import { getOtherUserPkByNickName } from "@/api/service/user";
import { useSharedNote } from "@/hooks/useSharedNote";
// import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";
import useGetUserInfo from "@/hooks/useGetUserInfo";
type Props = {
  id: string;
};

type userList = { userPk: number; nickName: string };
export default function ToShareBtn({ id }: Props) {
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  // const [user] = useAtom(userAtom);
  const { shareDocument } = useSharedNote();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [userList, setUserList] = useState<userList[]>([]);
  const [userPkList, setUserPkList] = useState<number[]>([]);

  const [serchList, setSerchList] = useState<userList[]>([]);

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = async () => {
    if (!user) {
      return;
    }

    try {
      await shareDocument(user.userPk, userPkList, id);
      setIsModalOpen(false);
      setUserList([]);
      setUserPkList([]);
    } catch (error) {
      console.log(error);
    }
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };
  const onSearch: SearchProps["onSearch"] = (value, _e, info) => {
    const getPkbyNickName = async () => {
      try {
        const data = await getOtherUserPkByNickName(value);
        console.log(data);
        if (data.data.data) {
          const user = { userPk: data.data.data.userPk, nickName: value };
          setSerchList([user]);
          console.log(data.data.data.userPk);
        } else {
          setSerchList([]);
        }
      } catch (error) {
        setSerchList([]);
      }
    };
    getPkbyNickName();
  };
  const clickNickName = (item: userList) => {
    console.log(item.userPk in userPkList);
    if (userPkList.includes(item.userPk)) {
      // 특정 item과 userPk를 제거
      setUserList((userList) => userList.filter((user) => user.userPk !== item.userPk));
      setUserPkList((userPkList) => userPkList.filter((userPk) => userPk !== item.userPk));
    } else {
      // 새로운 item과 userPk를 추가
      setUserList((userList) => [...userList, item]);
      setUserPkList((userPkList) => [...userPkList, item.userPk]);
    }
  };

  return (
    <>
      <FloatButton
        tooltip="공유하기"
        icon={<BsFillShareFill />}
        style={{ right: 24, bottom: 30, width: 50, height: 50 }}
        className="hover:-translate-y-2 hover:scale-105 duration-300 hover:bg-dark_font"
        onClick={showModal}
      />
      <Modal
        className="bg-font_primary dark:bg-line_primary"
        title="유저 검색"
        open={isModalOpen}
        onCancel={handleCancel}
        centered
        footer={[
          <Button key="back" onClick={handleCancel}>
            취소
          </Button>,
          <Button key="submit" onClick={handleOk}>
            확인
          </Button>,
        ]}>
        <Search placeholder="유저 닉네임을 검색하세요!" onSearch={onSearch} enterButton />
        <Flex wrap="wrap" gap="small">
          {userList.map((item, index) => (
            <Button
              key={item.userPk}
              onClick={() => clickNickName(item)}
              style={{ backgroundColor: "#a5b3e2", color: "white", marginTop: "10px", marginRight: "5px" }}>
              {item.nickName} x
            </Button>
          ))}
        </Flex>
        <List
          itemLayout="horizontal"
          dataSource={serchList}
          renderItem={(item, index) => (
            <List.Item>
              <List.Item.Meta title={<p onClick={() => clickNickName(item)}>{item.nickName}</p>} />
            </List.Item>
          )}
        />
      </Modal>
    </>
  );
}
