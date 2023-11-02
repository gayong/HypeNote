"use client";

import { SocketContext } from "@/context/SocketProvider";
import { useContext, useEffect } from "react";

interface QuizRoomProps {
  roomId: number;
}

export default function QuizRoom(props: QuizRoomProps) {
  const { sendRoom, client } = useContext(SocketContext);
  useEffect(() => {
    console.log(client?.connected);
  }, []);

  return (
    <div>
      <h1 className="text-3xl font-bold">퀴즈방</h1>
      <div>{props.roomId}</div>
    </div>
  );
}
