export interface NoteType {
  title: string;
  content: string;
}

export interface NotesType {
  notes: Array<NoteType>;
}

export interface SearchType {
  link: string;
  pagemap: PageMapType;
  snippet: string;
  title: string;
}

export interface PageMapType {
  cse_thumbnail: Array<ThumbnailType>;
}
export interface ThumbnailType {
  src: string;
  width: number;
  height: number;
}
