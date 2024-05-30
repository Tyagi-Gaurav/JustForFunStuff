import ReactDOM from "react-dom/client";
import { StrictMode } from "react";
import "./index.css";
import GamePage from "./pages/GamePage.js";
import NavBar from "./organisms/NavBar.js";

import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";
import TicTacToe from "./pages/TicTacToe.js";
import VocabularyTesting from "./pages/VocabularyTesting.js";

const router = createBrowserRouter([
  {
    path: "/",
    element: <NavbarWrapper />,
    children: [
      { path: "/", element: <GamePage /> },
      { path: "/games", element: <GamePage /> },
      { path: "/games/tictactoe", element: <TicTacToe /> },
      { path: "/games/vocabtesting", element: <VocabularyTesting /> },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <div>
    <RouterProvider router={router} />
  </div>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals();

function NavbarWrapper() {
  return (
    <StrictMode>
      <div>
        <NavBar />
        <div className="container pt-2">
          <Outlet />
        </div>
      </div>
    </StrictMode>
  );
}
