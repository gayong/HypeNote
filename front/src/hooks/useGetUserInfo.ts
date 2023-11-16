import { getUserInfo } from "@/api/service/user";
import { useAtom } from "jotai";
// import { userAtom } from "@/store/authAtom";
import { useEffect, useState } from "react";
import { useQuery } from "react-query";
import { User } from "@/types/user";

const useGetUserInfo = () => {
  const [token, setToken] = useState<string | null>(null);

  // useEffect(() => {
  //   // 컴포넌트가 마운트된 후에 localStorage에 접근합니다.
  //   setToken(localStorage.getItem("accessToken"));
  // }, []);

  return useQuery<User>({
    queryKey: ["user"],
    queryFn: () => getUserInfo(),
    retry: false,
  });
};
// const [, setUser] = useAtom(userAtom);

// useEffect(() => {
//   const asyncFunc = async () => {
//     const token = localStorage.getItem("accessToken");
//     // if (token) {
//     const response = await getUserInfo();

//     setUser({
//       userPk: response.data.userPk,
//       nickName: response.data.nickName,
//       email: response.data.email,
//       profileImage: response.data.profileImage,
//       documentsRoots: response.data.documentsRoots,
//       sharedDocumentsRoots: response.data.sharedDocumentsRoots,
//       role: response.data.documentsRoots,
//     });

//     // }
//   };

//   asyncFunc();
// }, []);
export default useGetUserInfo;
