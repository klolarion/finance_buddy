import React, { useState } from 'react';
import { Box, TextField, Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { signup } from '../services/finance-buddy-api';

const SignupPage = () => {
    const [account, setAccount] = useState('');
    const navigate = useNavigate();

    // 회원가입 요청 객체 생성
    const signupRequest = {
        account
    };

    const handleSignup = async (e) => {
        e.preventDefault();

        // 회원가입 처리 로직 (API 호출 예시)
        try {
            const response = await signup(signupRequest);

            if (response.status === 200) {
                // 회원가입 성공 시 로그인 페이지 또는 프로필 입력 페이지로 이동
                navigate('/login');
            } else {
                alert('회원가입에 실패했습니다. 다시 시도해주세요.');
            }
        } catch (error) {
            console.error('Error during signup:', error);
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
                Sign Up
            </Typography>
            <form onSubmit={handleSignup}>
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
                    Sign Up
                </Button>
            </form>
        </Box>
    );
};

export default SignupPage;
