"use client";

import { syncedStore, getYjsDoc } from "@syncedstore/core";
import SockJS from "sockjs-client";
import { Stomp, Frame } from "@stomp/stompjs";
import * as Y from "yjs";

let updateValue: Array<Number> = [];
// Define the type for Todo
let firstConnected = 1;
type Todo = { completed: boolean; title: string };
type update = { content: (number | number[])[]; type: string };

// Create your SyncedStore store
export const store = syncedStore({ todos: [] as Todo[], fragment: "xml" });

// Create a Yjs document that syncs automatically using Y-Doc
const yDoc = getYjsDoc(store);

// Initialize SockJS and Stomp
const sock = new SockJS("https://hype-note.com/api/editor/ws"); // Replace with your SockJS server endpoint
export const stompClient = Stomp.over(sock);
// Connect to SockJS
const pendingUpdates: { [key: number]: any[] } = {};

stompClient.connect({}, (frame: Frame) => {
  // Subscribe to a topic to receive changes from other clients
  stompClient.subscribe("/sub/note/3", (message) => {
    // Handle incoming messages (e.g., Yjs updates) and merge them with the local Y-Doc

    const payload = JSON.parse(message.body);
    const enter = payload.type;
    if (enter === "1") {
      // Handle Yjs updates and send them to other clients
      const InitialVal = Y.encodeStateAsUpdate(yDoc);
      if (InitialVal.length) {
        // const startPayload = { content: Array.from(InitialVal), type: "" };
        chunckMessage(InitialVal);
      }
      return;
    }
    // yDoc.transact(() => {
    //   // Merge Yjs updates received from other clients

    const arr = new Uint8Array(payload.content[0]);
    // const { content, chunkId, totalChunks, messageId } = payload.content;
    const content = payload.content[0];
    const chunkId = payload.content[1];
    const totalChunks = payload.content[2];
    const messageId = payload.content[3];
    console.log(content, chunkId, totalChunks, messageId);
    //   console.log(contentArray);
    //   // Apply Yjs update using applyUpdate
    //   yDoc.applyUpdate(payload.content[0]);
    // });
    if (!pendingUpdates[messageId]) {
      pendingUpdates[messageId] = new Array(totalChunks).fill(null);
    }
    pendingUpdates[messageId][chunkId] = new Array(content);
    if (!pendingUpdates[messageId].includes(null)) {
      // Concatenate all arrays in pendingUpdates[messageId] into a single array
      const concatenated = pendingUpdates[messageId].reduce((acc, val) => acc.concat(val), []);
      console.log(concatenated);
      // Convert the concatenated array into a Uint8Array
      const update = new Array();

      for (let i = 0; i < totalChunks; i++) {
        update.push(...concatenated[i]);
      }

      console.log(update);

      if (updateValue === update) {
        console.log("testtestseteststd");
        return;
      }
      const newUpdate = new Uint8Array(update);

      Y.applyUpdate(yDoc, newUpdate);
      // Remove the pending update
      delete pendingUpdates[messageId];
    }

    // const state1 = Y.encodeStateAsUpdate(yDoc);
    // console.log(state1);
  });
  if (firstConnected === 1) {
    firstConnected = 0;
    stompClient.send("/pub/note/3", {}, JSON.stringify({ type: "1" }));

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
  console.log(JSON.stringify(update));
  stompClient.send("/pub/note/3", {}, JSON.stringify(update));
}

// Define Yjs updates handler
// Define Yjs updates handler
let messageId = 0;
yDoc.on("update", (update) => {
  console.log(Y.encodeStateAsUpdate(yDoc), update, "전체값과 변환값");
  chunckMessage(update);
});

function chunckMessage(update: Uint8Array) {
  const updateArray = Array.from(update);
  const totalChunks = Math.ceil(updateArray.length / 3000);
  for (let i = 0; i < totalChunks; i++) {
    // Slice the chunk from the update array
    const chunk = updateArray.slice(i * 3000, (i + 1) * 3000);
    const payload = {
      content: [chunk, i, totalChunks, messageId],
      type: "",
    };
    sendYjsUpdate(payload);
  }
  messageId++;
}
// Export the Y-Doc
export { yDoc };
