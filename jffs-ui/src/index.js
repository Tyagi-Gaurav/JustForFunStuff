import ReactDOM from "react-dom/client";
import { StrictMode } from "react";
import "./index.css";
import GamePage from "./pages/GamePage.jsx";
import NavBar from "./organisms/NavBar.jsx";

import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";
import TicTacToe from "./pages/TicTacToe.jsx";
import VocabularyTesting from "./pages/VocabularyTesting.jsx";
import { useTracking } from "react-tracking";
import { postTrackingEvent } from "./api/vocab";

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
  const { Track } = useTracking(
    {page: "Game"},
    {
      dispatch: (data) => {
        console.log(JSON.stringify(data));
        postTrackingEvent(data);
      }
    }
  );

  return (
    <StrictMode>
      <div>
        <NavBar />
        <div className="container pt-2">
          <Track>
            <Outlet />
          </Track>
        </div>
      </div>
    </StrictMode>
  );
}
