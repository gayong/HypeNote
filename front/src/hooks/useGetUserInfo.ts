import { getUserInfo } from "@/api/service/user";
import { useAtom } from "jotai";

import { userAtom } from "@/store/authAtom";
import { useEffect } from "react";

const useGetUserInfo = () => {
  const [, setUser] = useAtom(userAtom);

  useEffect(() => {
    const asyncFunc = async () => {
      const token = localStorage.getItem("accessToken");
      // if (token) {
      const response = await getUserInfo();

      setUser({
        userPk: response.data.userPk,
        nickName: response.data.nickName,
        email: response.data.email,
        profileImage: response.data.profileImage,
        documentsRoots: response.data.documentsRoots,
        sharedDocumentsRoots: response.data.sharedDocumentsRoots,
        role: response.data.documentsRoots,
      });

      // }
    };

    asyncFunc();
  }, []);
};
export default useGetUserInfo;
