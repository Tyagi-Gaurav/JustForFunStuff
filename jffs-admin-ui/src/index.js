import ReactDOM from "react-dom/client";
import { StrictMode } from "react";
import NavBar from "./organisms/NavBar";
import VocabList from "./organisms/VocabList";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <NavbarWrapper />,
    children: [
      { path: "/vocab/list", element: <VocabList /> }
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <div>
    <RouterProvider router={router} />
  </div>
);

function NavbarWrapper() {
  return (
    <StrictMode>
      <div>
        <NavBar />
        <div className="container-lg">
          <Outlet />
        </div>
      </div>
    </StrictMode>
  );
}
