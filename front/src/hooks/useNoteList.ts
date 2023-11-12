import { fetchNoteList } from "@/api/service/editor";
import { useMutation } from "react-query";
import { useState } from "react";
import { useAtom } from "jotai";
import { userAtom } from "@/store/authAtom";
import { SharedDocumentsAtom, MyDocumentsAtom } from "@/store/documentsAtom";
import { DocumentsType } from "@/types/ediotr";

export const useNoteList = () => {
  const [myTreeNote, setMyTreeNote] = useState<DocumentsType[]>([]);
  const [sharedTreeNote, setsharedMyTreeNote] = useState<DocumentsType[]>([]);
  const [user] = useAtom(userAtom);
  const [, setMyDocuments] = useAtom(MyDocumentsAtom);
  const [, setSharedDocuments] = useAtom(SharedDocumentsAtom);

  const noteList = useMutation(
    async ({ rootList }: { rootList: Array<string> }) => {
      const response = await fetchNoteList(rootList);
      return response.data.data;
    },
    {
      onSuccess: (data, variables) => {
        // mutation 성공 시 수행할 로직
        // console.log("트리데이터 받아라 얍", data, variables);
        if (variables.rootList === user.documentsRoots) {
          setMyDocuments(data);
        } else if (variables.rootList === user.sharedDocumentsRoots) {
          setSharedDocuments(data);
        }
      },
      onError: (error) => {
        console.log(error);
      },
    }
  );
  return {
    noteList,
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
