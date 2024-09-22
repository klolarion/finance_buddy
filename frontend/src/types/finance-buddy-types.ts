// src/types/types.ts

// 투자 성향 정보 타입
export interface InvestmentProfile {
    riskTolerance: string; // 예: 보수적, 중립적, 공격적
    investmentHorizon: string; // 예: 단기, 중기, 장기
}

// 추천 항목 타입
export interface Recommendation {
    name: string;
    description: string;
}

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
