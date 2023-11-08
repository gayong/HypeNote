"use client";

import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { useShareDiagram } from "@/hooks/useShareDiagram";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";

export default function SelectShare({ onReceive }: { onReceive: (sharedData: any) => void }) {
  const [selectedFriends, setSelectedFriends] = useState([]);
  const { shareDiagmram, shareInfo } = useShareDiagram();

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
    <>
      <Select
        mode="tags"
        size="middle"
        placeholder="공유받을 친구를 선택하세요"
        style={{ width: 300, marginTop: "10px", zIndex: 9999 }}
        options={[
          { value: 2, label: "2번" },
          { value: 3, label: "3번" },
        ]}
        onChange={handleSelectChange}
      />
      <Button
        className="dark:border dark:border-font_primary"
        style={{ fontFamily: "preRg", backgroundColor: "#2946A2", zIndex: 9999 }}
        type="primary"
        onClick={handleReceive}>
        받기
      </Button>
    </>
  );
}
