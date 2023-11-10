"use client";

import SocketEditorProvider from "@/context/SocketEditorProvider";

export default function EditorLayout({ children }: { children: React.ReactNode }) {
  return (
    <SocketEditorProvider>
      {/* 소켓 프로바이더 */}
      <section className="socket">{children}</section>
    </SocketEditorProvider>
  );
}
