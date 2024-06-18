import { useState } from "react";
import VocabList from "./VocabList";
import VocabWordEdit from "./VocabWordEdit";
import VocabWordAdd from "./VocabWordAdd";

export default function Vocab() {
    const [showList, setShowList] = useState(true);
    const [showEdit, setShowEdit] = useState(false);
    const [showAdd, setShowAdd] = useState(false);
    const [editWord, setEditWord] = useState("");

  const callbackToShowEdit = (word) => {
    setEditWord(word);
    setShowList(false); 
    setShowEdit(true); 
    setShowAdd(false); 
  }

  const callbackToShowList = () => {
    setEditWord("");
    setShowList(true); 
    setShowEdit(false);
    setShowAdd(false); 
  }

  return (
    <>
      {showList && <VocabList editCallback={callbackToShowEdit}/>}
      {showEdit && <VocabWordEdit wordToEdit={editWord} listCallback={callbackToShowList}/>}
      {showAdd && <VocabWordAdd listCallback={callbackToShowList}/>}

    </>
  );
}
