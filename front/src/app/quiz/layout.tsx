"use client";
import React, { useEffect } from "react";
import SocketProvider from "@/context/SocketProvider";
import { useAtom } from "jotai";
import { userAtom } from "@/store/authAtom";
import useGetUserInfo from "@/hooks/useGetUserInfo";

export default function QuizLayout({ children }: { children: React.ReactNode }) {
  const { userInfo } = useGetUserInfo();

  useEffect(() => {
    userInfo();
  }, []);

  return (
    <SocketProvider>
      {/* 소켓 프로바이더 */}
      <section className="socket">{children}</section>
    </SocketProvider>
  );
}
