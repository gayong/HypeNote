import { getUserInfo } from "@/api/service/user";
import { useAtom } from "jotai";

import { userAtom } from "@/store/authAtom";

const useGetUserInfo = () => {
  const [, setUser] = useAtom(userAtom);

  const userInfo = async () => {
    try {
      const response = await getUserInfo();
      // data 저장하기
      console.log(response.data);
      setUser({
        userPk: response.data.userPk,
        nickName: response.data.nickName,
        email: response.data.email,
        profileImage: response.data.profileImage,
        documentsRoots: response.data.documentsRoots,
        sharedDocumentsRoots: response.data.sharedDocumentsRoots,
        role: response.data.documentsRoots,
      });

      //   return "success";
    } catch (error) {
      console.log(error);
      return "error";
    }
  };
  return { userInfo };
};

export default useGetUserInfo;
