import { syncedStore, getYjsDoc } from "@syncedstore/core";
//@ts-ignore
import { WebrtcProvider } from "y-webrtc";

// (optional, define types for TypeScript)
type Todo = { completed: boolean; title: string };

// Create your SyncedStore store
export const store = syncedStore({ todos: [] as Todo[], fragment: "xml" });

// Create a document that syncs automatically using Y-WebRTC
const doc = getYjsDoc(store);
// export const webrtcProvider = new WebrtcProvider("syncedstore-todos", doc);
export function webrtcProvider(id: String) {
  return new WebrtcProvider(id, doc);
}
//@ts-ignore
export const disconnect = () => webrtcProvider.disconnect();
//@ts-ignore
export const connect = () => webrtcProvider.connect();

const colors = ["#958DF1", "#F98181", "#FBBC88", "#FAF594", "#70CFF8", "#94FADB", "#B9F18D"];
const names = ["Lea Thompson", "Cyndi Lauper", "Tom Cruise", "Madonna"];

export const getRandomElement = (list: Array<string>) => list[Math.floor(Math.random() * list.length)];
export const getRandomColor = () => {
  return getRandomElement(colors);
};
export const getRandomName = () => {
  return getRandomElement(names);
};
