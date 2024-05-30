export default function TextInABox({word, testId}) {
  return (
    <div className="row mb-2 pr-0">
      <div className="col-sm-12">
        <h3 className="display-1 rounded bg-light word" data-testid={testId}>
          {word}
        </h3>
      </div>
    </div>
  );
}
