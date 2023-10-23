import {
  Box,
  Divider,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
} from "@mui/material";
import { Link } from "react-router-dom";
import { styled } from "@mui/system";

const SideBar = ({ open, onClose, routes, width = 160 }) => {
  const StyledDrawer = styled(Drawer)(() => ({
    "& .MuiDrawer-paper": {
      backgroundColor: "#1976d2",
      color: "#fff",
      boxSizing: "border-box",
      width: width,
    },
  }));

  const drawerComp = (
    <Box>
      <Divider />
      <List>
        {routes.map((route, index) => (
          <ListItem key={`sidebar_item_${index}`} disablePadding>
            <ListItemButton
              key={`sidebar_button_${index}`}
              component={Link}
              to={route.path}
            >
              <ListItemText primary={route.label} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );

  return (
    <Box
      component="nav"
      sx={{ width: { sm: width }, flexShrink: { sm: 0 } }}
      aria-label="sidebar_box"
    >
      <StyledDrawer
        variant="temporary"
        open={open}
        onClose={() => onClose()}
        ModalProps={{
          keepMounted: true,
        }}
        sx={{
          display: { xs: "block", sm: "none" },
        }}
      >
        {drawerComp}
      </StyledDrawer>
      <StyledDrawer
        variant="permanent"
        sx={{
          display: { xs: "none", sm: "block" },
        }}
        open
      >
        {drawerComp}
      </StyledDrawer>
    </Box>
  );
};

export default SideBar;
