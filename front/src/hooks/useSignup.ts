import { createUser } from "@/api/service/user";
import { message } from "antd";

const useSignup = () => {
  const signup = async (email: string, password: string, nickName: string, profileImage: any) => {
    console.log("회원가입!!!!!!!!");
    try {
      const response = await createUser(email, password, nickName, profileImage);
      return "success";
    } catch (error: any) {
      // message.error(error.response);
      console.log(error);
      return error.response.data;
    }
  };
  return { signup };
};

export default useSignup;
