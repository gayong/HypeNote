"use client";

import { Button, Dropdown, MenuProps, Space, message } from "antd";
import { DownOutlined, UserOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";
import useGetSharedMember from "@/hooks/useGetSharedMember";
import useUsersFindByPkList from "@/hooks/useUsersFindByPkList";
import { userInfo } from "os";
import { ReactNode } from "react";
import { FaUserFriends } from "react-icons/fa";
type Props = {
  id: string;
};

type userInfo = {
  label: string;
  key: number;
  icon: ReactNode;
};
export default function ShardeBtn({ id }: Props) {
  const { SharedMember } = useGetSharedMember();
  const { getUsersFindByPkList } = useUsersFindByPkList();
  const [items, setItem] = useState<userInfo[]>([]);

  useEffect(() => {
    const getSharedMember = async () => {
      try {
        const response = await SharedMember([id]);
        if (response) {
          const member = response.data.data.userList;
          // const member = [9];
          const res = await getUsersFindByPkList(member);
          if (res) {
            const userList: userInfo[] = [];
            res.data.forEach((element: any) => {
              console.log(element);
              const userinfo = {
                label: element.nickName,
                key: element.userPk,
                icon: <UserOutlined />,
              };
              console.log(userinfo);
              userList.push(userinfo);
            });
            setItem(userList);
          }
        }
      } catch (error) {
        console.log(error);
      }
    };
    getSharedMember();
  }, [id]);

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
