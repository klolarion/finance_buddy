import { useState } from 'react';
import { Box, TextField, Button, Typography, Paper, Divider, Card, CardContent, CardActions, Chip, Stack } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { chatRequest } from '../services/finance-buddy-api';

// 메시지 타입 정의
type Message = {
    sender: string;
    text: string;
};

// Recommendation 타입 정의
type Recommendation = {
    message: string;
    name: string;
    type: string;
    issuer: string;
    issueDate?: string;
    expiryDate?: string;
    price?: number;
    currency?: string;
    category: string;
    riskLevel?: string;
    interestRate?: number;
};

const IndexPage = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState('');
    const [recommendations, setRecommendations] = useState<string[]>([]);
    const [details, setDetails] = useState<Recommendation[]>([]);
    const navigate = useNavigate();

    const handleSendMessage = async () => {
        if (input.trim() === '') return;

        const newMessage = { sender: 'user', text: input };
        setMessages((prevMessages) => [...prevMessages, newMessage]);
        setInput('');

        try {
            const response = await chatRequest({ message: input });
            console.log('Received response:', response);
            const newRecommendations = response.data;

            if (Array.isArray(newRecommendations)) {
                const botMessage = newRecommendations[0]?.message || '자료없음';
                setMessages((prevMessages) => [
                    ...prevMessages,
                    { sender: 'bot', text: botMessage },
                ]);

                // 추천 상품이 없는 경우 추가하지 않음
                const validRecommendations = newRecommendations.filter(
                    (rec) => rec.name !== '추천 상품 없음'
                );

                if (validRecommendations.length > 0) {
                    // 중복되지 않은 name만 추가
                    const names = new Set([...recommendations, ...validRecommendations.map((rec) => rec.name)]);
                    setRecommendations(Array.from(names));

                    // 중복되지 않은 추천 객체 추가
                    const uniqueDetails = [...details, ...validRecommendations].filter(
                        (rec, index, self) => index === self.findIndex((t) => t.name === rec.name)
                    );
                    setDetails(uniqueDetails);
                }
            } else {
                console.error('Received data is not in the expected array format:', newRecommendations);
            }
        } catch (error) {
            console.error('Error during chatbot interaction:', error);
        }
    };

    const addKeyword = (keyword: string) => {
        setInput((prevInput) => `${prevInput} ${keyword}`.trim());
    };

    return (
        <Box display="flex" height="80vh" p={2} width="100%">
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
                {/* 키워드 안내 */}
                <Stack direction="row" spacing={1} sx={{ mb: 2 }}>
                    <Chip label="장기" onClick={() => addKeyword('장기')} />
                    <Chip label="단기" onClick={() => addKeyword('단기')} />
                    <Chip label="안정" onClick={() => addKeyword('안정')} />
                    <Chip label="위험" onClick={() => addKeyword('위험')} />
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
                    Send
                </Button>
            </Box>

            <Divider orientation="vertical" flexItem />

            {/* Recommendations Section */}
            <Box flex={2} p={2}>
                <Typography variant="h5" mb={2}>
                    Recommendations
                </Typography>
                <Paper
                    variant="outlined"
                    sx={{ padding: 2, overflowY: 'auto', borderRadius: '8px', height: '85%' }}
                >
                    {recommendations.length > 0 ? (
                        recommendations.map((name, index) => (
                            <Typography key={index} sx={{ mb: 1 }}>
                                {name}
                            </Typography>
                        ))
                    ) : (
                        <Typography color="textSecondary">No recommendations yet.</Typography>
                    )}
                </Paper>
            </Box>

            <Divider orientation="vertical" flexItem />

            {/* Details Section */}
            <Box flex={4} p={2}>
                <Typography variant="h5" mb={2}>
                    Details
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
                                        Type: {rec.type}
                                    </Typography>
                                    <Typography color="textSecondary">
                                        Issuer: {rec.issuer}
                                    </Typography>
                                    {rec.issueDate && (
                                        <Typography color="textSecondary">
                                            Issue Date: {rec.issueDate}
                                        </Typography>
                                    )}
                                    {rec.expiryDate && (
                                        <Typography color="textSecondary">
                                            Expiry Date: {rec.expiryDate}
                                        </Typography>
                                    )}
                                    {rec.price && (
                                        <Typography color="textSecondary">
                                            Price: {rec.price} {rec.currency}
                                        </Typography>
                                    )}
                                    {rec.interestRate && (
                                        <Typography color="textSecondary">
                                            Interest Rate: {rec.interestRate}%
                                        </Typography>
                                    )}
                                    {rec.riskLevel && (
                                        <Typography color="textSecondary">
                                            Risk Level: {rec.riskLevel}
                                        </Typography>
                                    )}
                                </CardContent>
                                <CardActions>
                                    <Button size="small" color="primary">
                                        Learn More
                                    </Button>
                                </CardActions>
                            </Card>
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