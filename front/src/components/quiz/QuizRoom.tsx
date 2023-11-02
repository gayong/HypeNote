"use client";

import { SocketContext } from "@/context/SocketProvider";
import { useContext, useEffect } from "react";

interface QuizRoomProps {
  roomId: number;
}

export default function QuizRoom(props: QuizRoomProps) {
  const { setRoomNumber } = useContext(SocketContext);

  useEffect(() => {
    setRoomNumber(props.roomId);
    console.log(props.roomId);
  }, []);

  return (
    <div>
      <h1 className="text-3xl font-bold">퀴즈방</h1>
      <div>{props.roomId}</div>
    </div>
  );
}
