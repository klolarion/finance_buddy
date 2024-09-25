import { useState, useEffect } from 'react';
import { Box, TextField, Button, Typography, Paper, Divider, Card, CardContent, CardActions, Chip, Stack } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { chatRequest } from '../services/finance-buddy-api';
import axios from 'axios';
import { Message, Recommendation } from '../types/finance-buddy-types';


const IndexPage = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState('');
    const [recommendations, setRecommendations] = useState<{ [key: string]: Recommendation[] }>(() => JSON.parse(localStorage.getItem('recommendations') || '{}'));
    const [details, setDetails] = useState<Recommendation[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        localStorage.setItem('recommendations', JSON.stringify(recommendations));
        // 첫 번째 추천 메시지가 있으면 첫 번째 detail을 자동으로 보여줌
        if (Object.keys(recommendations).length > 0) {
            const firstMessage = Object.keys(recommendations)[0];
            setDetails(recommendations[firstMessage] || []);
        }
    }, [recommendations]);

    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');
      
        if (!accessToken || !refreshToken) {
          navigate('/login');
          return;
        }
      
        // 헤더에 토큰을 담아 검증 요청
        axios.get('http://localhost:8080/api/index', {
            headers: {
              'Accept': 'application/json',  // JSON 형식의 응답을 기대
              'Access': accessToken,
              'Refresh': refreshToken
            }
          })
            .then((response) => {
            })
            .catch((error) => {
              navigate('/login');
              
            });
        
      }, []);

    const handleSendMessage = async () => {
        if (input.trim() === '') return;

        const newMessage = { sender: 'user', text: input };
        setMessages((prevMessages) => [...prevMessages, newMessage]);
        setInput('');

        try {
            const response = await chatRequest({ message: input });


            const newRecommendations = response.data;

            if (Array.isArray(newRecommendations)) {
                const botMessage = newRecommendations.length > 0 ? '추천 상품을 찾았습니다.' : '추천할만한 상품이 없습니다. 다시 입력해보세요.';
                setMessages((prevMessages) => [
                    ...prevMessages,
                    { sender: 'bot', text: botMessage },
                ]);

                // 사용자 입력 메시지와 해당 추천 데이터를 연결하여 로컬에 저장
                setRecommendations((prevRecommendations) => ({
                    ...prevRecommendations,
                    [input]: newRecommendations,
                }));
            } else {
                setMessages((prevMessages) => [
                    ...prevMessages,
                    { sender: 'bot', text: '데이터 타입 오류.' },
                ]);
            }
        } catch (error) {
            setMessages((prevMessages) => [
                ...prevMessages,
                { sender: 'bot', text: '챗봇과 상호작용에 실패했습니다.' },
            ]);
            
        }
    };

    const addKeyword = (keyword: string) => {
        setInput((prevInput) => `${prevInput} ${keyword}`.trim());
    };

    const handleRecommendationClick = (message: string) => {
        const selectedDetails = recommendations[message] || [];
        setDetails(selectedDetails);
    };

    const handleReset = () => {
        localStorage.removeItem('recommendations');
        setRecommendations({});
        setDetails([]);
        setMessages([]);
    };

    return (
        <Box display="flex" height="80vh" p={2} width="100%">
            {/* Chatbot Section */}
            <Box flex={2} display="flex" flexDirection="column" p={2}>
                <Typography variant="h5" mb={2}>
                    챗봇
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
                {/* 키워드 안내 */}
                <Stack direction="row" spacing={1} sx={{ mb: 2 }}>
                    <Chip label="장기" onClick={() => addKeyword('장기')} />
                    <Chip label="단기" onClick={() => addKeyword('단기')} />
                    <Chip label="안전" onClick={() => addKeyword('안전')} />
                    <Chip label="위험" onClick={() => addKeyword('위험')} />
                    <Chip label="고위험" onClick={() => addKeyword('고위험')} />
                    <Chip label="펀드" onClick={() => addKeyword('펀드')} />
                    <Chip label="채권" onClick={() => addKeyword('채권')} />
                    <Chip label="연금" onClick={() => addKeyword('연금')} />
                </Stack>
                <TextField
                    placeholder="예: 장기로 투자할만한 안정적인 펀드 추천해줘"
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
                    전송
                </Button>
                <Button
                    variant="outlined"
                    color="secondary"
                    onClick={handleReset}
                    sx={{ mt: 1, borderRadius: '8px' }}
                >
                    초기화
                </Button>
            </Box>

            <Divider orientation="vertical" flexItem />

            {/* Recommendations Section */}
            <Box flex={2} p={2}>
                <Typography variant="h5" mb={2}>
                    검색 기록
                </Typography>
                <Paper
                    variant="outlined"
                    sx={{ padding: 2, overflowY: 'auto', borderRadius: '8px', height: '85%' }}
                >
                    {Object.keys(recommendations).length > 0 ? (
                        Object.keys(recommendations).map((message, index) => (
                            <Typography
                                key={index}
                                sx={{ mb: 1, cursor: 'pointer' }}
                                onClick={() => handleRecommendationClick(message)}
                            >
                                {message}
                            </Typography>
                        ))
                    ) : (
                        <Typography color="textSecondary">검색기록이 없습니다.</Typography>
                    )}
                </Paper>
            </Box>

            <Divider orientation="vertical" flexItem />

            {/* Details Section */}
            <Box flex={4} p={2}>
                <Typography variant="h5" mb={2}>
                    추천목록
                </Typography>
                <Paper
                    variant="outlined"
                    sx={{ padding: 2, overflowY: 'auto', borderRadius: '8px', height: '85%' }}
                >
                    {details.length > 0 ? (
                        details.map((rec, index) => (
                            <Card key={index} sx={{ mb: 2, borderRadius: '8px' }}>
                                <CardContent>
                                    <Typography variant="h6" gutterBottom>
                                        {rec.name}
                                    </Typography>
                                    <Typography color="textSecondary">
                                        종류: {rec.type}
                                    </Typography>
                                    <Typography color="textSecondary">
                                        주체: {rec.issuer}
                                    </Typography>
                                    {rec.issueDate && (
                                        <Typography color="textSecondary">
                                            발행일: {rec.issueDate}
                                        </Typography>
                                    )}
                                    {rec.expiryDate && (
                                        <Typography color="textSecondary">
                                            만기일: {rec.expiryDate}
                                        </Typography>
                                    )}
                                    {rec.price && (
                                        <Typography color="textSecondary">
                                            가격: {rec.price} {rec.currency}
                                        </Typography>
                                    )}
                                        <Typography color="textSecondary">
                                            이자/배당율: {rec.interestRate}%
                                        </Typography>
                                    
                                    {rec.riskLevel && (
                                        <Typography color="textSecondary">
                                            위험도: {rec.riskLevel}
                                        </Typography>
                                    )}
                                </CardContent>
                                <CardActions>
                                    <Button size="small" color="primary">
                                        자세히 알아보기
                                    </Button>
                                </CardActions>
                            </Card>
                        ))
                    ) : (
                        <Typography color="textSecondary">추천 목록이 없습니다.</Typography>
                    )}
                </Paper>
            </Box>
        </Box>
    );
};

export default IndexPage;