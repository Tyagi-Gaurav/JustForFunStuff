import { useEffect, useState } from "react";
import { getWord, saveWord } from "../api/backend_api";

export default function VocabWordEdit({ wordToEdit , listCallback}) {
  const [word, setWord] = useState("");
  const [oldWord, setOldWord] = useState("");
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState([]);
  const [example, setExample] = useState([]);

  const handleWordChange = (event) => {
    setWord(event.target.value);
  }

  const handleMeaningChange = (event) => {
    setMeaning(event.target.value);
  }

  const handleSynonymsChange = (event) => {
    setSynonym(event.target.value.split(","));
  }

  const handleExamplesChange = (event) => {
    setExample(event.target.value);
  }

  const handleSave = () => {
    saveWord(oldWord, {
      word: word,
      meanings: [{
        definition: meaning,
        synonyms: synonyms,
        examples: example
      }]
    }).then((value) => {
      listCallback("Call list");
    }).catch((error) => {
      //TODO Show error message
      console.log("Error occurred " + error);
    })
  }

  useEffect(() => {
    setOldWord(wordToEdit);
    getWord(wordToEdit).then((wordResponse) => {
      setWord(wordResponse.data["word"]);
      var meaning = wordResponse.data["meanings"][0];
      setSynonym(meaning["synonyms"]);
      setMeaning(meaning["definition"]);
      setExample(meaning["examples"]);
    });
  }, []);

  return (
    <>
      <div className="mb-3">
        <label htmlFor="formControlWord" className="form-label">
          Word
        </label>
        <input
          type="Word"
          className="form-control"
          id="formControlWord"
          placeholder="Word"
          defaultValue={word}
          onChange={handleWordChange}
        />
      </div>
      <div className="mb-3">
        <label htmlFor="formControlMeaning" className="form-label">
          Meaning
        </label>
        <textarea
          className="form-control"
          id="formControlMeaning"
          rows="3"
          defaultValue={meaning}
          onChange={handleMeaningChange}
        />
      </div>
      <div className="mb-3">
        <label htmlFor="formControlSynomyms" className="form-label">
          Synonyms
        </label>
        <textarea
          className="form-control"
          id="formControlSynonyms"
          rows="3"
          defaultValue={synonyms}
          onChange={handleSynonymsChange}
        />
      </div>
      <div className="mb-3">
        <label htmlFor="formControlExamples" className="form-label">
          Examples
        </label>
        <textarea
          className="form-control"
          id="formControlExamples"
          rows="3"
          defaultValue={example}
          onChange={handleExamplesChange}
        />
      </div>
      <div className="col-12">
        <button type="submit" className="col-md-1 btn btn-primary" onClick={handleSave}>
          Save
        </button>
      </div>
    </>
  );
}
