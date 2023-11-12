export interface NoteType {
  id: string;
  title: string;
  content: string;
  url: string;
  thumbnail: string;
}

export interface NotesType {
  notes: Array<NoteType>;
}

export interface SearchType {
  url: string;
  // pagemap: PageMapType;
  title: string;
  datetime: string;
  contents: string;
  thumbnail: string;
}

export interface PageMapType {
  cse_thumbnail: Array<ThumbnailType>;
}
export interface ThumbnailType {
  src: string;
  width: number;
  height: number;
}

export interface DocumentsType {
  title: string;
  children?: DocumentsType[];
  id: string;
  owner: number;
  parentId: string;
}
