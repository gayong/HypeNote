"use client";

import React, { useState, useEffect } from "react";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";
import { MyDocumentsAtom } from "@/store/documentsAtom";
import { useAtom } from "jotai";
import { DocumentsType } from "@/types/ediotr";

interface TreeType {
  title: string;
  value: string;
  key: string;
  children?: TreeType[];
}

interface ClickType {
  disabled: undefined;
  halfChecked: undefined;
  label: string;
  value: string;
}

interface TreeProps {
  onDocumentsChange: (value: Array<ClickType>) => void;
}

const Tree: React.FC<TreeProps> = ({ onDocumentsChange }) => {
  const [treeProps, setTreeProps] = useState<object>();
  const [treeData, setTreeData] = useState<TreeType[]>([]);
  const [documentsValue, setDocumentsValue] = useState<Array<ClickType>>([]);
  const { SHOW_PARENT, SHOW_ALL } = TreeSelect;
  const [myDocuments] = useAtom(MyDocumentsAtom);

  useEffect(() => {
    // 재귀 함수를 이용한 DocumentsType에서 TreeType으로의 변환
    const convertToTreeType = (documents: DocumentsType[]): TreeType[] => {
      return documents.map((item) => ({
        title: item.title,
        value: item.id,
        key: item.id,
        children: item.children ? convertToTreeType(item.children) : [], // children이 있으면 재귀 호출
      }));
    };

    // 변환 과정
    if (myDocuments && myDocuments.length > 0) {
      const newTreeData: TreeType[] = convertToTreeType(myDocuments);
      setTreeData(newTreeData.filter((item) => myDocuments.find((i) => i.id === item.key)?.parentId === "root"));
    }
  }, [myDocuments]);

  useEffect(() => {
    console.log(treeData);

    const tProps = {
      treeData,
      // value: value,
      onChange: documentsOnChange,
      treeCheckable: true,
      treeCheckStrictly: true,
      showCheckedStrategy: SHOW_ALL,
      placeholder: "페이지를 선택해주세요",
      style: {
        width: "300px",
        marginTop: "10px",
      },
    };
    setTreeProps(tProps);
  }, [treeData]);

  // const documentsOnChange = (newDocument: ClickType[]) => {
  //   setDocumentsValue(newDocument);
  // };

  const documentsOnChange = (newDocument: ClickType[]) => {
    setDocumentsValue(newDocument);
    onDocumentsChange(newDocument); // 부모 컴포넌트의 함수를 호출하여 데이터를 전달
  };

  useEffect(() => {
    console.log("qqqq", documentsValue);
  }, [documentsValue]);

  return <>{treeData.length > 0 && <TreeSelect {...treeProps} />}</>;
};
export default Tree;
