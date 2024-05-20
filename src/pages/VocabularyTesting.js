import "./VocabularyTesting.css";

export default function VocabularyTesting() {
  return (
    <div>
      <div className="text-center heading">
        <h1>Test your Vocabulary</h1>
      </div>
      <div className="row mb-2">
        <div className="col-sm-10">
          <h3 className="border display-1 rounded">Word</h3>
        </div>
      </div>

      <div className="container">
        <div className="row py-4">
          <div className="col-6 col-lg-5 mr-3 bg-danger text-white">
            <span className="p-3 mb-2">Meaning 1</span>
          </div>

          <div className="col-6 col-lg-5 ml-3 bg-warning text-white">
            <span className="p-3 mb-2">Meaning 2</span>
          </div>
        </div>
        <div className="row py-4">
          <div className="col-6 col-lg-5 mr-3 bg-primary text-white">Meaning 3</div>
          <div className="col-6 col-lg-5 ml-3 bg-success text-white">Meaning 4</div>
        </div>
      </div>
    </div>
  );
}
