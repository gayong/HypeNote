import { signinUser } from "@/api/service/user";

const useSignin = () => {
  const signin = async (email: string, password: string) => {
    try {
      const response = await signinUser(email, password);
      console.log(response.data);
      // retrun
    } catch (error) {
      console.log(error);
    }
  };
  return { signin };
};

export default useSignin;
