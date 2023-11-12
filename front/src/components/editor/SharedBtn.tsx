import { Button, Dropdown, MenuProps, Space, message } from "antd";
import { DownOutlined, UserOutlined } from "@ant-design/icons";
import { useState, useEffect } from "react";
import useGetSharedMember from "@/hooks/useGetSharedMember";
import useUsersFindByPkList from "@/hooks/useUsersFindByPkList";
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
  const [userList, setUserList] = useState([]);
  const [items, setItem] = useState<MenuProps[]>([]);
  // const items: MenuProps["items"] = [
  //   {
  //     label: "권인식",
  //     key: "1",
  //     icon: <UserOutlined />,
  //   },
  //   {
  //     label: "심규렬",
  //     key: "2",
  //     icon: <UserOutlined />,
  //   },
  //   {
  //     label: "윤자현",
  //     key: "3",
  //     icon: <UserOutlined />,
  //   },
  //   {
  //     label: "이가영",
  //     key: "4",
  //     icon: <UserOutlined />,
  //   },
  //   {
  //     label: "최상익",
  //     key: "5",
  //     icon: <UserOutlined />,
  //   },
  // ];
  useEffect(() => {
    const getSharedMember = async () => {
      try {
        const response = await SharedMember([id]);
        if (response) {
          // const member = response.data.data.userList;
          const member = [9];
          const res = await getUsersFindByPkList(member);
          if (res) {
            console.log(res.data);
            const userList: MenuProps[] = [];
            res.forEach((element) => {
              const userinfo = {
                label: element.nickName,
                key: element.userPk,
                icon: <UserOutlined />,
              };
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
