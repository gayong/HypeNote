import { atomWithStorage } from "jotai/utils";

export const MyDocumentsAtom = atomWithStorage("documentsAtom", [
  {
    title: "",
    children: [],
    id: "",
    owner: 0,
    parentId: "",
  },
]);
export const SharedDocumentsAtom = atomWithStorage("SharedDocumentsAtom", [
  {
    title: "",
    children: [],
    id: "",
    owner: 0,
    parentId: "",
  },
]);
