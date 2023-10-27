// 구독
import { useState } from "react";
import { CompatClient } from "@stomp/stompjs";

export default function useSubscribe(
  client: React.MutableRefObject<CompatClient | null>,
  topic: string
) {
  const [message, setMessage] = useState("");

  if (client.current) {
    client.current.subscribe(topic, (message) => {
      setMessage(JSON.parse(message.body));
    });
  }

  return message;
}
