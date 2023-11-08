import { createUser } from "@/api/service/user";

const useSignup = () => {
  const signup = async (email: string, password: string, nickName: string, profileImage: string) => {
    console.log("회원가입!!!!!!!!");
    try {
      const response = await createUser(email, password, nickName, profileImage);
      return "success";
    } catch (error) {
      console.log(error);
    }
  };
  return { signup };
};

export default useSignup;
