import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import { useNavigate } from "react-router-dom";

export default function NavBar() {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const navigate = useNavigate();

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleClickAdd = () => {
    setAnchorEl(null);
    navigate("/vocab/add");
  }

  const handleClickList = () => {
    setAnchorEl(null);
    navigate("/vocab");
  }

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" sx={{
        bgcolor: "black"
      }}>
        <Toolbar>
          <Typography variant="h6">Admin</Typography>
          <Button
            id="demo-positioned-button"
            aria-controls={open ? "demo-positioned-menu" : undefined}
            aria-haspopup="true"
            aria-expanded={open ? "true" : undefined}
            onClick={handleClick}
            color="inherit">
            Vocabulary
          </Button>
        </Toolbar>
        <Menu
          id="demo-positioned-menu"
          aria-labelledby="demo-positioned-button"
          anchorEl={anchorEl}
          open={open}
          onClose={handleClose}
          anchorOrigin={{
            vertical: "top",
            horizontal: "left",
          }}
          transformOrigin={{
            vertical: "top",
            horizontal: "left",
          }}
        >
          <MenuItem onClick={handleClickAdd}>Add Word</MenuItem>
          <MenuItem onClick={handleClickList}>List Words</MenuItem>
        </Menu>
      </AppBar>
    </Box>
  );
}
