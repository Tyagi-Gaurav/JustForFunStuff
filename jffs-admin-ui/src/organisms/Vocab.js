import { useState } from "react";
import VocabList from "./VocabList";
import VocabWordEdit from "./VocabWordEdit";

export default function Vocab() {
    const [showList, setShowList] = useState(true);
    const [showEdit, setShowEdit] = useState(false);
    const [editWord, setEditWord] = useState("");

  const callbackToShowEdit = (word) => {
    setEditWord(word);
    setShowList(false); 
    setShowEdit(true); 
  }

  const callbackToShowList = () => {
    setEditWord("");
    setShowList(true); 
    setShowEdit(false);
  }

  return (
    <>
      {showList && <VocabList editCallback={callbackToShowEdit}/>}
      {showEdit && <VocabWordEdit wordToEdit={editWord} listCallback={callbackToShowList}/>}
    </>
  );
}
