export default function AlertMessage({ type, message }) {
  return (
    <div class={"alert alert-" + type} role="alert">
    {message}
    </div>
  );
}
