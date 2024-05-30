export default function Heading({headingStyle, headingMessage}) {
  return (
    <div className={headingStyle + " text-center"}>
      <h1>{headingMessage}</h1>
    </div>
  );
}
