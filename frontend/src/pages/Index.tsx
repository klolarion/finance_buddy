import { useState } from 'react';
import { Box, TextField, Button, Typography, Paper, Divider, Card, CardContent, CardActions } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { chatRequest } from '../services/finance-buddy-api';
import { Message, Recommendation } from '../types/finance-buddy-types';


const IndexPage = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState('');
    const [recommendations, setRecommendations] = useState<string[]>([]); // 중복 제거된 이름 리스트
    const [details, setDetails] = useState<Recommendation[]>([]); // 중복 제거된 추천 객체 리스트
    const navigate = useNavigate();

    const handleSendMessage = async () => {
        if (input.trim() === '') return;

        // 사용자 메시지 추가
        const newMessage = { sender: 'user', text: input };
        setMessages((prevMessages) => [...prevMessages, newMessage]);
        setInput('');

        try {
            const response = await chatRequest({ message: input });


            // 서버에서 전달된 recommendation 데이터들
            const newRecommendations = response.data;

            if (Array.isArray(newRecommendations)) {
                // 첫 번째 recommendation의 message를 챗봇 응답으로 표시
                const botMessage = newRecommendations[0]?.message || '자료없음';
                setMessages((prevMessages) => [
                    ...prevMessages,
                    { sender: 'bot', text: botMessage }, // 챗봇 응답은 중복되더라도 계속 표시
                ]);

                // 각 recommendation의 name을 누적하여 저장하되 중복 제거
                const names = new Set([...recommendations, ...newRecommendations.map((rec) => rec.name)]);
                setRecommendations(Array.from(names));

                // recommendation 객체들을 누적하여 저장하되 중복 제거
                const uniqueDetails = [...details, ...newRecommendations].filter(
                    (rec, index, self) =>
                        index === self.findIndex((t) => t.name === rec.name) // name 기준으로 중복 제거
                );
                setDetails(uniqueDetails);
            } else {
                console.error('Received data is not in the expected array format:', newRecommendations);
            }
        } catch (error) {
            console.error('Error during chatbot interaction:', error);
        }
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
