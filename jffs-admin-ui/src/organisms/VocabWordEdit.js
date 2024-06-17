import { useEffect, useState } from "react";
import { getWord } from "../api/backend_api";

export default function VocabWordEdit({ wordToEdit }) {
  const [word, setWord] = useState("");
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState("");
  const [example, setExample] = useState("");

  function formattedArray(words) {
    if (words) {
      return words.join("\n ");
    } else {
      return "";
    }
  }

  useEffect(() => {
    getWord(wordToEdit).then((wordResponse) => {
      setWord(wordResponse.data["word"]);
      var meaning = wordResponse.data["meanings"][0];
      setSynonym(formattedArray(meaning["synonyms"]));
      setMeaning(meaning["definition"]);
      setExample(formattedArray(meaning["examples"]));
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
        />
      </div>
      <div class="col-12">
        <button type="submit" class="col-md-1 btn btn-primary">
          Save
        </button>
      </div>
    </>
  );
}
