'use client';

import { Editor as NovelEditor } from 'novel';
import { useState, useEffect } from 'react';
import { Editor as Editor$1 } from '@tiptap/core';

type Props = {
  id: string; // id를 문자열로 지정
};

export default function Editor({ id }: Props) {
  // editorPage Id
  const EditorpageId = id;
  const [value, setValue] = useState<Editor$1>();
  useEffect(() => {
    console.log(value);
  }, [value]);

  return (
    <>
      {/* <NovelEditor defaultValue={{}} onUpdate={(editor?: Editor$1 | undefined) => setValue(editor)} /> */}
      <NovelEditor defaultValue={{}} onUpdate={(editor?: Editor$1 | undefined) => console.log(editor)} />
    </>
  );
}
