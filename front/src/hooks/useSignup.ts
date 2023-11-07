import { createUser } from "@/api/service/member";

const useSignup = () => {
  const signup = async (email: string, password: string, nickName: string, profileImage: string) => {
    try {
      const response = await createUser(email, password, nickName, profileImage);
      console.log(response);
      return "success";
    } catch (error) {
      console.log(error);
    }
  };
  return { signup };
};

export default useSignup;
