"use client";

import Loading from "@/app/loading";
import QuizRoom from "@/components/quiz/QuizRoom";
import { useContext, useEffect } from "react";
import { useWebSocket } from "@/context/SocketProvider";
import SubscribeProvider from "@/context/SubscribeProvider";

type Props = {
  params: {
    id: number;
  };
};

export default function QuizRoomPage({ params: { id } }: Props) {
  const stompClient = useWebSocket();

  useEffect(() => {
    if (stompClient) {
      const data = {
        userPk: "2",
        userName: "isc",
      };

      stompClient.send(`/pub/quizroom/in/${id}`, {}, JSON.stringify(data));
    }
  }, []);

  return (
    <SubscribeProvider roomId={id}>
      <section className="mx-5 my-20">
        <QuizRoom roomId={id} />
      </section>
    </SubscribeProvider>
  );
}
