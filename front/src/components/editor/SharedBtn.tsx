"use client";

import { Button, Dropdown, MenuProps, Space, message } from "antd";
import { DownOutlined, UserOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";
import useGetSharedMember from "@/hooks/useGetSharedMember";
import useUsersFindByPkList from "@/hooks/useUsersFindByPkList";
import { userInfo } from "os";
import { ReactNode } from "react";
import { FaUserFriends } from "react-icons/fa";
import Image from "next/image";
type Props = {
  id: string;
  owner: number;
};

type userInfo = {
  label: string;
  key: number;
  icon: ReactNode;
};
export default function ShardeBtn({ id, owner }: Props) {
  // const { SharedMember } = useGetSharedMember();
  const getUsersFindByPkList = useUsersFindByPkList();
  const [items, setItem] = useState<userInfo[]>([]);
  const SharedMember = useGetSharedMember();

  useEffect(() => {
    const getSharedMember = async () => {
      const response = await SharedMember.mutateAsync([id]);
      const member = response.data.data.userList;
      const res = await getUsersFindByPkList.mutateAsync(member);
      if (res) {
        const userList: userInfo[] = [];
        res.forEach((element: any) => {
          if (element.userPk === owner) {
            const userinfo = {
              label: "주인",
              key: element.userPk,
              icon: (
                <Image
                  src={element.profileImage}
                  alt="유저 이미지"
                  width={20}
                  height={20}
                  className="w-[20px] h-[20px] rounded-full"></Image>
              ),
            };
            userList.push(userinfo);
          }
        });
        res.forEach((element: any) => {
          if (element.userPk !== owner) {
            const userinfo = {
              label: element.nickName,
              key: element.userPk,
              icon: (
                <Image
                  src={element.profileImage}
                  alt="유저 이미지"
                  width={20}
                  height={20}
                  className="w-[20px] h-[20px] rounded-full"></Image>
              ),
            };
            userList.push(userinfo);
          }
        });

        setItem(userList);
      }
    };

    getSharedMember();
  }, [id, owner]);

  const menuProps = {
    items,
  };
  return (
    <Dropdown menu={menuProps} className="absolute top-5 right-12">
      <FaUserFriends
        className="dark:text-font_primary dark:hover:text-dark_font text-2xl hover:text-dark_font"
        title="공유한 친구목록"
      />
    </Dropdown>
  );
}
