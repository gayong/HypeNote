"use client";

import { useContext, useEffect, useRef, useState } from "react";
import { MyChat, YourChat } from "../ui/chat";
import { Button, Input } from "antd";
import { SocketContext } from "@/context/SubscribeProvider";
import { chatUser } from "@/types/quiz";
import { useWebSocket } from "@/context/SocketProvider";

interface QuizRoomProps {
  roomId: number;
  height: number;
}
export default function ChatRoom(props: QuizRoomProps) {
  const [message, setMessage] = useState("");

  const { chatMessages } = useContext(SocketContext);
  const chatEndRef = useRef<null | HTMLDivElement>(null);
  const stompClient = useWebSocket();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(e.target.value);
  };

  const handleSendMessage = () => {
    console.log(message, "를 보냈다.");

    const messageInput = {
      userPk: "2",
      userName: "자롱이",
      content: message,
      chatTime: new Date().toLocaleString(),
    };
    if (stompClient) {
      stompClient.send(`/pub/chat/${props.roomId}`, {}, JSON.stringify(messageInput));
    }
    // sendMessage(props.roomId, messageInput);
    // setChatMessages([...chatMessages, messageInput]);
    setMessage("");
  };

  useEffect(() => {
    if (chatEndRef.current) {
      chatEndRef.current.scrollIntoView({ behavior: "smooth", block: "nearest" });
    }
  }, [chatMessages]);

  return (
    <>
      <div
        style={{ height: `${props.height}vh` }}
        className="outline outline-offset-2 outline-2 outline-line_primary flex flex-col flex-grow w-full rounded-lg overflow-hidden">
        <div className="flex flex-col flex-grow h-0 p-4 overflow-auto min-h-min">
          {chatMessages.map((chat, idx) =>
            chat.userPk === 2 ? <MyChat key={idx} {...chat} /> : <YourChat key={idx} {...chat} />
          )}
          <div ref={chatEndRef} />
        </div>

        <div className="flex justify-between items-center p-2">
          <Input
            className="flex-grow mr-2 dark:bg-dark_primary dark:text-font_primary"
            type="text"
            placeholder="메세지를 입력하시오."
            value={message}
            onChange={handleInputChange}
            onPressEnter={handleSendMessage}
          />

          <Button
            className="bg-primary dark:border-none dark:border-font_primary font-preRg"
            type="primary"
            onClick={handleSendMessage}>
            전송
          </Button>
        </div>
      </div>
    </>
  );
}
