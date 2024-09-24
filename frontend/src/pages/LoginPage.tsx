import { useState } from "react";
import {
  Box,
  TextField,
  Button,
  Typography,
  CircularProgress,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { login } from "../services/finance-buddy-api";

const LoginPage = () => {
  const [account, setAccount] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleNext = async (e) => {
    e.preventDefault();
    setLoading(true);

    // 서버에 계정을 전송해 provider 확인 (예시 API 호출)
    try {
      const response = await login(account);

      const { accessToken, refreshToken, provider } = response.data;

      console.log(response.data);
      console.log(accessToken);
      console.log(refreshToken);

      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      if (provider) {
        // provider에 따라 소셜 로그인 페이지로 리디렉션
        navigate(`/social?provider=${provider}`);
      } else {
        navigate(`/social?provider=${provider}`);
      }
    } catch (error) {
      console.error("Login error:", error);
      alert("Account not found. Redirecting to signup page.");
      navigate("/signup");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      height="100vh"
      sx={{ padding: 3 }}
    >
      <Typography variant="h4" mb={2}>
        Enter Your Account
      </Typography>
      <form onSubmit={handleNext}>
        <TextField
          label="Account(Email)"
          variant="outlined"
          fullWidth
          type="email"
          value={account}
          onChange={(e) => setAccount(e.target.value)}
          sx={{ mb: 2, borderRadius: "8px" }}
          required
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          disabled={loading}
          sx={{ borderRadius: "8px" }}
        >
          {loading ? <CircularProgress size={24} /> : "Next"}{" "}
          {/* 로딩 중일 때 로딩 스피너 표시 */}
        </Button>
      </form>
    </Box>
  );
};

export default LoginPage;
