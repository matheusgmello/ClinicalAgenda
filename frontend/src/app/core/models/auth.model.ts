export type UserRole = 'ROLE_PACIENTE' | 'ROLE_MEDICO' | 'ROLE_ADMIN';

export interface AuthResponse {
  token: string;
}

export interface LoginRequest {
  login: string;
  senha: string;
}

export interface RegisterRequest {
  login: string;
  senha: string;
  role: 'PACIENTE' | 'MEDICO' | 'ADMIN';
}

export interface JwtPayload {
  sub: string;
  role: UserRole;
  exp: number;
  iat: number;
}

export interface CurrentUser {
  email: string;
  role: UserRole;
}
