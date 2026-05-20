import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ConsultaService } from '../../core/services/consulta.service';
import { ConsultaDTO } from '../../core/models/consulta.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, DatePipe],
  template: `
    <div class="p-8 animate-fade-in">

      <!-- Header -->
      <div class="mb-8">
        <p class="font-mono text-xs text-accent uppercase tracking-widest mb-2">Painel de controle</p>
        <h1 class="font-display text-4xl font-bold text-text-primary">
          Olá, <em class="not-italic text-gradient">{{ firstName() }}</em>
        </h1>
        <p class="font-body text-text-secondary mt-2">
          {{ today | date:'EEEE, d \'de\' MMMM \'de\' y':'':'pt' }}
        </p>
      </div>

      <!-- Stats grid -->
      <div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4 mb-8">
        @for (stat of stats(); track stat.label) {
          <div class="stat-card animate-slide-up" [style.animation-delay]="stat.delay">
            <div class="flex items-center justify-between">
              <span class="text-text-muted text-xs font-mono uppercase tracking-wider">{{ stat.label }}</span>
              <span
                class="w-8 h-8 rounded-lg flex items-center justify-center"
                [class]="stat.iconBg"
                [innerHTML]="stat.icon"
              ></span>
            </div>
            <div>
              <p class="font-display text-3xl font-bold text-text-primary">{{ stat.value }}</p>
              <p class="font-body text-xs text-text-muted mt-0.5">{{ stat.sub }}</p>
            </div>
          </div>
        }
      </div>

      <!-- Content grid -->
      <div class="grid grid-cols-1 xl:grid-cols-3 gap-6">

        <!-- Recent consultas -->
        <div class="xl:col-span-2 card p-6">
          <div class="flex items-center justify-between mb-5">
            <h2 class="font-display text-lg font-semibold text-text-primary">Consultas recentes</h2>
            @if (canListConsultas()) {
              <a routerLink="/consultas" class="text-xs text-accent hover:underline font-mono">Ver todas →</a>
            }
          </div>

          @if (loadingConsultas()) {
            <div class="space-y-3">
              @for (i of [1,2,3]; track i) {
                <div class="h-16 rounded-lg bg-bg-elevated animate-pulse"></div>
              }
            </div>
          } @else if (recentConsultas().length === 0) {
            <div class="flex flex-col items-center justify-center py-12 text-center">
              <div class="w-12 h-12 rounded-full bg-bg-elevated flex items-center justify-center mb-3">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" class="text-text-muted"><rect x="3" y="4" width="18" height="18" rx="2"/><path d="M16 2v4M8 2v4M3 10h18"/></svg>
              </div>
              <p class="font-body text-text-secondary text-sm">Nenhuma consulta encontrada.</p>
              @if (isPaciente()) {
                <a routerLink="/consultas/agendar" class="btn-primary mt-4 text-xs">Agendar agora</a>
              }
            </div>
          } @else {
            <div class="space-y-2">
              @for (c of recentConsultas(); track c.identificador) {
                <a
                  [routerLink]="['/consultas', c.identificador]"
                  class="flex items-center gap-4 p-3 rounded-lg hover:bg-bg-elevated border border-transparent hover:border-border-subtle transition-all duration-200 group"
                >
                  <div class="w-10 h-10 rounded-lg flex items-center justify-center flex-shrink-0"
                    [class]="c.tipo === 'PRESENCIAL' ? 'bg-accent/10 text-accent' : 'bg-status-info/10 text-status-info'">
                    @if (c.tipo === 'PRESENCIAL') {
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>
                    } @else {
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="23 7 16 12 23 17 23 7"/><rect x="1" y="5" width="15" height="14" rx="2" ry="2"/></svg>
                    }
                  </div>
                  <div class="flex-1 min-w-0">
                    <p class="font-body text-sm font-semibold text-text-primary truncate">{{ c.pacienteNome }}</p>
                    <p class="font-mono text-xs text-text-muted">{{ c.dataInicio | date:'dd/MM/yy HH:mm' }} · {{ c.consultorio }}</p>
                  </div>
                  <div class="flex items-center gap-2 flex-shrink-0">
                    <span [class]="c.tipo === 'PRESENCIAL' ? 'badge-accent' : 'badge-info'">
                      {{ c.tipo === 'PRESENCIAL' ? 'Presencial' : 'Teleconsulta' }}
                    </span>
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-text-muted opacity-0 group-hover:opacity-100 transition-opacity"><path d="M9 18l6-6-6-6"/></svg>
                  </div>
                </a>
              }
            </div>
          }
        </div>

        <!-- Quick actions -->
        <div class="space-y-4">
          <div class="card p-6">
            <h2 class="font-display text-lg font-semibold text-text-primary mb-4">Ações rápidas</h2>
            <div class="space-y-2">
              @if (isPaciente()) {
                <a routerLink="/consultas/agendar" class="flex items-center gap-3 p-3 rounded-lg bg-accent/5 border border-accent/20 hover:bg-accent/10 transition-all duration-200 group">
                  <div class="w-8 h-8 rounded-lg bg-accent/10 flex items-center justify-center text-accent">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><path d="M16 2v4M8 2v4M3 10h18M12 14v4M10 16h4"/></svg>
                  </div>
                  <div>
                    <p class="font-body text-sm font-semibold text-accent">Agendar consulta</p>
                    <p class="font-body text-xs text-text-muted">Marque sua próxima consulta</p>
                  </div>
                </a>
              }
              @if (canListConsultas()) {
                <a routerLink="/consultas" class="flex items-center gap-3 p-3 rounded-lg border border-border hover:bg-bg-elevated hover:border-border-strong transition-all duration-200">
                  <div class="w-8 h-8 rounded-lg bg-bg-elevated flex items-center justify-center text-text-secondary">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 5H7a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-2"/><rect x="9" y="3" width="6" height="4" rx="1"/></svg>
                  </div>
                  <div>
                    <p class="font-body text-sm font-semibold text-text-primary">Ver consultas</p>
                    <p class="font-body text-xs text-text-muted">Lista completa paginada</p>
                  </div>
                </a>
              }
            </div>
          </div>

          <!-- Role badge -->
          <div class="card p-6">
            <p class="font-mono text-xs text-text-muted uppercase tracking-widest mb-3">Seu perfil</p>
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 rounded-full bg-accent/10 border border-accent/20 flex items-center justify-center">
                <span class="font-display text-accent font-bold">{{ firstName().charAt(0) }}</span>
              </div>
              <div>
                <p class="font-body text-sm font-semibold text-text-primary">{{ userEmail() }}</p>
                <span class="badge-accent mt-1">{{ roleBadge() }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class DashboardComponent implements OnInit {
  private auth = inject(AuthService);
  private consultaService = inject(ConsultaService);

  readonly today = new Date();
  readonly loadingConsultas = signal(false);
  readonly recentConsultas = signal<ConsultaDTO[]>([]);
  readonly totalConsultas = signal(0);

  readonly isPaciente = computed(() => this.auth.hasRole('ROLE_PACIENTE'));
  readonly canListConsultas = computed(() => this.auth.hasAnyRole('ROLE_MEDICO', 'ROLE_ADMIN'));

  readonly userEmail = computed(() => this.auth.currentUser()?.email ?? '');
  readonly firstName = computed(() => {
    const email = this.auth.currentUser()?.email ?? 'usuário';
    return email.split('@')[0].split('.')[0];
  });

  readonly roleBadge = computed(() => {
    const map: Record<string, string> = {
      ROLE_PACIENTE: 'Paciente',
      ROLE_MEDICO: 'Médico',
      ROLE_ADMIN: 'Administrador',
    };
    return map[this.auth.userRole() ?? ''] ?? '';
  });

  readonly stats = computed(() => [
    {
      label: 'Total consultas',
      value: String(this.totalConsultas()),
      sub: 'registradas no sistema',
      delay: '0ms',
      iconBg: 'bg-accent/10 text-accent',
      icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><path d="M16 2v4M8 2v4M3 10h18"/></svg>',
    },
    {
      label: 'Perfil',
      value: this.roleBadge(),
      sub: 'nível de acesso atual',
      delay: '50ms',
      iconBg: 'bg-status-info/10 text-status-info',
      icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>',
    },
    {
      label: 'Tipos',
      value: '2',
      sub: 'Presencial · Teleconsulta',
      delay: '100ms',
      iconBg: 'bg-status-warning/10 text-status-warning',
      icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="23 7 16 12 23 17 23 7"/><rect x="1" y="5" width="15" height="14" rx="2"/></svg>',
    },
    {
      label: 'API Status',
      value: 'Online',
      sub: 'Spring Boot · localhost:8080',
      delay: '150ms',
      iconBg: 'bg-accent/10 text-accent',
      icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>',
    },
  ]);

  ngOnInit() {
    if (this.canListConsultas()) {
      this.loadingConsultas.set(true);
      this.consultaService.listar(0, 5).subscribe({
        next: (res) => {
          this.recentConsultas.set(res.conteudo);
          this.totalConsultas.set(res.totalElementos);
          this.loadingConsultas.set(false);
        },
        error: () => this.loadingConsultas.set(false),
      });
    }
  }
}
