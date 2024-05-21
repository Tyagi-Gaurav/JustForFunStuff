import Parser from 'html-react-parser';

export default function FloatableTextAreaWithLabel(props) {

  return (
    <>
      <div className="row p-0">
        <div className="col-sm-2">
          <label>{props.label}</label>
        </div>
        <div className="col-sm-9 ml-1">
          <h5
            className="border display-5 rounded bg-light"
            data-testid={props.testId}
          >
            {Parser(props.text)}
          </h5>
        </div>
      </div>
    </>
  );
}
