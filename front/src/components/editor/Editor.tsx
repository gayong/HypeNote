"use client";

import { Editor as TypeOfEditor } from "@tiptap/core";
import { Editor as NovelEditor } from "novel";
import styles from "./Editor.module.css";

import Document from "@tiptap/extension-document";
import Placeholder from "@tiptap/extension-placeholder";
import Collaboration from "@tiptap/extension-collaboration";
import { store } from "./store";

type Props = {
  id: string; // id를 문자열로 지정
};

export default function Editor({ id }: Props) {
  const colors = ["#958DF1", "#F98181", "#FBBC88", "#FAF594", "#70CFF8", "#94FADB", "#B9F18D"];
  const names = ["Lea Thompson", "Cyndi Lauper", "Tom Cruise", "Madonna"];

  // const getRandomElement = (list) => list[Math.floor(Math.random() * list.length)];
  // const getRandomColor = () => getRandomElement(colors);
  // const getRandomName = () => getRandomElement(names);
  // // 웹 소켓 연결
  // const [stompClient, setStompClient] = useState<CompatClient | null>(null);
  // const testNote = useRef({});
  // testNote.current = {
  //   content: [
  //     {
  //       type: "heading",
  //       attrs: {
  //         level: 1,
  //       },
  //       content: [
  //         {
  //           type: "text",
  //           text: "1231231231231231321132132133333333333333333",
  //         },
  //       ],
  //     },
  //   ],
  //   type: "doc",
  // };

  // useEffect(() => {
  //   const socket = new SockJS("https://www.hype-note.com/api/editor/ws");
  //   const client = Stomp.over(socket);
  //   function connect() {
  //     setStompClient(client);
  //     client.connect({}, function connection() {
  //       const subscription = client.subscribe(
  //         "/sub/note/1",
  //         function handleNote(frame: Frame) {
  //           console.log(frame.body);
  //           // if (frame.body) {
  //           //   const Note = JSON.parse(frame.body);
  //           //   testNote.current = Note;
  //           //   editorProps?.commands.setContent(Note);
  //           //   console.log(editorProps?.getJSON());
  //           // }
  //         },
  //         {}
  //       );
  //     });
  //   }
  //   connect();
  // }, []);

  // // 첫 블럭은 h1
  const CustomDocument = Document.extend({
    content: "heading block*",
  });
  const editorHandler = (editor: TypeOfEditor) => {
    // console.log(
    //   Collaboration.configure({
    //     fragment: store.fragment,
    //   })
    // );
    //   if (stompClient) {
    //     const html = editor.getHTML;
    console.log(editor.getJSON());
    //     // console.log(editorProps?.getJSON());
    //     stompClient.send("/pub/note/1", {}, JSON.stringify(editor.getJSON()));
    //     // const Json = generateJSON(html, [StarterKit]);
    //     // const html = editor.getHTML();
    //     // stompClient.send("/pub/note/1", {}, html);

    //     // editor.commands.setContent(Json);
    //   }
  };

  return (
    <>
      <NovelEditor
        defaultValue={{}}
        // onUpdate={(editor) => {
        //   editorHandler(editor);
        // }}
        className={styles["editor-container"]}
        disableLocalStorage={true}
        extensions={[
          // CustomDocument,
          Placeholder.configure({
            placeholder: "Write something …",
          }),
          Collaboration.configure({
            fragment: store.fragment,
          }),
          // CollaborationCursor.configure({
          //   provider: webrtcProvider(id),
          //   user: { name: getRandomName(), color: getRandomColor() },
          // }),
        ]}
      />
    </>
  );
}
