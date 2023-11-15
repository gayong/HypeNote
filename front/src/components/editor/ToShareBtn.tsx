"use client";

import { Button, Flex, FloatButton, List, Modal } from "antd";
import Search, { SearchProps } from "antd/es/input/Search";
import { useEffect, useState } from "react";
import { BsFillShareFill } from "react-icons/bs";
import { useSharedNote } from "@/hooks/useSharedNote";
import { RiUserSearchLine } from "react-icons/ri";
import useGetUserInfo from "@/hooks/useGetUserInfo";
import { useGetUserInfoByNickName } from "@/hooks/useGetUserInfoByNickName";
import { BasicUser } from "@/types/user";
import Image from "next/image";

type Props = {
  id: string;
};

interface ImgUser extends BasicUser {
  profileImage: string;
}

export default function ToShareBtn({ id }: Props) {
  const { data: user } = useGetUserInfo();
  const { shareDocument } = useSharedNote();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [userList, setUserList] = useState<ImgUser[]>([]);
  const [userPkList, setUserPkList] = useState<number[]>([]);
  const [serchList, setSerchList] = useState<ImgUser[]>([]);
  const [searchNickName, setSearchNickName] = useState<string | null>(null);
  const { data: response, isLoading, error } = useGetUserInfoByNickName(searchNickName as string);

  useEffect(() => {
    // 검색 됐을 때
    if (searchNickName) {
      // 검색 결과가 있을 때
      if (response?.data) {
        setSerchList([response?.data.data]);
      } else {
        // message.info("해당 닉네임의 유저가 존재하지 않습니다.");
        setSerchList([]);
      }
      // console.log(response?.data.data);
    }
  }, [response]);

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
    setSearchNickName(value);
  };

  const clickNickName = (item: ImgUser) => {
    console.log(item, "##############################");
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
        tooltip={<div className="font-preBd">공유하기</div>}
        icon={<BsFillShareFill />}
        style={{ right: 24, bottom: 30, width: 50, height: 50 }}
        className="bg-font_primary hover:-translate-y-2 hover:scale-105 duration-300 hover:bg-font_primary"
        onClick={showModal}
      />
      <Modal
        title={
          <div className="font-preBd flex justify-center items-center">
            <RiUserSearchLine />

            <br />
            <span className="ml-3 no-drag">공유하고 싶은 사람을 검색해 주세요</span>
          </div>
        }
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
        <Search placeholder="닉네임을 정확하게 검색하세요!" onSearch={onSearch} enterButton className="font-preBd" />
        {/* 선택된 아이들 */}
        <div className="h-48">
          <Flex wrap="wrap" gap="small" className="mt-2">
            {userList.map((item) => (
              <p
                key={item.userPk}
                onClick={() => clickNickName(item)}
                className="px-2 h-8 rounded-lg text-font_primary flex items-center p-2 hover:cursor-pointer bg-primary hover:bg-hover_primary">
                <Image
                  src={item.profileImage}
                  alt="유저 이미지"
                  width={20}
                  height={20}
                  className="w-[22px] h-[22px] rounded-full"></Image>
                <span className="font-preRg ml-2 text-sm">
                  {item.nickName}
                  <span className="ml-2">x</span>
                </span>
              </p>
            ))}
          </Flex>
          {/* 검색결과 */}
          <List
            itemLayout="horizontal"
            dataSource={serchList}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  key={item.userPk}
                  title={
                    <p onClick={() => clickNickName(item)} className="h-8 flex items-center py-2 hover:cursor-pointer">
                      <Image
                        src={item.profileImage}
                        alt="유저 이미지"
                        width={20}
                        height={20}
                        className="w-[22px] h-[22px] rounded-full"></Image>
                      <span className="font-preRg text-sm ml-2 ">{item.nickName}</span>
                    </p>
                  }
                />
              </List.Item>
            )}
          />
        </div>
      </Modal>
    </>
  );
}
