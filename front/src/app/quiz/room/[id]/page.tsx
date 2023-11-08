"use client";

import Loading from "@/app/loading";
import QuizRoom from "@/components/quiz/QuizRoom";
import { useContext, useEffect } from "react";
import { useWebSocket } from "@/context/SocketProvider";
import SubscribeProvider from "@/context/SubscribeProvider";
import { useAtom } from "jotai";
import { userAtom } from "@/store/authAtom";

type Props = {
  params: {
    id: number;
  };
};

export default function QuizRoomPage({ params: { id } }: Props) {
  const stompClient = useWebSocket();
  const [user] = useAtom(userAtom);

  useEffect(() => {
    if (stompClient) {
      // stompClient.subscribe(`/sub/quiz/${id}`, (response) => {});

      const data = {
        userPk: user.userPk,
        userName: user.nickName,
        userImg: user.profileImage,
      };

      stompClient.send(`/pub/quizroom/in/${id}`, {}, JSON.stringify(data));
    }
  }, []);

  return (
    <SubscribeProvider roomId={id}>
      <section className="h-[100vh] flex items-center justify-center">
        <QuizRoom roomId={id} />
      </section>
    </SubscribeProvider>
  );
}
