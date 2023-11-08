import { signinUser } from "@/api/service/user";
import { useAtom } from "jotai";

import { userAtom } from "@/store/authAtom";

const useSignin = () => {
  const [, setUser] = useAtom(userAtom);

  const signin = async (email: string, password: string) => {
    try {
      const response = await signinUser(email, password);
      // console.log(response);
      localStorage.setItem("accessToken", response.data.accessToken);
      // console.log(response.data.userInfo);

      setUser({
        userPk: response.data.userInfo.userPk,
        nickName: response.data.userInfo.nickName,
        email: response.data.userInfo.email,
        profileImage: response.data.userInfo.profileImage,
        documents: response.data.userInfo.documents,
      });

      return "success";
    } catch (error) {
      console.log(error);
      return "error";
    }
  };
  return { signin };
};

export default useSignin;
