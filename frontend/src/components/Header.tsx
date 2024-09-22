// Header.js
import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const Header = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        // 로그아웃 처리 로직
        fetch('/api/logout', { method: 'POST' }).then(() => {
            localStorage.removeItem('token'); // 토큰 제거
            navigate('/'); // 로그인 페이지로 이동
        });
    };

    return (
        <AppBar position="static">
            <Toolbar>
                {/* 로고 또는 타이틀 */}
                <Typography variant="h6" sx={{ flexGrow: 1, cursor: 'pointer' }} onClick={() => navigate('/')}>
                    Finance Buddy
                </Typography>

                {/* 네비게이션 메뉴 */}
                <Box>
                    <Button color="inherit" onClick={() => navigate('/')}>
                        Home
                    </Button>
                    <Button color="inherit" onClick={() => navigate('/signup')}>
                        Sign Up
                    </Button>
                    <Button color="inherit" onClick={() => navigate('/my-page')}>
                        My Page
                    </Button>
                    <Button color="inherit" onClick={handleLogout}>
                        Logout
                    </Button>
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Header;
