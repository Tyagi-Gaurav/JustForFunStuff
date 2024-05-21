import "./VocabularyTesting.css";

export default function VocabularyTesting() {
  return (
    <div>
      <div className="text-center heading">
        <h1>Test your Vocabulary</h1>
      </div>
      <div className="row mb-2">
        <div className="col-sm-10">
          <h3 className="border display-1 rounded bg-light">Enact</h3>
        </div>
      </div>

      <div className="row mb-2">
        <label>Can you think of an answer before the timer runs out?</label>
        <p></p>
      </div>

      <div className="row p-0">
        <div className="col-sm-2">
          <label>Meaning</label>
        </div>
        <div className="col-sm-9 ml-1">
          <h5
            className="border display-5 rounded bg-light"
            data-testid="meaning-text"
          >
            Make Law
            <br />
            put into practice (an idea or suggestion)
            <br />
            act out (a role or play) on stage
            <br />
          </h5>
        </div>
      </div>

      <div className="row p-0">
        <div className="col-sm-2">
          <label>Synonyms</label>
        </div>
        <div className="col-sm-9 ml-1">
          <h5
            className="border display-5 rounded bg-light"
            data-testid="synonym-text"
          >
            Pass
            <br />
            Approve
            <br />
            Perform
            <br />
            Play
            <br />
          </h5>
        </div>
      </div>

      <div className="row p-0">
        <div className="col-sm-2">
          <label>Example</label>
        </div>
        <div className="col-sm-9 ml-1">
          <h5
            className="border display-5 rounded bg-light"
            data-testid="example-text"
          >
            Example Usage
          </h5>
        </div>
      </div>

      <div className="row">
        <div className="col-sm-12 replay justify-content-center pb-10">
          <button className="btn btn-primary">Begin</button>
        </div>
      </div>
    </div>
  );
}
