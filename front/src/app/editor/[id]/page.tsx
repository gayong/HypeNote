"use client";

import { useEditorWebSocket } from "@/context/SocketEditorProvider";
import dynamic from "next/dynamic";
// import { useEffect, useState } from "react";

type Props = {
  params: {
    id: string;
  };
};

export default function EditorPage({ params: { id } }: Props) {
  const TestEditor = dynamic(() => import("@/components/editor/TestEditor"), { ssr: false });
  const TestEditorNotFirst = dynamic(() => import("@/components/editor/TestEditorNotFirst"), { ssr: false });
  // const stompClient = useEditorWebSocket();
  const array = Array.from({ length: 100 }, (_, index) => index + 1);
  const randomValue = array[Math.floor(Math.random() * array.length)];
  // const [first, setFirst] = useState(0);
  // useEffect(() => {
  //   if(stompClient){
  //     stompClient.subscribe("/sub/")
  //   }
  // }, [stompClient]);

  return <> {id === "1" ? <TestEditor id={id} /> : <TestEditorNotFirst id={id} />}</>;
}
