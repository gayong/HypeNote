"use client";

import Loading from "@/app/loading";
import QuizRoom from "@/components/quiz/QuizRoom";
import { SocketContext } from "@/context/SocketProvider";
import { useEffect, useContext } from "react";

type Props = {
  params: {
    id: number;
  };
};

export default function QuizRoomPage({ params: { id } }: Props) {
  const { setRoomNumber, room } = useContext(SocketContext);

  useEffect(() => {
    setRoomNumber(id);
  }, []);

  return (
    <div>
      <QuizRoom roomId={id} />
    </div>
  );
}
