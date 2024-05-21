import { useState } from "react";
import styles from "./VocabularyTesting.module.css";
import { getWords } from "../api/vocab";
import FloatableTextAreaWithLabel from "../components/FloatableTextAreaWithLabel";

function getRandomInt(max) {
  return Math.floor(Math.random() * max);
}

function formattedArray(words) {
  if (words) {
    return words.join('<br/>');
  } else {
    return ""
  }
}

export default function VocabularyTesting() {
  const [allWords, setAllWords] = useState("");
  const [word, setWord] = useState("");
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState("");
  const [example, setExample] = useState("");
  const [inProgress, setInProgress] = useState(false);
  
  const handleClick = (event) => {
    event.preventDefault();
    getWords()
      .then((response) => {
        var data = response.data["words"];
        setAllWords(data);
        var index = getRandomInt(data.length);
        var selectedWord = data[index];
        setWord(selectedWord["word"]);
        setSynonym(formattedArray(selectedWord["synonyms"]));
        setMeaning(formattedArray(selectedWord["meaning"]));
        setExample(formattedArray(selectedWord["examples"]));
        setInProgress(true);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div>
      <div className={styles.heading + " text-center"}>
        <h1>Test your Vocabulary</h1>
      </div>
      {inProgress && <div className="row mb-2 pr-0">
        <div className="col-sm-12">
          <h3 className="display-1 rounded bg-light word" data-testid="word-text">{word}</h3>
        </div>
      </div>}

      {inProgress && (
        <div className="row mb-2">
          <label>Can you think of an answer before the timer runs out?</label>
          <p></p>
        </div>
      )}

      {inProgress && (
        <FloatableTextAreaWithLabel label="Meaning" testId="meaning-text" text={meaning} />)
      }

      {inProgress && synonyms && (
        <FloatableTextAreaWithLabel label="Synonyms" testId="synonym-text" text={synonyms} />)
      }

      {inProgress && example && (
        <FloatableTextAreaWithLabel label="Examples" testId="example-text" text={example} />)
      }

      <div className="row">
        <div className={styles.centered_button +  " col-sm-12 justify-content-center pb-10"}>
          <button className="btn btn-primary" onClick={handleClick}>
            {inProgress ? "Next" : "Begin"}
          </button>
        </div>
      </div>
    </div>
  );
}
