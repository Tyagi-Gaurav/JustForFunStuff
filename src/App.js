import React, { useState } from "react";
import "./App.css";
import Board from "./TicTacToe";

function HelloWorld({ activeComponent }) {
  if (activeComponent === 0) {
    return <h1 className="HighLowApp">Hello World</h1>;
  }
  return null;
}

function TicTacToe({ activeComponent }) {
  if (activeComponent === 1) {
    return <Board />;
  }
  return null;
}

export default function App() {
  const [activeComponent, setActiveComponent] = useState(1); //TODO Can we use Enums??

  return (
    <div>
      <HelloWorld activeComponent={activeComponent} />
      <TicTacToe activeComponent={activeComponent} />
    </div>
  );
}
