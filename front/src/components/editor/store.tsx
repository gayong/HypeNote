"use client";

import { syncedStore, getYjsDoc } from "@syncedstore/core";
import SockJS from "sockjs-client";
import { Stomp, Frame } from "@stomp/stompjs";
import * as Y from "yjs";
import type { CompatClient } from "@stomp/stompjs";

type Payload = {
  content: (number | number[])[];
  type: string;
};
type Todo = { completed: boolean; title: string };
type update = { content: (number | number[])[]; type: string };

export const store = syncedStore({ todos: [] as Todo[], fragment: "xml" });
const yDoc = getYjsDoc(store);
export let firstConnected = 1;
let messageId = 0;
let pendingUpdates: { [key: number]: any[] } = {};

export function connectStompClient(id: string, stompClient: CompatClient) {
  if (stompClient) {
    stompClient.subscribe(`/sub/note/${id}`, (message) => {
      const payload = JSON.parse(message.body);
      const enter = payload.type;
      if (enter === "1") {
        const initialVal = Y.encodeStateAsUpdate(yDoc);
        if (initialVal.length) {
          chunkMessage(initialVal);
        }
        return;
      }

      const arr = new Uint8Array(payload.content[0]);
      const content = payload.content[0];
      const chunkId = payload.content[1];
      const totalChunks = payload.content[2];
      const messageId = payload.content[3];

      if (!pendingUpdates[messageId]) {
        pendingUpdates[messageId] = new Array(totalChunks).fill(null);
      }
      pendingUpdates[messageId][chunkId] = new Array(content);

      if (!pendingUpdates[messageId].includes(null)) {
        const concatenated = pendingUpdates[messageId].reduce((acc, val) => acc.concat(val), []);
        const update = [];

        for (let i = 0; i < totalChunks; i++) {
          update.push(...concatenated[i]);
        }

        const newUpdate = new Uint8Array(update);
        console.log(newUpdate);
        Y.applyUpdate(yDoc, newUpdate);
        delete pendingUpdates[messageId];
      }
    });

    if (firstConnected === 1) {
      firstConnected = 0;
      // stompClient.send(`/pub/note/${id}`, {}, JSON.stringify({ type: "1" }));
      stompClient.send(`/pub/note/${id}`, {}, JSON.stringify({ type: "1" }));
    }
    // });

    function sendYjsUpdate(update: Payload) {
      // stompClient.send(`/pub/note/${id}`, {}, JSON.stringify(update));
      stompClient.send(`/pub/note/${id}`, {}, JSON.stringify(update));
    }

    yDoc.on("update", (update) => {
      try {
        chunkMessage(update);
      } catch (error) {
        console.log(error);
      }
    });

    function chunkMessage(update: Uint8Array) {
      const updateArray = Array.from(update);
      const totalChunks = Math.ceil(updateArray.length / 3000);
      for (let i = 0; i < totalChunks; i++) {
        const chunk = updateArray.slice(i * 3000, (i + 1) * 3000);
        const payload = {
          content: [chunk, i, totalChunks, messageId],
          type: "",
        };
        sendYjsUpdate(payload);
      }
      messageId++;
    }
  }
}

export { yDoc };
