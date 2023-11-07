"use client";

import { syncedStore, getYjsDoc } from "@syncedstore/core";
import SockJS from "sockjs-client";
import { Stomp, Frame } from "@stomp/stompjs";
import * as Y from "yjs";

let updateValue: Array<Number> = [];
// Define the type for Todo
let firstConnected = 1;
type Todo = { completed: boolean; title: string };
type update = { content: Number[]; type: string };

// Create your SyncedStore store
export const store = syncedStore({ todos: [] as Todo[], fragment: "xml" });

// Create a Yjs document that syncs automatically using Y-Doc
const yDoc = getYjsDoc(store);

// Initialize SockJS and Stomp
const sock = new SockJS("https://hype-note.com/api/editor/ws"); // Replace with your SockJS server endpoint
export const stompClient = Stomp.over(sock);
// Connect to SockJS
stompClient.connect({}, (frame: Frame) => {
  // Subscribe to a topic to receive changes from other clients
  stompClient.subscribe("/sub/note/1", (message) => {
    // Handle incoming messages (e.g., Yjs updates) and merge them with the local Y-Doc

    const payload = JSON.parse(message.body);
    const enter = payload.type;
    if (enter === "1") {
      // Handle Yjs updates and send them to other clients
      if (updateValue.length) {
        const startPayload = { content: updateValue, type: "" };
        sendYjsUpdate(startPayload);
      }
      return;
    }
    // yDoc.transact(() => {
    //   // Merge Yjs updates received from other clients

    const arr = new Uint8Array(payload.content);
    //   console.log(contentArray);
    //   // Apply Yjs update using applyUpdate
    //   yDoc.applyUpdate(payload.content[0]);
    // });
    if (updateValue === Array.from(arr)) {
      console.log("testtestseteststd");
      return;
    }
    Y.applyUpdate(yDoc, arr);
    // const state1 = Y.encodeStateAsUpdate(yDoc);
    // console.log(state1);
  });
  if (firstConnected === 1) {
    firstConnected = 0;
    stompClient.send("/pub/note/1", {}, JSON.stringify({ type: "1" }));

    // updateValue = Array.from(yDoc);
    // Handle Yjs updates and send them to other clients
    // const firstPatload = { content: updateValue, type: "" };
    // console.log(payload);
    // sendYjsUpdate(payload);
    return;
  }
});

// Function to send Yjs updates to other clients
function sendYjsUpdate(update: update) {
  // Publish the update to a SockJS topic
  stompClient.send("/pub/note/1", {}, JSON.stringify(update));
}

// Define Yjs updates handler
yDoc.on("update", (update) => {
  const encodeValue = Y.encodeStateAsUpdate(yDoc);
  updateValue = Array.from(encodeValue);
  // Handle Yjs updates and send them to other clients
  const payload = { content: Array.from(encodeValue), type: "" };
  sendYjsUpdate(payload);
});

// Export the Y-Doc
export { yDoc };
