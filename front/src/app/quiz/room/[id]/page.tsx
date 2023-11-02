"use client";

import QuizRoom from "@/components/quiz/QuizRoom";
import { SocketContext } from "@/context/SocketProvider";
import { useContext, useEffect } from "react";

type Props = {
  params: {
    id: number;
  };
};

export default function QuizRoomPage({ params: { id } }: Props) {
  return (
    <div>
      <QuizRoom roomId={id} />
    </div>
  );
}
