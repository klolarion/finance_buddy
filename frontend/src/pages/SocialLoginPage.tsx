import React, { useEffect, useState } from 'react';
import { Box, Button, Grid, Typography } from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import NaverIcon from '@mui/icons-material/TravelExplore';  // Naver 아이콘 대체
import { useLocation, useNavigate } from 'react-router-dom';

const SocialLoginPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [provider, setProvider] = useState('');

    useEffect(() => {
        // 쿼리스트링에서 provider 추출
        const params = new URLSearchParams(location.search);
        const providerParam = params.get('provider');
        setProvider(providerParam);
    }, [location.search]);

    const handleGoogleLogin = async () => {
        try {
          // 백엔드의 Google 로그인 엔드포인트로 요청
          window.location.href = 'http://localhost:8080/oauth2/authorization/google';
        } catch (error) {
          console.error('Google Login Error:', error);
        }
      };
    
      const handleNaverLogin = async () => {
        try {
          // 백엔드의 Naver 로그인 엔드포인트로 요청
          window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
        } catch (error) {
          console.error('Naver Login Error:', error);
        }
      };

      const renderButton = () => {
        switch (provider) {
          case 'Google':
            return (
                <Grid item xs={12} sm={8}>
                  <Button
                    variant="contained"
                    fullWidth
                    startIcon={<GoogleIcon />}
                    onClick={handleGoogleLogin}
                    sx={{
                      backgroundColor: '#DB4437',
                      '&:hover': { backgroundColor: '#C33D29' },
                      color: '#fff',
                      mb: 2,
                    }}
                  >
                    Google Login
                  </Button>
                </Grid>
            );
          case 'Naver':
            return (
              <Grid item xs={12} sm={8}>
                  <Button
                    variant="contained"
                    fullWidth
                    startIcon={<NaverIcon />}
                    onClick={handleNaverLogin}
                    sx={{
                      backgroundColor: '#03C75A',
                      '&:hover': { backgroundColor: '#02984A' },
                      color: '#fff',
                    }}
                  >
                    Naver Login
                  </Button>
                </Grid>
            );
            default:
              // provider 값이 없는 경우 모든 버튼을 보여줌
              return (
                <>
                <Grid item xs={12} sm={8}>
                  <Button
                    variant="contained"
                    fullWidth
                    startIcon={<GoogleIcon />}
                    onClick={handleGoogleLogin}
                    sx={{
                      backgroundColor: '#DB4437',
                      '&:hover': { backgroundColor: '#C33D29' },
                      color: '#fff',
                      mb: 2,
                    }}
                  >
                    Google Login
                  </Button>
                </Grid>
                <Grid item xs={12} sm={8}>
                  <Button
                    variant="contained"
                    fullWidth
                    startIcon={<NaverIcon />}
                    onClick={handleNaverLogin}
                    sx={{
                      backgroundColor: '#03C75A',
                      '&:hover': { backgroundColor: '#02984A' },
                      color: '#fff',
                    }}
                  >
                    Naver Login
                  </Button>
                </Grid>
                </>
              )
        }
      };
    

      return (
        <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
          <Box textAlign="center">
            <Box
              display="flex"
              flexDirection="column"
              justifyContent="center"
              alignItems="center"
              sx={{
                width: '100%',
                maxWidth: 400, // 박스의 최대 너비
                minWidth: 400,
                padding: 4,
                backgroundColor: '#f5f5f5',
                borderRadius: 2,
                boxShadow: 3, // 약간의 그림자 추가
              }}
            >
              <Typography variant="h4" gutterBottom>
                인증
              </Typography>
              
              <Grid container spacing={2} justifyContent="center">
                {renderButton()}
              </Grid>
            </Box>
          </Box>
        </Box>
      );
};

export default SocialLoginPage;
