import Link from "next/link";
import React, { useEffect, useState } from "react";
import { Button, Drawer, Input } from "antd";
import { useAtom } from "jotai";
import { isSearchOpen } from "../../store/searchOpen";

export default function Search() {
  const [open, setOpen] = useAtom(isSearchOpen);
  const { Search } = Input;

  const showDrawer = () => {
    setOpen(true);
    console.log("서랍 열었다");
  };

  const onClose = () => {
    setOpen(false);
    console.log("서랍 닫았다");
  };

  const handleEnter = () => {
    console.log("엔터눌러서 검색");
  };

  return (
    <div>
      <Button
        className="bg-primary absolute top-5 -right-5 rounded-b-none rotate-[270deg]"
        type="primary"
        onClick={showDrawer}>
        웹 검색
      </Button>
      <Drawer
        title="웹 검색"
        placement="right"
        onClose={onClose}
        open={open}
        zIndex={0}
        mask={false}
        maskClosable={false}
        width={300}
        className="relative">
        <Search placeholder="검색어를 입력해주세요" enterButton onPressEnter={handleEnter} />
      </Drawer>
    </div>
  );
}
