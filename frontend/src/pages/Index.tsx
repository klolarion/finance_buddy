import React, { useState, useEffect } from 'react';
import { Box, TextField, Button, Typography, Paper, Divider } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const IndexPage = () => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const [recommendations, setRecommendations] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // 토큰 검증 로직 (API 호출)
        const verifyToken = async () => {
            try {
                const response = await fetch('/api/verify-token', {
                    method: 'GET',
                    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` },
                });
                if (!response.ok) {
                    throw new Error('Invalid token');
                }
            } catch (error) {
                alert('로그인이 필요합니다.');
                navigate('/');
            }
        };

        verifyToken();
    }, [navigate]);

    const handleSendMessage = async () => {
        if (input.trim() === '') return;

        // 사용자 메시지 추가
        const newMessage = { sender: 'user', text: input };
        setMessages([...messages, newMessage]);

        // 입력 초기화
        setInput('');

        // 챗봇 응답 요청
        try {
            const response = await fetch('/api/chatbot', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: input }),
            });
            const data = await response.json();

            // 챗봇 응답 추가
            setMessages((prevMessages) => [
                ...prevMessages,
                { sender: 'bot', text: data.response },
            ]);

            // 추천 결과 업데이트
            setRecommendations(data.recommendations);
        } catch (error) {
            console.error('Error during chatbot interaction:', error);
        }
    };

    return (
        <Box display="flex" height="100vh">
            {/* Chatbot Section */}
            <Box flex={2} display="flex" flexDirection="column" p={2}>
                <Typography variant="h5" mb={2}>
                    Chatbot
                </Typography>
                <Paper
                    variant="outlined"
                    sx={{ flex: 1, mb: 2, padding: 2, overflowY: 'auto', borderRadius: '8px' }}
                >
                    {messages.map((msg, index) => (
                        <Typography
                            key={index}
                            align={msg.sender === 'user' ? 'right' : 'left'}
                            sx={{ mb: 1 }}
                        >
                            <strong>{msg.sender === 'user' ? 'You' : 'Bot'}:</strong> {msg.text}
                        </Typography>
                    ))}
                </Paper>
                <TextField
                    placeholder="Type your message..."
                    variant="outlined"
                    fullWidth
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
                    sx={{ borderRadius: '8px' }}
                />
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSendMessage}
                    sx={{ mt: 1, borderRadius: '8px' }}
                >
                    Send
                </Button>
            </Box>

            <Divider orientation="vertical" flexItem />

            {/* Recommendations Section */}
            <Box flex={1} p={2}>
                <Typography variant="h5" mb={2}>
                    Recommendations
                </Typography>
                <Paper
                    variant="outlined"
                    sx={{ padding: 2, overflowY: 'auto', borderRadius: '8px', height: '85%' }}
                >
                    {recommendations.length > 0 ? (
                        recommendations.map((rec, index) => (
                            <Typography key={index} sx={{ mb: 1 }}>
                                {rec}
                            </Typography>
                        ))
                    ) : (
                        <Typography color="textSecondary">No recommendations yet.</Typography>
                    )}
                </Paper>
            </Box>
        </Box>
    );
};

export default IndexPage;
