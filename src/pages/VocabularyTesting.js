import "./VocabularyTesting.css";

export default function VocabularyTesting() {
  return (
    <div>
      <div className="text-center heading">
        <h1>Test your Vocabulary</h1>
      </div>
      <div className="row mb-2">
        <div className="col-sm-12">
          <h3 className="border display-1 rounded bg-light">Word</h3>
        </div>
      </div>

      <div className="row mb-2">
        <label>Can you think of the answer before the timer runs out?</label>
        <p></p>
      </div>

      <div className="row mb-2">
        <div className="col-sm-12">
          <h4 className="border display-3 rounded bg-light">Meaning</h4>
        </div>
      </div>

      <div className="row mb-2">
        <div className="col-sm-12">
          <h4 className="border display-3 rounded bg-light">Synonyms</h4>
        </div>
      </div>

      <div className="row mb-2">
        <div className="col-sm-12">
          <h4 className="border display-3 rounded bg-light">Antonyms</h4>
        </div>
      </div>

      <div className="row mb-2 mt-10">
        <div className="col-sm-12 replay justify-content-center pb-10">
          <button className="btn btn-primary">Next</button>
        </div>
      </div>
    </div>
  );
}
