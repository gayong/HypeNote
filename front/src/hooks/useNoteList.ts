import { fetchNoteList } from "@/api/service/editor";
import { useMutation } from "react-query";
import { useState } from "react";
import { useAtom } from "jotai";
import { userAtom } from "@/store/authAtom";
import { DocumentsType } from "@/types/ediotr";

export const useNoteList = () => {
  const [myTreeNote, setMyTreeNote] = useState<DocumentsType[]>([]);
  const [sharedTreeNote, setsharedMyTreeNote] = useState<DocumentsType[]>([]);
  const [user] = useAtom(userAtom);

  const noteList = useMutation(
    async ({ rootList }: { rootList: Array<string> }) => {
      const response = await fetchNoteList(rootList);
      console.log(response);
      return response.data.data;
    },
    {
      onSuccess: (data, variables) => {
        // mutation 성공 시 수행할 로직
        console.log("트리데이터 받아라 얍", data, variables);
        if (variables.rootList === user.documentsRoots) {
          setMyTreeNote(data);
        } else if (variables.rootList === user.sharedDocumentsRoots) {
          setsharedMyTreeNote(data);
        }
      },
      onError: (error) => {
        console.log(error);
      },
    }
  );
  return {
    noteList,
    myTreeNote,
    sharedTreeNote,
  };
};

// const useNoteList = () => {
//   const noteList = async (rootList: string[]) => {
//     try {
//       const response = await fetchNoteList(rootList);
//       return response.data.data;
//     } catch (err) {
//       console.log(err);
//     }
//     return "";
//   };
//   return { noteList };
// };
// export default useNoteList;

// export const useAllDiagram = () => {
//   return useQuery(["diagrams"], fetchNoteListD);
// };
