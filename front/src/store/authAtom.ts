import { atom } from "jotai";
import { User } from "@/types/user";
export const userAtom = {
  userPk: 0,
};

export const profileAtom = atom<User>({
  userPk: 0,
  nickName: "",
  email: "",
  documents: [],
});
