import axios from 'axios';
import { ChatRequest, SignupRequest } from '../types/finance-buddy-types';

// Axios 인스턴스 설정
const api = axios.create({
    baseURL: 'http://localhost:8080/api', // 백엔드 서버 주소
    headers: {
        'Content-Type': 'application/json',
    },
});



// 로그인 - 계정 기반 로그인
export const login = async (account: string) => {
    console.log(account)
    const response = await api.post(`/auth/login/${account}`);
    
    return response;
};

// 회원가입
export const signup = async (signupRequest: SignupRequest) => {
    const response = await api.post('/auth/signup', signupRequest);
    return response;
};

// 챗봇 요청 처리
export const chatRequest = async (chatRequest: ChatRequest) => {
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');

    // 토큰이 없을 경우 에러 처리
    if (!accessToken || !refreshToken) {
        throw new Error('Access and refresh tokens are missing');
    }

    const response = await api.post('/chat/request', chatRequest, {
        headers: {
            'Access': accessToken,
            'Refresh': refreshToken
        }
    });

    return response;
};

