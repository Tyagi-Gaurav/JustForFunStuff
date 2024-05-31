export default function AlertMessage({ type, message }) {
  return (
    <div className={"alert alert-" + type} role="alert">
    {message}
    </div>
  );
}
