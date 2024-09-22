import { useState } from 'react';
import { Box, TextField, Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/finance-buddy-api';

const LoginPage = () => {
    const [account, setAccount] = useState('');
    const navigate = useNavigate();

    const handleNext = async (e) => {
        e.preventDefault();

        // 서버에 계정을 전송해 provider 확인 (예시 API 호출)
        try {
            const response = await login(account);

            const { provider } = await response.data;

            if (provider) {
                // provider에 따라 소셜 로그인 페이지로 리디렉션
                navigate(`/social?provider=${provider}`);
            } else {
                navigate(`/social?provider=${provider}`)
            }
        } catch (error) {
            navigate('/signup')
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
                    label="Account"
                    variant="outlined"
                    fullWidth
                    value={account}
                    onChange={(e) => setAccount(e.target.value)}
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
