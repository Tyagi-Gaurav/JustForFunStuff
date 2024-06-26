import Alert from "@mui/material/Alert";

export default function AlertMessage({ type, message }) {
  return <Alert severity="error">{message}</Alert>;
}
