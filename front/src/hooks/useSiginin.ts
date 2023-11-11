import { signinUser } from "@/api/service/user";
import useGetUserInfo from "./useGetUserInfo";
import { message } from "antd";

const useSignin = () => {
  const { userInfo } = useGetUserInfo();
  const signin = async (email: string, password: string) => {
    try {
      const response = await signinUser(email, password);
      console.log(response);
      localStorage.setItem("accessToken", response.data.accessToken);
      // const refreshToken = response.headers["refreshToken"];
      // console.log(refreshToken);
      userInfo();

      return "success";
    } catch (error: any) {
      console.log(error.response);
      // message.error(error);
      return error.response.data.message;
    }
  };
  return { signin };
};

export default useSignin;
