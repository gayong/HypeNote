import { reissueToken } from "@/api/service/user";

const useReissue = () => {
  const reissue = async () => {
    try {
      const response = await reissueToken();
      console.log(response);
      //   localStorage.setItem("accessToken", response.data.accessToken);
      // const refreshToken = response.headers["refreshToken"];
      // console.log(refreshToken);
      //   userInfo();

      //   return "success";
    } catch (error: any) {
      console.log(error);
      // message.error(error);
    }
  };
  return { reissue };
};

export default useReissue;
