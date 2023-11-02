// 구독
import { useState, useRef, useEffect } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";

export default function useSubscribe(topic: string, roomId?: number) {
  const [message, setMessage] = useState("");

  return message;
}
