import { fetchGPT } from "@/api/service/editor";

const useGPT = () => {
  const getGPT = async (question: string) => {
    console.log("보내는 질문", question);
    try {
      const response = await fetchGPT(question);
      console.log(response.data.choices);
      return response.data.choices;
    } catch (err) {
      console.log(err);
    }
    return "";
  };
  return { getGPT };
};

export default useGPT;
