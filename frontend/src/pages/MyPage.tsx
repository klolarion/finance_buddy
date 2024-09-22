import React, { useState, useEffect } from 'react';
import { Box, TextField, Button, Typography, Divider } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const MyPage = () => {
    const navigate = useNavigate();
    const [userInfo, setUserInfo] = useState({
        email: '',
        age: '',
        investmentAmount: '',
        investmentPeriod: '',
        preferredProduct: '',
    });

    useEffect(() => {
        // 사용자 정보 불러오기 (API 호출 예시)
        const fetchUserInfo = async () => {
            try {
                const response = await fetch('/api/user-info', {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' },
                });
                const data = await response.json();
                setUserInfo(data);
            } catch (error) {
                console.error('Error fetching user info:', error);
            }
        };

        fetchUserInfo();
    }, []);

    const handleUpdate = async () => {
        // 정보 수정 처리 로직 (API 호출 예시)
        try {
            const response = await fetch('/api/update-user-info', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userInfo),
            });

            if (response.ok) {
                alert('정보가 성공적으로 업데이트되었습니다.');
            } else {
                alert('정보 업데이트에 실패했습니다. 다시 시도해주세요.');
            }
        } catch (error) {
            console.error('Error updating user info:', error);
        }
    };

    const handleLogout = () => {
        // 로그아웃 처리 로직
        // JWT 토큰 삭제 후 로그인 페이지로 리디렉션
        fetch('/api/logout', { method: 'POST' }).then(() => {
            navigate('/');
        });
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
                My Page
            </Typography>
            <TextField
                label="Email"
                variant="outlined"
                fullWidth
                value={userInfo.email}
                disabled
                sx={{ mb: 2, borderRadius: '8px' }}
            />
            <TextField
                label="Age"
                type="number"
                variant="outlined"
                fullWidth
                value={userInfo.age}
                onChange={(e) => setUserInfo({ ...userInfo, age: e.target.value })}
                sx={{ mb: 2, borderRadius: '8px' }}
            />
            <TextField
                label="Investment Amount"
                type="number"
                variant="outlined"
                fullWidth
                value={userInfo.investmentAmount}
                onChange={(e) =>
                    setUserInfo({ ...userInfo, investmentAmount: e.target.value })
                }
                sx={{ mb: 2, borderRadius: '8px' }}
            />
            <TextField
                label="Investment Period"
                variant="outlined"
                fullWidth
                value={userInfo.investmentPeriod}
                onChange={(e) =>
                    setUserInfo({ ...userInfo, investmentPeriod: e.target.value })
                }
                sx={{ mb: 2, borderRadius: '8px' }}
            />
            <TextField
                label="Preferred Product"
                variant="outlined"
                fullWidth
                value={userInfo.preferredProduct}
                onChange={(e) =>
                    setUserInfo({ ...userInfo, preferredProduct: e.target.value })
                }
                sx={{ mb: 2, borderRadius: '8px' }}
            />
            <Button
                variant="contained"
                color="primary"
                onClick={handleUpdate}
                fullWidth
                sx={{ mb: 2, borderRadius: '8px' }}
            >
                Update Information
            </Button>
            <Divider sx={{ width: '100%', mb: 2 }} />
            <Button
                variant="outlined"
                color="secondary"
                onClick={handleLogout}
                fullWidth
                sx={{ borderRadius: '8px' }}
            >
                Logout
            </Button>
        </Box>
    );
};

export default MyPage;
