"use client";

import { useEditorWebSocket } from "@/context/SocketEditorProvider";
import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
// import { useEffect, useState } from "react";

type Props = {
  params: {
    id: string;
  };
};

export default function EditorPage({ params: { id } }: Props) {
  const TestEditor = dynamic(() => import("@/components/editor/TestEditor"), { ssr: false });
  const TestEditorNotFirst = dynamic(() => import("@/components/editor/TestEditorNotFirst"), { ssr: false });
  const stompClient = useEditorWebSocket();
  const array = Array.from({ length: 100 }, (_, index) => index + 1);
  const randomValue = array[Math.floor(Math.random() * array.length)];
  const [first, setFirst] = useState(0);
  // useEffect(() => {
  //   if (stompClient) {
  //     stompClient.subscribe(`/sub/note/connection/${id}`, (message) => {
  //       const payload = JSON.parse(message.body);
  //       if (first === 0) {
  //         setFirst(payload.length);
  //       }
  //     });
  //     stompClient.send(`/pub/note/connection/${id}`, {}, JSON.stringify({ userId: randomValue }));
  //   }
  //   return () => {
  //     if (stompClient) {
  //       stompClient.send(`/pub/note/disconnection/${id}`, {}, JSON.stringify({ userId: randomValue }));
  //       stompClient.unsubscribe(`/sub/note/connection/${id}`);
  //     }
  //   };
  // }, [stompClient]);

  // return <> {first === 0 ? null : first === 1 ? <TestEditor id={id} /> : <TestEditorNotFirst id={id} />}</>;
  return (
    <>
      <TestEditor id={id} />
    </>
  );
}
