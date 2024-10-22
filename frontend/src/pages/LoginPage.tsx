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

            const accessToken = response.headers.get('access');
            const refreshToken = response.headers.get('refresh');
            console.log(accessToken)

            if (accessToken && refreshToken) {
                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('refreshToken', refreshToken);
                console.log('토큰이 로컬 스토리지에 저장되었습니다.');
            } else {
                console.error('토큰을 가져오는 데 실패했습니다.');
        }

            const { provider } = await response.data;

      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      
      navigate("/");

    } catch (error) {
      console.error("Login error:", error);
      alert("계정이 없습니다. 회원가입 페이지로 이동합니다.");
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
        Login
      </Typography>
      <form onSubmit={handleNext}>
        <TextField
          label="사용자 계정"
          variant="outlined"
          fullWidth
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
