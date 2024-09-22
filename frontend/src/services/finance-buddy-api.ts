import axios from 'axios';

// Axios 인스턴스 설정
const api = axios.create({
    baseURL: 'http://localhost:8080', // 백엔드 서버 주소
    headers: {
        'Content-Type': 'application/json',
    },
});

// 타입 정의
interface SignupRequest {
    email: string;
    password: string;
}

interface AuthResponse {
    token: string;
    provider?: string;
}

interface ProviderResponse {
    provider: string;
}

interface LogoutRequest {
    token: string;
}

interface Member {
    email: string;
    age: number;
    investmentAmount: number;
    investmentPeriod: string;
    preferredProduct: string;
}

interface ChatRequest {
    message: string;
}

interface InvestmentProfile {
    age: number;
    investmentAmount: number;
    investmentPeriod: string;
    preferredProduct: string;
}

// API 함수들

// 로그인 - 계정 기반 로그인
export const login = async (account: string): Promise<AuthResponse> => {
    const response = await api.post(`/auth/login/${account}`);
    return response.data;
};

// 회원가입
export const signup = async (signupRequest: SignupRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/signup', signupRequest);
    return response.data;
};

// 계정 찾기
export const findAccount = async (email: string): Promise<ProviderResponse> => {
    const response = await api.get(`/auth/find-account?email=${email}`);
    return response.data;
};

// 소셜 로그인 처리
export const socialLogin = async (provider: string, code: string): Promise<AuthResponse> => {
    const response = await api.get(`/auth/social-login/${provider}?code=${code}`);
    return response.data;
};

// 로그아웃
export const logout = async (logoutRequest: LogoutRequest): Promise<void> => {
    await api.post('/auth/logout', logoutRequest);
};

// 멤버 등록
export const registerMember = async (member: Member): Promise<Member> => {
    const response = await api.post('/member/register', member);
    return response.data;
};

// 멤버 정보 가져오기
export const getMember = async (email: string): Promise<Member> => {
    const response = await api.get(`/member/${email}`);
    return response.data;
};

// 챗봇 요청 처리
export const handleChatRequest = async (chatRequest: ChatRequest): Promise<void> => {
    await api.post('/chat/request', chatRequest);
};

// 프로필 저장
export const saveProfile = async (profile: InvestmentProfile): Promise<InvestmentProfile> => {
    const response = await api.post('/profiles', profile);
    return response.data;
};

// 프로필 ID로 가져오기
export const getProfileById = async (id: number): Promise<InvestmentProfile> => {
    const response = await api.get(`/profiles/${id}`);
    return response.data;
};

// 프로필 수정
export const modifyProfileById = async (id: number, profile: InvestmentProfile): Promise<InvestmentProfile> => {
    const response = await api.put(`/profiles/${id}`, profile);
    return response.data;
};
