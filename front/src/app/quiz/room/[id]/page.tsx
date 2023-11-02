"use client";

import { SocketContext } from "@/context/SocketProvider";
import { useContext, useEffect } from "react";

type Props = {
  params: {
    id: number;
  };
};

export default function QuizRoomPage({ params: { id } }: Props) {
  const { sendRoom, client } = useContext(SocketContext);

  useEffect(() => {
    console.log("################################", client, client?.connected);
    if (client && client.connected) {
      console.log(id);
      sendRoom(id);
    }
  }, [client, id, sendRoom]);

  return <div>퀴즈룸이다 인마</div>;
}
