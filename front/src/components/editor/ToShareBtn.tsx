import { Button, Dropdown, MenuProps, Space, message } from "antd";
import { DownOutlined, UserOutlined } from "@ant-design/icons";

export default function ToShareBtn() {
  const handleMenuClick: MenuProps["onClick"] = (e) => {
    message.info("Click on menu item.");
    console.log("click", e);
  };

  const menuProps = {
    onClick: handleMenuClick,
  };
  return (
    <Dropdown menu={menuProps} className="absolute bottom-5 right-10">
      <Button>
        <Space>공유목록</Space>
      </Button>
    </Dropdown>
  );
}
