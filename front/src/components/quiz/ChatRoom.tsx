"use client";

import { useContext, useEffect, useRef, useState } from "react";
import { MyChat, YourChat } from "../ui/chat";
import { Button, Input } from "antd";
import { SocketContext } from "@/context/SocketProvider";
import { chatUser } from "@/types/quiz";

interface QuizRoomProps {
  roomId: number;
}
export default function ChatRoom(props: QuizRoomProps) {
  const [message, setMessage] = useState("");
  const [chatMessages, setChatMessages] = useState<Array<chatUser>>([]);
  const { sendMessage } = useContext(SocketContext);
  const chatEndRef = useRef<null | HTMLDivElement>(null);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(e.target.value);
  };

  const handleSendMessage = () => {
    console.log(message, "를 보냈다.");
    const messageInput = {
      userPk: "2",
      content: message,
      chatTime: new Date().toLocaleString(),
    };
    sendMessage(props.roomId, messageInput);
    setChatMessages([...chatMessages, messageInput]);
    setMessage("");
  };

  useEffect(() => {
    if (chatEndRef.current) {
      chatEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [chatMessages]);

  return (
    <>
      <div className="flex flex-col items-center justify-center w-screen min-h-screen p-10">
        <div className="flex flex-col flex-grow w-full max-w-xl rounded-lg overflow-hidden">
          <div className="flex flex-col flex-grow h-0 p-4 overflow-auto">
            {chatMessages.map((chat, idx) =>
              chat.userPk === "2" ? <MyChat key={idx} {...chat} /> : <YourChat key={idx} {...chat} />
            )}

            {/* 
            <YourChat userPk="2" content="최상익 바보" chatTime="20231027 02:05" />
            <MyChat userPk="2" content="안녕 나는 자현이야 만나서 반가워." chatTime="20231027 02:05" />
            <MyChat userPk="2" content="최상익 바보" chatTime="20231027 02:05" />
            <MyChat userPk="1" content="최상익 바보" chatTime="20231027 02:05" />
            <MyChat userPk="3" content="최상익 바보" chatTime="20231027 02:05" />
            <YourChat userPk="5" content="최상익 바보" chatTime="20231027 02:05" />
            <YourChat userPk="2" content="최상익 바보" chatTime="20231027 02:05" /> */}
          </div>

          <div className="flex justify-between items-center p-2">
            <Input
              className="flex-grow mr-2"
              type="text"
              placeholder="메세지를 입력하시오."
              value={message}
              onChange={handleInputChange}
              onPressEnter={handleSendMessage}
            />

            <Button
              className="dark:border dark:border-font_primary"
              style={{ fontFamily: "preRg", backgroundColor: "#2946A2" }}
              type="primary"
              onClick={handleSendMessage}>
              전송
            </Button>
          </div>
        </div>
      </div>
    </>
  );
}
