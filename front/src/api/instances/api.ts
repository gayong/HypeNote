import axios from "axios";
// axios 인스턴스 생성
const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_SERVER_URL,
});

// 요청 인터셉터 설정
api.interceptors.request.use(
  function (config) {
    // 요청이 전달되기 전에 작업 수행

    // 토큰이 있는 경우
    const refreshToken = sessionStorage.getItem("refreshToken");
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      // config.headers["Authorization"] = token;
      // config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
  },
  function (error) {
    // 요청 오류가 있는 작업 수행
    // 토큰이 없는 경우
    console.log(error);
    return Promise.reject(error);
  }
);

// // 응답 인터셉터 추가하기
// api.interceptors.response.use(
//   function (response) {
//     // 2xx 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
//     // 응답 데이터가 있는 작업 수행
//     return response;
//   },
//   function (error) {
//     // 2xx 외의 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
//     // 응답 오류가 있는 작업 수행

//     // 에러가 토큰관련일 경우 함수 작성
//     // 403 에러 토큰 만료
//     if (error.response.status === 403) {
//       // 리프레쉬토큰 가져오기
//       const refreshToken = localStorage.getItem("refreshtoken");
//       // 여기서부터는 백과 상의해서 해야됨
//     }

//     return Promise.reject(error);
//   }
// );

export default api;
