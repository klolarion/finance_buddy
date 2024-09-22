import React, { useState } from 'react';
import { Box, TextField, Button, Typography, MenuItem, Select, FormControl, InputLabel } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const InputProfilePage = () => {
    const [age, setAge] = useState('');
    const [investmentAmount, setInvestmentAmount] = useState('');
    const [investmentPeriod, setInvestmentPeriod] = useState('');
    const [preferredProduct, setPreferredProduct] = useState('');
    const navigate = useNavigate();

    const handleProfileSubmit = async (e) => {
        e.preventDefault();

        // 입력한 정보 저장 처리 로직 (API 호출 예시)
        try {
            const response = await fetch('/api/save-profile', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ age, investmentAmount, investmentPeriod, preferredProduct }),
            });

            if (response.ok) {
                // 성공 시 인덱스 페이지로 이동
                navigate('/index');
            } else {
                alert('정보 저장에 실패했습니다. 다시 시도해주세요.');
            }
        } catch (error) {
            console.error('Error saving profile:', error);
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
                Input Your Profile
            </Typography>
            <form onSubmit={handleProfileSubmit}>
                <TextField
                    label="Age"
                    type="number"
                    variant="outlined"
                    fullWidth
                    value={age}
                    onChange={(e) => setAge(e.target.value)}
                    sx={{ mb: 2, borderRadius: '8px' }}
                />
                <TextField
                    label="Investment Amount"
                    type="number"
                    variant="outlined"
                    fullWidth
                    value={investmentAmount}
                    onChange={(e) => setInvestmentAmount(e.target.value)}
                    sx={{ mb: 2, borderRadius: '8px' }}
                />
                <FormControl fullWidth sx={{ mb: 2 }}>
                    <InputLabel>Investment Period</InputLabel>
                    <Select
                        value={investmentPeriod}
                        onChange={(e) => setInvestmentPeriod(e.target.value)}
                        label="Investment Period"
                        sx={{ borderRadius: '8px' }}
                    >
                        <MenuItem value={1}>1 Year</MenuItem>
                        <MenuItem value={3}>3 Years</MenuItem>
                        <MenuItem value={5}>5 Years</MenuItem>
                        <MenuItem value={10}>10 Years</MenuItem>
                    </Select>
                </FormControl>
                <TextField
                    label="Preferred Product"
                    variant="outlined"
                    fullWidth
                    value={preferredProduct}
                    onChange={(e) => setPreferredProduct(e.target.value)}
                    sx={{ mb: 2, borderRadius: '8px' }}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ borderRadius: '8px' }}
                >
                    Save Profile
                </Button>
            </form>
        </Box>
    );
};

export default InputProfilePage;
