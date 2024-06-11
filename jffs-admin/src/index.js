import ReactDOM from "react-dom/client";
import { StrictMode } from "react";
import NavBar from "./organisms/NavBar";
import { createBrowserRouter, RouterProvider, Outlet } from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <NavbarWrapper />,
    children: [
      { path: "/vocab/list", element: <NavbarWrapper /> },
      { path: "/vocab/add", element: <NavbarWrapper /> },
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
        <div className="container pt-2">
          <Outlet />
        </div>
      </div>
    </StrictMode>
  );
}
