"use client";

import { useEffect, useRef, useState, useMemo } from "react";
import { useRouter } from "next/navigation";
import { useShareDiagram } from "@/hooks/useShareDiagram";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";
import { useShareMemberList } from "@/hooks/useGetShareUserList";
import { ShareMember } from "@/types/diagram";
import Pendulum from "../../../public/assets/pen.gif";
import Image from "next/image";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";

export default function SelectShare({ onReceive }: { onReceive: (sharedData: any) => void }) {
  const [selectedFriends, setSelectedFriends] = useState([]);
  const { shareDiagmram, shareInfo } = useShareDiagram();
  const { data: response, isLoading, error } = useShareMemberList();
  const [responseReady, setResponseReady] = useState(true);
  console.log(response, 111);

  const userOptions = useMemo(() => {
    if (response) {
      console.log(response, 111);
      setResponseReady(true);
      return response.map((user: ShareMember) => ({
        value: user.userPk,
        label: user.nickName,
      }));
    } else {
      return [];
    }
  }, [response]);

  const handleReceive = async () => {
    console.log(selectedFriends, "버튼클릭");
    if (selectedFriends.length === 0) {
      onReceive(null);
      return;
    }

    if (onReceive && shareDiagmram) {
      setResponseReady(false);
      const info = await shareDiagmram.mutateAsync({ members: selectedFriends });
      setResponseReady(true);
      console.log(1111, info);
      onReceive(info);
    }
  };

  const handleSelectChange = (value: any) => {
    setSelectedFriends(value);
    console.log(selectedFriends, "친구 선택");
  };

  return (
    <div className="relative mt-4 ml-8">
      <Select
        mode="tags"
        size="middle"
        placeholder="내 노트와 친구 노트를 합쳐보세요!"
        style={{ width: 230, marginTop: "10px", zIndex: 9999 }}
        options={userOptions}
        value={selectedFriends}
        onChange={handleSelectChange}
      />
      <Button
        className="dark:border dark:border-font_primary h-[30px] w-[53px] ml-2 font-preBd hover:bg-dark_font bg-primary z-50 scrollbar-hide"
        type="primary"
        onClick={handleReceive}>
        {responseReady ? "GO" : <Spin indicator={<LoadingOutlined style={{ fontSize: 16, color: "white" }} spin />} />}
      </Button>
    </div>
  );
}
