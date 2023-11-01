import { atom } from "jotai";
import { CompatClient } from "@stomp/stompjs";

export const socketAtom = atom<CompatClient | null>(null);
