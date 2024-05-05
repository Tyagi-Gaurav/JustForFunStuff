import ReactDOM from "react-dom/client";
import { StrictMode } from "react";
import "./index.css";
import GamePage from "./pages/GamePage.js";
import NavBar from "./components/NavBar.js";

import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";
import TicTacToe from "./components/TicTacToe.js";

const router = createBrowserRouter([
  {
    path: "/",
    element: <NavbarWrapper />,
    children: [
      { path: "/", element: <GamePage /> },
      { path: "/games/tictactoe", element: <TicTacToe /> },
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
