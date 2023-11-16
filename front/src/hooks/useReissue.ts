import { reissueToken } from "@/api/service/user";

const useReissue = () => {
  const reissue = async () => {
    try {
      const response = await reissueToken();
    } catch (error: any) {
      console.log(error);
      // message.error(error);
    }
  };
  return { reissue };
};

export default useReissue;
