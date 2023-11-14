"use client";

import { useEffect, useRef, useState, useMemo } from "react";
import { useRouter } from "next/navigation";
import { useShareDiagram } from "@/hooks/useShareDiagram";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";
import { useShareMemberList } from "@/hooks/useGetShareUserList";
import { ShareMember } from "@/types/diagram";

export default function SelectShare({ onReceive }: { onReceive: (sharedData: any) => void }) {
  const [selectedFriends, setSelectedFriends] = useState([]);
  const { shareDiagmram, shareInfo } = useShareDiagram();
  const { data: response, isLoading, error } = useShareMemberList();
  console.log(response, 111);

  const userOptions = useMemo(() => {
    if (response) {
      console.log(response, 111);
      return response.map((user: ShareMember) => ({
        value: user.userPk,
        label: user.nickName,
      }));
    } else {
      return [];
    }
  }, [response?.data]);

  const handleReceive = async () => {
    console.log(selectedFriends, "버튼클릭");
    if (selectedFriends.length === 0) {
      onReceive(null);
      return;
    }

    if (onReceive && shareDiagmram) {
      const info = await shareDiagmram.mutateAsync({ members: selectedFriends });
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
        GO
      </Button>
    </div>
  );
}
