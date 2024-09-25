// src/types/types.ts

// 투자 성향 정보 타입
export interface InvestmentProfile {
    riskTolerance: string; // 예: 보수적, 중립적, 공격적
    investmentHorizon: string; // 예: 단기, 중기, 장기
}

export type Message = {
    sender: string;
    text: string;
};

export type Recommendation = {
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

// 인증 응답 타입
export interface AuthResponse {
    accessToken: string;
    refreshToken: string;
}

// 사용자 정보 타입 (예시, 필요시 사용)
export interface User {
    id: number;
    email: string;
    memberName: string;
    provider: string; // 예: Google, Naver
}

export interface SignupRequest {
    account: string;
}


export interface ProviderResponse {
    provider: string;
}

export interface LogoutRequest {
    token: string;
}


export interface ChatRequest {
    message: string;
}