import logo from "./logo.svg";
import "./App.css";
import Button from "@mui/material/Button";
import { useState } from "react";
import HighLow from "./HighLow";

function App() {
  const [showHighLow, setShowHighLow] = useState(true);

  return (
    <div className="App">
        {showHighLow ? <HighLow/> : <h1>Content Coming Soon. Please check back later!!</h1>}
      </div>
  );
}

export default App;
