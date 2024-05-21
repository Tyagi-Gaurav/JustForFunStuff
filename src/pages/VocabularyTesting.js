import { useState } from "react";
import "./VocabularyTesting.css";
import { getWords } from "../api/vocab";
import Parser from 'html-react-parser';

function getRandomInt(max) {
  return Math.floor(Math.random() * max);
}

function formattedArray(words) {
  return words.join('<br/>');
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
        setInProgress(true);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div>
      <div className="text-center heading">
        <h1>Test your Vocabulary</h1>
      </div>
      {inProgress && <div className="row mb-2 p-0">
        <div className="col-sm-10">
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
        <div className="row p-0">
          <div className="col-sm-2">
            <label>Meaning</label>
          </div>
          <div className="col-sm-9 ml-1">
            <h5
              className="border display-5 rounded bg-light"
              data-testid="meaning-text"
            >
              {Parser(meaning)}
            </h5>
          </div>
        </div>
      )}

      {inProgress && (
        <div className="row p-0">
          <div className="col-sm-2">
            <label>Synonyms</label>
          </div>
          <div className="col-sm-9 ml-1">
            <h5
              className="border display-5 rounded bg-light"
              data-testid="synonym-text"
            >
              {Parser(synonyms)}
            </h5>
          </div>
        </div>
      )}

      {inProgress && (
        <div className="row p-0">
          <div className="col-sm-2">
            <label>Example</label>
          </div>
          <div className="col-sm-9 ml-1">
            <h5
              className="border display-5 rounded bg-light"
              data-testid="example-text"
            >
              {example}
            </h5>
          </div>
        </div>
      )}

      <div className="row">
        <div className="col-sm-12 replay justify-content-center pb-10">
          <button className="btn btn-primary" onClick={handleClick}>
            Begin
          </button>
        </div>
      </div>
    </div>
  );
}
