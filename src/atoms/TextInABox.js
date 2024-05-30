export default function TextInABox({text, testId}) {
  return (
        <h3 className="display-1 rounded bg-light word" data-testid={testId}>
          {text}
        </h3>
    
  );
}
