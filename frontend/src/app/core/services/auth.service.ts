import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { AuthResponse, CurrentUser, JwtPayload, LoginRequest, RegisterRequest, UserRole } from '../models/auth.model';

const API = 'http://localhost:8080/api/v1/auth';
const TOKEN_KEY = 'ca_token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly _token = signal<string | null>(localStorage.getItem(TOKEN_KEY));

  readonly currentUser = computed<CurrentUser | null>(() => {
    const token = this._token();
    if (!token) return null;
    try {
      const payload = this.decodeJwt(token);
      if (this.isExpired(payload)) {
        this.clearToken();
        return null;
      }
      return { email: payload.sub, role: payload.role };
    } catch {
      return null;
    }
  });

  readonly isAuthenticated = computed(() => this.currentUser() !== null);
  readonly userRole = computed(() => this.currentUser()?.role ?? null);

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest) {
    return this.http.post<AuthResponse>(`${API}/login`, req).pipe(
      tap(res => this.saveToken(res.token))
    );
  }

  register(req: RegisterRequest) {
    return this.http.post<void>(`${API}/register`, req);
  }

  logout() {
    this.clearToken();
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this._token();
  }

  hasRole(role: UserRole): boolean {
    return this.userRole() === role;
  }

  hasAnyRole(...roles: UserRole[]): boolean {
    const current = this.userRole();
    return current !== null && roles.includes(current);
  }

  private saveToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
    this._token.set(token);
  }

  private clearToken() {
    localStorage.removeItem(TOKEN_KEY);
    this._token.set(null);
  }

  private decodeJwt(token: string): JwtPayload {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
  }

  private isExpired(payload: JwtPayload): boolean {
    return payload.exp * 1000 < Date.now();
  }
}
