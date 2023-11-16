"use client";

import { useAtom } from "jotai";
import { themeAtom } from "../../store/theme";
import { isSearchOpen } from "../../store/searchOpen";
import { useEffect, useRef, useState } from "react";
import {
  BlockNoteView,
  Theme,
  blockNoteToMantineTheme,
  darkDefaultTheme,
  lightDefaultTheme,
  useBlockNote,
} from "@blocknote/react";
import "@blocknote/core/style.css";
import styles from "./Editor.module.css";
import { uploadToTmpFilesDotOrg_DEV_ONLY, Block, PartialBlock } from "@blocknote/core";
import * as storeS from "./store";
import Search from "@/components/editor/Search";
import Note from "@/hooks/useGetNote";
import UpdateNote from "@/hooks/useUpdateNote";
import { useRouter } from "next/navigation";
import { useEditorWebSocket } from "@/context/SocketEditorProvider";
import ShardeBtn from "./SharedBtn";
import DeleteBtn from "./DeleteBtn";
import ToShareBtn from "./ToShareBtn";
import { useNoteList } from "@/hooks/useNoteList";
import HorizontalRule from "@tiptap/extension-horizontal-rule";
import CodeBlock from "@tiptap/extension-code-block";
import useGetUserInfo from "@/hooks/useGetUserInfo";

import useImageUpload from "@/hooks/useImageUpload";
import syncedStore, { getYjsDoc } from "@syncedstore/core";
type WindowWithProseMirror = Window & typeof globalThis & { ProseMirror: any };

type Props = {
  id: string;
};

type Todo = { completed: boolean; title: string };

function TestEditor({ id }: Props) {
  const { ImageUpload } = useImageUpload();
  const router = useRouter();
  const stompClient = useEditorWebSocket();
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  // const [user] = useAtom(userAtom);
  const { noteList } = useNoteList();
  const [prevTitle, setPrevTitle] = useState("");
  const prevTitleRef = useRef("");
  const [owner, setOwner] = useState(0);

  // const [theme, setTheme] = useState<"light" | "dark">("light");
  const [nowtheme, setTheme] = useAtom<any>(themeAtom);
  const [open] = useAtom(isSearchOpen);
  const store = syncedStore({ todos: [] as Todo[], fragment: "xml" });
  const yDoc = getYjsDoc(store);

  // 폰트 설정
  const componentStyles = (theme: Theme) => ({
    Editor: {
      '[data-node-type="blockContainer"] *': {
        fontFamily: "PreRg",
        color: nowtheme === "light" ? "#424242" : "#ffffff",
      },
    },
  });

  const themeWithFont = {
    light: {
      ...lightDefaultTheme,
      componentStyles,
    },
    dark: {
      ...darkDefaultTheme,
      componentStyles,
    },
  } satisfies {
    light: Theme;
    dark: Theme;
  };

  const onSave = () => {
    console.log(editor.topLevelBlocks);
    const title = editor.topLevelBlocks[0].content;
    const content = editor.domElement.innerHTML;
    const update = async (title: string) => {
      if (!user) {
        return;
      }

      try {
        await UpdateNote(id, title, content);
        if (title !== prevTitleRef.current) {
          noteList.mutate({
            rootList: user.documentsRoots,
          });
          noteList.mutate({
            rootList: user.sharedDocumentsRoots,
          });
          prevTitleRef.current = title;
        }
      } catch (error) {
        console.log(error);
      }
    };
    if (title) {
      update(title);
    }
  };
  useEffect(() => {
    if (stompClient) {
      // if (yDoc) {
      storeS.connectStompClient(id, stompClient, yDoc);
      // }
    }

    // 컴포넌트가 언마운트될 때 실행되는 함수를 반환합니다.
    return () => {
      if (stompClient) {
        stompClient.unsubscribe(`/sub/note/${id}`);
      }
      if (yDoc) {
        yDoc.destroy();
      }
    };
  }, [id, stompClient]); // editor가 변경될 때마다 이 훅을 실행합니다.

  const editor = useBlockNote({
    _tiptapOptions: {
      extensions: [HorizontalRule, CodeBlock],
    },
    initialContent: [
      {
        id: "d91d6999-f308-4d23-adea-4e69e22b50ea",
        type: "paragraph",

        props: {
          //@ts-ignore
          textColor: "default",
          //@ts-ignore
          backgroundColor: "default",
          //@ts-ignore
          textAlignment: "left",
        },
        //@ts-ignore
        content: [],
        children: [],
      },
    ],
    onEditorReady(editor) {
      if (!user) {
        return;
      }

      const getBlocks = async (val: string) => {
        noteList.mutate({
          rootList: user.documentsRoots,
        });
        noteList.mutate({
          rootList: user.sharedDocumentsRoots,
        });

        const blocks = await editor.HTMLToBlocks(val);
        editor.replaceBlocks(editor.topLevelBlocks, blocks);
        if (blocks && blocks[0]) {
          const content = blocks[0].content;
          if (content) {
            // @ts-ignore
            prevTitleRef.current = content[0].text;
          }
        }
      };

      Note(id)
        .then((content) => {
          getBlocks(content.content);
          setOwner(content.owner);
        })
        .catch((error) => {
          // router.push("/404");
        });
    },
    onEditorContentChange: (editor) => {
      const blockToUpdate = editor.topLevelBlocks[0];
      const content = editor.domElement.innerHTML;
      if (blockToUpdate.type !== "heading") {
        editor.updateBlock(blockToUpdate, {
          type: "heading",
        });
      }
      // if (blockToUpdate.content !== prevTitleRef.current) {
      //   noteList.mutate({
      //     rootList: user.documentsRoots,
      //   });
      //   noteList.mutate({
      //     rootList: user.sharedDocumentsRoots,
      //   });
      // }
    },
    domAttributes: {
      editor: {
        class: styles.editor,
        "data-test": "editor",
      },
      blockContent: {},
    },
    // uploadFile: uploadToTmpFilesDotOrg_DEV_ONLY,
    uploadFile: ImageUpload,
    collaboration: {
      // The Yjs Provider responsible for transporting updates:
      provider: {
        connect: () => {
          // No need to connect here; it's handled in store.tsx
        },
        disconnect: () => {
          // Disconnect from SockJS and Stomp
          // Implement disconnection logic as needed
        },
      },
      // Where to store BlockNote data in the Y-Doc:
      fragment: store.fragment,
      // Information (name and color) for this user:
      user: {
        name: "store.getRandomName()",
        color: "#958DF1",
      },
    },
  });
  const Drag = () => {
    const title = editor.topLevelBlocks[0].content;
    const content = editor.domElement.innerHTML;
    const update = async (title: string) => {
      try {
        await UpdateNote(id, title, content);
        // noteList.mutate({
        //   rootList: user.documentsRoots,
        // });
        // noteList.mutate({
        //   rootList: user.sharedDocumentsRoots,
        // });
      } catch (error) {
        console.log(error);
      }
    };
    if (title) {
      update(title);
    }
  };

  //Test
  // Give tests a way to get prosemirror instance
  (window as WindowWithProseMirror).ProseMirror = editor?._tiptapEditor;

  return (
    <>
      <div style={{ width: open ? "calc(100% - 380px)" : "100%" }}>
        <BlockNoteView editor={editor} theme={themeWithFont} onKeyUp={onSave} onDragEnd={Drag} />
        {/* <BlockNoteView editor={editor} theme={theme}/> */}
      </div>
      <Search />

      {user?.userPk === owner && <DeleteBtn id={id} />}
      <ShardeBtn owner={owner} id={id} />
      {user?.userPk === owner && <ToShareBtn id={id} />}
    </>
  );
}

export default TestEditor;
