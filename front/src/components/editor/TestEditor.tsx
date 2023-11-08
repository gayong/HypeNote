"use client";

import { useEffect, useState } from "react";
import { BlockNoteView, useBlockNote } from "@blocknote/react";
import "@blocknote/core/style.css";
import styles from "./Editor.module.css";
import { uploadToTmpFilesDotOrg_DEV_ONLY } from "@blocknote/core";
import * as store from "./store";
type WindowWithProseMirror = Window & typeof globalThis & { ProseMirror: any };

type Props = {
  id: string;
};

function TestEditor({ id }: Props) {
  const [theme, setTheme] = useState<"light" | "dark">("light");

  useEffect(() => {
    if (typeof window !== "undefined") {
      const localTheme = window.localStorage.getItem("theme");
      setTheme(localTheme === "light" ? "light" : "dark");
    }
  }, [theme]);

  useEffect(() => {
    const handleStorageChange = (e: StorageEvent) => {
      if (e.key === "theme") {
        setTheme(e.newValue === "light" ? "light" : "dark");
      }
    };

    window.addEventListener("storage", handleStorageChange);

    // 컴포넌트가 언마운트될 때 이벤트 리스너를 제거
    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, []);

  const editor = useBlockNote({
    onEditorContentChange: (editor) => {
      console.log(editor.topLevelBlocks);
    },
    domAttributes: {
      editor: {
        class: styles.editor,
        "data-test": "editor",
        theme: theme,
      },
    },
    uploadFile: uploadToTmpFilesDotOrg_DEV_ONLY,
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
      fragment: store.store.fragment,
      // Information (name and color) for this user:
      user: {
        name: "store.getRandomName()",
        color: "#958DF1",
      },
    },
  });

  // Give tests a way to get prosemirror instance
  (window as WindowWithProseMirror).ProseMirror = editor?._tiptapEditor;

  return <BlockNoteView editor={editor} theme={theme} />;
}

export default TestEditor;
