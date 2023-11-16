import React from "react";
import { MdLogout } from "react-icons/md";
import { Popconfirm } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";
import { useRouter } from "next/navigation";

export default function Logout() {
  const router = useRouter();
  const confirm = () => {
    localStorage.clear();
    router.replace("/");
  };

  const cancel = () => {
    console.log("로그아웃 취소");
  };
  return (
    <Popconfirm
      title={<div className="font-preBd">LOGOUT</div>}
      description={<div className="font-preRg">정말 로그아웃 하시겠습니까?</div>}
      onConfirm={confirm}
      onCancel={cancel}
      okButtonProps={{
        danger: true,
        className: "hover:none bg-red font-preRg",
      }}
      okText="네"
      cancelButtonProps={{ danger: true }}
      cancelText={<div className="font-preRg">아니오</div>}
      icon={<QuestionCircleOutlined style={{ color: "red" }} />}>
      <span className="hover:text-yellow">
        <MdLogout />
      </span>
    </Popconfirm>
  );
}
