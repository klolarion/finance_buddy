import { useState } from "react";
import {
  Box,
  TextField,
  Button,
  Typography,
  CircularProgress,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { signup } from "../services/finance-buddy-api";

const SignupPage = () => {
  const [account, setAccount] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // 회원가입 요청 객체 생성
  const signinRequest = {
    account,
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    setLoading(true);
    // 회원가입 처리 로직 (API 호출 예시)
    try {
      const response = await signup(signinRequest);
      console.log(response.status)

      if (response.status === 200) {
        // 회원가입 성공 시 로그인 페이지 또는 프로필 입력 페이지로 이동
        navigate("/login");
      } else {
        alert("회원가입에 실패했습니다. 다시 시도해주세요.");
      }
    } catch (error) {
      console.error("Error during signup:", error);
      alert("회원 가입 중 에러가 발생했습니다.");
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
        Sign In
      </Typography>
      <form onSubmit={handleSignup}>
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
          {loading ? <CircularProgress size={24} /> : "Sign Up"}{" "}
          {/* 로딩 중일 때 로딩 스피너 표시 */}
        </Button>
      </form>
    </Box>
  );
};

export default SignupPage;
