import { Button, Dropdown, MenuProps, Space, message } from "antd";
import { DownOutlined, UserOutlined } from "@ant-design/icons";

export default function ShardeBtn() {
  const handleMenuClick: MenuProps["onClick"] = (e) => {
    message.info("Click on menu item.");
    console.log("click", e);
  };

  const items: MenuProps["items"] = [
    {
      label: "권인식",
      key: "1",
      icon: <UserOutlined />,
    },
    {
      label: "심규렬",
      key: "2",
      icon: <UserOutlined />,
    },
    {
      label: "윤자현",
      key: "3",
      icon: <UserOutlined />,
    },
    {
      label: "이가영",
      key: "4",
      icon: <UserOutlined />,
    },
    {
      label: "최상익",
      key: "5",
      icon: <UserOutlined />,
    },
  ];

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
