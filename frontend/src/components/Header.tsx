// Header.js
import React, { useEffect, useState } from "react";
import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");
    setIsLoggedIn(!!accessToken); // accessToken이 있으면 로그인된 상태로 설정
  }, []);

  const handleLogout = async () => {
    try {
      // 로그아웃 처리 로직 (백엔드에 요청)
      await fetch("/api/auth/logout", { method: "POST" });

      // 로그아웃 후 로컬 스토리지에서 토큰 제거
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");

      setIsLoggedIn(false); // 로그아웃 상태로 설정
      navigate("/"); // 로그인 페이지로 이동
    } catch (error) {
      console.error("Logout failed:", error);
      alert("Logout failed. Please try again.");
    }
  };

  return (
    <AppBar position="static">
      <Toolbar>
        {/* 로고 또는 타이틀 */}
        <Typography
          variant="h6"
          sx={{ flexGrow: 1, cursor: "pointer" }}
          onClick={() => navigate("/")}
        >
          Finance Buddy
        </Typography>
        {/* 네비게이션 메뉴 */}
        <Box>
          {isLoggedIn ? (
            // 로그인된 상태
            <>
              <Button color="inherit" onClick={() => navigate("/my-page")}>
                My Page
              </Button>
              <Button color="inherit" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            // 로그아웃된 상태
            <>
              <Button color="inherit" onClick={() => navigate("/login")}>
                Login
              </Button>
              <Button color="inherit" onClick={() => navigate("/signup")}>
                Sign Up
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
