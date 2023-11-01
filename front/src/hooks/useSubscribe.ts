// 구독
import { useState, useRef, useEffect } from "react";
import useConnectSocket from "./useConnectSocket";
import { Stomp, CompatClient } from "@stomp/stompjs";

export default function useSubscribe(topic: string, roomId?: number) {
  const [message, setMessage] = useState("");

  useConnectSocket(topic, roomId);

  return message;
}
