import { atomWithStorage } from "jotai/utils";

export const userAtom = atomWithStorage("user", {
  userPk: 0,
  nickName: "",
  email: "",
  profileImage: "",
  documentsRoots: [],
  sharedDocumentsRoots: [],
  role: "",
});
