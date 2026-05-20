import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

type RoleOption = { value: 'PACIENTE' | 'MEDICO' | 'ADMIN'; label: string; desc: string; icon: string };

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="min-h-screen bg-bg-primary flex items-center justify-center px-6 py-12">
      <div class="w-full max-w-lg animate-slide-up">

        <!-- Back link -->
        <a routerLink="/login" class="inline-flex items-center gap-2 text-text-muted text-sm font-body hover:text-text-secondary mb-8 transition-colors">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5M12 19l-7-7 7-7"/></svg>
          Voltar ao login
        </a>

        <!-- Header -->
        <div class="mb-8">
          <div class="flex items-center gap-3 mb-6">
            <div class="w-9 h-9 rounded-xl bg-accent flex items-center justify-center">
              <svg width="18" height="18" viewBox="0 0 16 16" fill="none">
                <path d="M8 2v12M2 8h12" stroke="white" stroke-width="2.5" stroke-linecap="round"/>
              </svg>
            </div>
            <span class="font-display text-text-primary font-semibold">ClinicalAgenda</span>
          </div>
          <h2 class="font-display text-3xl font-bold text-text-primary">Criar conta</h2>
          <p class="font-body text-text-secondary mt-2 text-sm">Preencha os dados e selecione seu perfil de acesso.</p>
        </div>

        <form [formGroup]="form" (ngSubmit)="onSubmit()" class="space-y-6">

          <!-- Role selection -->
          <div>
            <label class="input-label mb-3">Perfil de acesso</label>
            <div class="grid grid-cols-3 gap-3">
              @for (role of roles; track role.value) {
                <button
                  type="button"
                  (click)="selectRole(role.value)"
                  class="flex flex-col items-center gap-2 p-4 rounded-xl border transition-all duration-200 text-center"
                  [class]="selectedRole() === role.value
                    ? 'border-accent bg-accent/10 text-accent shadow-glow-sm'
                    : 'border-border bg-bg-secondary text-text-secondary hover:border-border-strong hover:text-text-primary'"
                >
                  <span [innerHTML]="role.icon"></span>
                  <span class="text-xs font-semibold font-body">{{ role.label }}</span>
                  <span class="text-[10px] leading-tight opacity-70">{{ role.desc }}</span>
                </button>
              }
            </div>
          </div>

          <!-- Email -->
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
              <p class="mt-1 text-xs text-status-danger">Informe um e-mail válido.</p>
            }
          </div>

          <!-- Password -->
          <div>
            <label class="input-label">Senha</label>
            <input
              type="password"
              formControlName="senha"
              class="input-field"
              placeholder="mínimo 6 caracteres"
              autocomplete="new-password"
            />
            @if (form.get('senha')?.touched && form.get('senha')?.hasError('minlength')) {
              <p class="mt-1 text-xs text-status-danger">Senha deve ter ao menos 6 caracteres.</p>
            }
          </div>

          @if (error()) {
            <div class="flex items-center gap-2 px-4 py-3 rounded-lg bg-status-danger/10 border border-status-danger/20 text-status-danger text-sm">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
              {{ error() }}
            </div>
          }

          @if (success()) {
            <div class="flex items-center gap-2 px-4 py-3 rounded-lg bg-status-success/10 border border-status-success/20 text-status-success text-sm">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
              Conta criada! Redirecionando para login...
            </div>
          }

          <button type="submit" class="btn-primary w-full" [disabled]="loading() || !selectedRole()">
            @if (loading()) {
              <svg class="animate-spin" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
              Criando conta...
            } @else {
              Criar conta
            }
          </button>
        </form>

        <p class="mt-6 text-center font-body text-sm text-text-secondary">
          Já tem conta?
          <a routerLink="/login" class="text-accent hover:underline ml-1 font-medium">Fazer login</a>
        </p>
      </div>
    </div>
  `,
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly success = signal(false);
  readonly selectedRole = signal<'PACIENTE' | 'MEDICO' | 'ADMIN' | null>(null);

  readonly roles: RoleOption[] = [
    {
      value: 'PACIENTE',
      label: 'Paciente',
      desc: 'Agendar consultas',
      icon: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>',
    },
    {
      value: 'MEDICO',
      label: 'Médico',
      desc: 'Gerir consultas',
      icon: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>',
    },
    {
      value: 'ADMIN',
      label: 'Admin',
      desc: 'Controle total',
      icon: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>',
    },
  ];

  readonly form = this.fb.group({
    login: ['', [Validators.required, Validators.email]],
    senha: ['', [Validators.required, Validators.minLength(6)]],
  });

  selectRole(role: 'PACIENTE' | 'MEDICO' | 'ADMIN') {
    this.selectedRole.set(role);
  }

  onSubmit() {
    if (this.form.invalid || !this.selectedRole()) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    const { login, senha } = this.form.getRawValue();
    this.auth.register({ login: login!, senha: senha!, role: this.selectedRole()! }).subscribe({
      next: () => {
        this.success.set(true);
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.error.set(err.status === 400 ? 'E-mail já cadastrado.' : 'Erro ao criar conta. Tente novamente.');
        this.loading.set(false);
      },
    });
  }
}
