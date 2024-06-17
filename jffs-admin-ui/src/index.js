import ReactDOM from "react-dom/client";
import { StrictMode } from "react";
import NavBar from "./organisms/NavBar";
import Vocab from "./organisms/Vocab";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <NavbarWrapper />,
    children: [
      { path: "/vocab", element: <Vocab /> }
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
