import { getUsersInfo } from "@/api/service/user";

export const useUsersFindByPkList = () => {
  const getUsersFindByPkList = async (userList: number[]) => {
    try {
      const response = await getUsersInfo(userList);
      console.log(response.data);
      return response;
    } catch (error) {
      console.log(error);
    }
  };
  return {
    getUsersFindByPkList,
  };
};
export default useUsersFindByPkList;
