import { useState } from "react";
import { updateWord } from "../api/backend_api";

export default function VocabWordAdd({ listCallback}) {
  const [word, setWord] = useState("");
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
    updateWord({
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
          placeholder="meaning 1; meaning 2"
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
          placeholder="synonym 1,synonym 2"
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
          placeholder="example1,example2"
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
