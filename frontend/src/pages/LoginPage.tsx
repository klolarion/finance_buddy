import React, { useState } from 'react';
import { Box, TextField, Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    const handleNext = async (e) => {
        e.preventDefault();

        // 서버에 계정을 전송해 provider 확인 (예시 API 호출)
        try {
            const response = await fetch('/api/check-provider', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email }),
            });

            const { provider } = await response.json();

            if (provider) {
                // provider에 따라 소셜 로그인 페이지로 리디렉션
                navigate(`/social-login?provider=${provider}`);
            } else {
                alert('해당 계정의 제공자를 찾을 수 없습니다.');
            }
        } catch (error) {
            console.error('Error checking provider:', error);
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
                    label="Email"
                    variant="outlined"
                    fullWidth
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    sx={{ mb: 2, borderRadius: '8px' }}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ borderRadius: '8px' }}
                >
                    Next
                </Button>
            </form>
        </Box>
    );
};

export default LoginPage;
