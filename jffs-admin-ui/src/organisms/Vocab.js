import { useState } from "react";
import VocabList from "./VocabList";
import VocabWordEdit from "./VocabWordEdit";

export default function Vocab() {
    const [showList, setShowList] = useState(true);
    const [showEdit, setShowEdit] = useState(false);
    const [editWord, setEditWord] = useState("");

  const editCallback = (word) => {
    setEditWord(word);
    setShowList(false); 
    setShowEdit(true); 
  }

  return (
    <>
      {showList && <VocabList editCallback={editCallback}/>}
      {showEdit && <VocabWordEdit wordToEdit={editWord}/>}
    </>
  );
}
