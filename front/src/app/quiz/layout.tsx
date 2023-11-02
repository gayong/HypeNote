"use client";

import { SocketProvider } from "@/context/SocketProvider";

export default function QuizLayout({ children }: { children: React.ReactNode }) {
  return (
    <SocketProvider>
      <section>{children}</section>
    </SocketProvider>
  );
}
