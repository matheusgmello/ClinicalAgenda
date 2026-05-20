import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="min-h-screen bg-bg-primary flex overflow-hidden">

      <!-- Left panel — hero -->
      <div class="hidden lg:flex flex-col justify-between w-1/2 bg-bg-secondary border-r border-border p-12 relative overflow-hidden">
        <!-- Background grid -->
        <div class="absolute inset-0 bg-grid-pattern bg-grid opacity-30 pointer-events-none"></div>
        <!-- Glow orb -->
        <div class="absolute -top-24 -left-24 w-96 h-96 rounded-full bg-accent/5 blur-3xl pointer-events-none"></div>
        <div class="absolute bottom-0 right-0 w-64 h-64 rounded-full bg-status-info/5 blur-3xl pointer-events-none"></div>

        <!-- Logo -->
        <div class="relative flex items-center gap-3 z-10">
          <div class="w-10 h-10 rounded-xl bg-accent flex items-center justify-center">
            <svg width="20" height="20" viewBox="0 0 16 16" fill="none">
              <path d="M8 2v12M2 8h12" stroke="#06080F" stroke-width="2.5" stroke-linecap="round"/>
            </svg>
          </div>
          <span class="font-display text-text-primary font-semibold text-lg">ClinicalAgenda</span>
        </div>

        <!-- Headline -->
        <div class="relative z-10">
          <p class="font-mono text-xs text-accent uppercase tracking-widest mb-4">Sistema de Saúde — v1.0</p>
          <h1 class="font-display text-5xl font-bold leading-tight text-text-primary mb-6">
            Gestão clínica<br />
            <em class="not-italic text-gradient">sem fricção.</em>
          </h1>
          <p class="font-body text-text-secondary leading-relaxed max-w-sm">
            Agende, gerencie e monitore consultas médicas com controle total de acesso por perfil.
          </p>

          <!-- Stats -->
          <div class="grid grid-cols-3 gap-4 mt-10">
            @for (stat of stats; track stat.label) {
              <div class="border border-border rounded-xl p-4 bg-bg-card/50">
                <p class="font-mono text-2xl font-bold text-accent">{{ stat.value }}</p>
                <p class="font-body text-xs text-text-muted mt-1">{{ stat.label }}</p>
              </div>
            }
          </div>
        </div>

        <p class="relative z-10 font-mono text-xs text-text-muted">
          &copy; 2026 ClinicalAgenda — Portfolio Project
        </p>
      </div>

      <!-- Right panel — form -->
      <div class="flex-1 flex items-center justify-center px-6 py-12 lg:px-16">
        <div class="w-full max-w-sm animate-slide-up">

          <!-- Mobile logo -->
          <div class="flex items-center gap-3 mb-10 lg:hidden">
            <div class="w-8 h-8 rounded-lg bg-accent flex items-center justify-center">
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M8 2v12M2 8h12" stroke="#06080F" stroke-width="2.5" stroke-linecap="round"/>
              </svg>
            </div>
            <span class="font-display text-text-primary font-semibold">ClinicalAgenda</span>
          </div>

          <div class="mb-8">
            <h2 class="font-display text-3xl font-bold text-text-primary">Bem-vindo de volta</h2>
            <p class="font-body text-text-secondary mt-2 text-sm">Entre com suas credenciais para acessar o sistema.</p>
          </div>

          <form [formGroup]="form" (ngSubmit)="onSubmit()" class="space-y-5">
            <div>
              <label class="input-label">E-mail</label>
              <input
                type="email"
                formControlName="login"
                class="input-field"
                placeholder="seu@email.com"
                autocomplete="email"
              />
              @if (form.get('login')?.touched && form.get('login')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">E-mail inválido.</p>
              }
            </div>

            <div>
              <label class="input-label">Senha</label>
              <div class="relative">
                <input
                  [type]="showPassword() ? 'text' : 'password'"
                  formControlName="senha"
                  class="input-field pr-10"
                  placeholder="••••••••"
                  autocomplete="current-password"
                />
                <button
                  type="button"
                  (click)="showPassword.set(!showPassword())"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-text-muted hover:text-text-secondary transition-colors"
                >
                  @if (showPassword()) {
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/></svg>
                  } @else {
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                  }
                </button>
              </div>
              @if (form.get('senha')?.touched && form.get('senha')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">Senha obrigatória.</p>
              }
            </div>

            @if (error()) {
              <div class="flex items-center gap-2 px-4 py-3 rounded-lg bg-status-danger/10 border border-status-danger/20 text-status-danger text-sm">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
                {{ error() }}
              </div>
            }

            <button type="submit" class="btn-primary w-full mt-2" [disabled]="loading()">
              @if (loading()) {
                <svg class="animate-spin" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
                Entrando...
              } @else {
                Entrar no sistema
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
              }
            </button>
          </form>

          <p class="mt-6 text-center font-body text-sm text-text-secondary">
            Não tem conta?
            <a routerLink="/register" class="text-accent hover:underline ml-1 font-medium">Criar conta</a>
          </p>
        </div>
      </div>
    </div>
  `,
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  readonly showPassword = signal(false);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly stats = [
    { value: '99%', label: 'Uptime' },
    { value: '<50ms', label: 'Latência' },
    { value: '3', label: 'Perfis' },
  ];

  readonly form = this.fb.group({
    login: ['', [Validators.required, Validators.email]],
    senha: ['', Validators.required],
  });

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    const { login, senha } = this.form.getRawValue();
    this.auth.login({ login: login!, senha: senha! }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        this.error.set(err.status === 401 ? 'Credenciais inválidas.' : 'Erro ao conectar. Tente novamente.');
        this.loading.set(false);
      },
    });
  }
}
