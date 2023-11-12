import { Button, Dropdown, MenuProps, Space, message } from "antd";
import { DownOutlined, UserOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";
import useGetSharedMember from "@/hooks/useGetSharedMember";
import useUsersFindByPkList from "@/hooks/useUsersFindByPkList";
import { userInfo } from "os";
type Props = {
  id: string;
};
export default function ShardeBtn({ id }: Props) {
  const { SharedMember } = useGetSharedMember();
  const { getUsersFindByPkList } = useUsersFindByPkList();
  const handleMenuClick: MenuProps["onClick"] = (e) => {
    message.info("Click on menu item.");
    console.log("click", e);
  };
  const [items, setItem] = useState<MenuProps[]>([]);

  useEffect(() => {
    const getSharedMember = async () => {
      try {
        const response = await SharedMember([id]);
        if (response) {
          const member = response.data.data.userList;
          // const member = [9];
          const res = await getUsersFindByPkList(member);
          if (res) {
            const userList: MenuProps[] = [];
            res.data.forEach((element) => {
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
    onClick: handleMenuClick,
  };
  return (
    <Dropdown menu={menuProps} className="absolute top-5 right-10">
      <Button>
        <Space>공유목록</Space>
      </Button>
    </Dropdown>
  );
}
