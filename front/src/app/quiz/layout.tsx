"use client";

import { SocketProvider } from "@/context/SocketProvider";

export default function QuizLayout({ children }: { children: React.ReactNode }) {
  return (
    <SocketProvider>
      {/* 소켓 프로바이더 */}
      <section className="socket">{children}</section>
    </SocketProvider>
  );
}
