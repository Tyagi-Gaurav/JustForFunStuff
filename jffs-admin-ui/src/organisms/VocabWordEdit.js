import { useEffect, useState } from "react";
import { search, updateWord, deleteWord } from "../api/backend_api";

export default function VocabWordEdit({ wordToEdit, listCallback }) {
  const [word, setWord] = useState("");
  const [oldWord, setOldWord] = useState("");
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState([]);
  const [example, setExample] = useState([]);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");

  const handleWordChange = (event) => {
    setSuccess(false);
    setError(false);
    setWord(event.target.value);
  };

  const handleMeaningChange = (event) => {
    setSuccess(false);
    setError(false);
    setMeaning(event.target.value);
  };

  const handleSynonymsChange = (event) => {
    setSuccess(false);
    setError(false);
    setSynonym(event.target.value.split(","));
  };

  const handleExamplesChange = (event) => {
    setSuccess(false);
    setError(false);
    setExample(event.target.value);
  };

  const handleSave = () => {
    updateWord(oldWord, {
      word: word,
      meanings: [
        {
          definition: meaning,
          synonyms: synonyms,
          examples: example,
        },
      ],
    })
      .then((value) => {
        setSuccess(true);
      })
      .catch((error) => {
        setError(true);
        setErrorMsg(error);
      });
  };

  const handleDelete = () => {
    deleteWord(oldWord)
      .then((value) => {
        listCallback();
      })
      .catch((error) => {
        setError(true);
        setErrorMsg(error);
      });
  };

  useEffect(() => {
    setOldWord(wordToEdit);
    search("WORD", wordToEdit).then((wordResponse) => {
      setWord(wordResponse.data["word"]);
      var meaning = wordResponse.data["meanings"][0];
      setSynonym(meaning["synonyms"]);
      setMeaning(meaning["definition"]);
      setExample(meaning["examples"]);
    });
  }, []);

  return (
    <>
      {success && (
        <div class="alert alert-primary align-items-center" role="alert">
          Success
        </div>
      )}
      {error && (
        <div class="alert alert-danger align-items-center" role="alert">
          {errorMsg}
        </div>
      )}
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
      <div class="row">
        <div className="col-6">
          <button
            type="submit"
            className="btn btn-primary"
            onClick={handleSave}
          >
            Save
          </button>
        </div>
        <div className="col-2 offset-md-4 text-end">
          <button
            type="submit"
            className="btn btn-danger"
            onClick={handleDelete}
          >
            Delete
          </button>
        </div>
      </div>
    </>
  );
}
