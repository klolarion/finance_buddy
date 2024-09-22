import React, { useEffect, useState } from 'react';
import { Box, Button, Typography } from '@mui/material';
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

    const handleSocialLogin = () => {
        // 각 소셜 로그인 버튼 클릭 시 로그인 처리
        switch (provider) {
            case 'Google':
                // Google 로그인 로직 예시
                window.location.href = '/api/auth/google'; // 실제 Google 로그인 API 경로로 변경 필요
                break;
            case 'Kakao':
                // Kakao 로그인 로직 예시
                window.location.href = '/api/auth/kakao'; // 실제 Kakao 로그인 API 경로로 변경 필요
                break;
            default:
                alert('지원하지 않는 제공자입니다.');
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
            <Typography variant="h5" mb={3}>
                {provider} Login
            </Typography>
            {provider ? (
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSocialLogin}
                    sx={{ borderRadius: '8px' }}
                >
                    Continue with {provider}
                </Button>
            ) : (
                <Typography color="error">
                    유효하지 않은 제공자입니다. 다시 시도하세요.
                </Typography>
            )}
        </Box>
    );
};

export default SocialLoginPage;
