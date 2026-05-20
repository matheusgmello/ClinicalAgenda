import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

interface NavItem {
  label: string;
  route: string;
  icon: string;
  roles?: string[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <aside class="flex flex-col h-full w-64 bg-white border-r border-border-subtle shadow-card">
      <!-- Logo -->
      <div class="px-6 py-5 border-b border-border-subtle">
        <div class="flex items-center gap-3">
          <div class="w-8 h-8 rounded-lg bg-accent flex items-center justify-center flex-shrink-0">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M8 2v12M2 8h12" stroke="white" stroke-width="2.5" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <p class="font-display font-semibold text-text-primary text-sm leading-none">ClinicalAgenda</p>
            <p class="font-mono text-[10px] text-accent mt-0.5">v1.0 — health system</p>
          </div>
        </div>
      </div>

      <!-- User info -->
      <div class="px-4 py-4 border-b border-border-subtle">
        <div class="flex items-center gap-3 px-2 py-2.5 rounded-lg bg-bg-secondary">
          <div class="w-8 h-8 rounded-full bg-accent/10 border border-accent/20 flex items-center justify-center flex-shrink-0">
            <span class="font-display text-accent text-xs font-bold">{{ userInitial() }}</span>
          </div>
          <div class="min-w-0">
            <p class="text-text-primary text-xs font-semibold truncate">{{ userEmail() }}</p>
            <span class="badge-accent text-[10px] px-1.5 py-0.5 mt-0.5 inline-block">{{ roleBadge() }}</span>
          </div>
        </div>
      </div>

      <!-- Nav -->
      <nav class="flex-1 px-3 py-4 overflow-y-auto">
        <p class="px-3 mb-2 text-[10px] font-mono font-medium uppercase tracking-widest text-text-muted">Navegação</p>
        @for (item of visibleItems(); track item.route) {
          <a
            [routerLink]="item.route"
            routerLinkActive="bg-accent/8 text-accent border-accent/20 font-semibold"
            [routerLinkActiveOptions]="{ exact: item.route === '/dashboard' }"
            class="flex items-center gap-3 px-3 py-2.5 rounded-lg mb-0.5 text-text-secondary border border-transparent
              hover:bg-bg-elevated hover:text-text-primary
              transition-all duration-200 text-sm font-medium group"
          >
            <span class="w-5 h-5 flex items-center justify-center flex-shrink-0 opacity-60 group-hover:opacity-100 transition-opacity" [innerHTML]="item.icon"></span>
            {{ item.label }}
          </a>
        }
      </nav>

      <!-- Logout -->
      <div class="px-3 py-4 border-t border-border-subtle">
        <button
          (click)="logout()"
          class="flex items-center gap-3 w-full px-3 py-2.5 rounded-lg text-text-muted
            hover:bg-red-50 hover:text-status-danger
            transition-all duration-200 text-sm font-medium"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9"/>
          </svg>
          Sair da conta
        </button>
      </div>
    </aside>
  `,
})
export class SidebarComponent {
  private auth = inject(AuthService);

  private readonly allItems: NavItem[] = [
    {
      label: 'Dashboard',
      route: '/dashboard',
      icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/></svg>',
    },
    {
      label: 'Agendar Consulta',
      route: '/consultas/agendar',
      icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><rect x="3" y="4" width="18" height="18" rx="2"/><path d="M16 2v4M8 2v4M3 10h18M12 14v4M10 16h4"/></svg>',
      roles: ['ROLE_PACIENTE'],
    },
    {
      label: 'Consultas',
      route: '/consultas',
      icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><path d="M9 5H7a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-2"/><rect x="9" y="3" width="6" height="4" rx="1"/><path d="M9 12h6M9 16h4"/></svg>',
      roles: ['ROLE_MEDICO', 'ROLE_ADMIN'],
    },
  ];

  readonly visibleItems = computed(() => {
    const role = this.auth.userRole();
    return this.allItems.filter(item => {
      if (!item.roles) return true;
      return role && item.roles.includes(role);
    });
  });

  readonly userEmail = computed(() => this.auth.currentUser()?.email ?? '—');
  readonly userInitial = computed(() => {
    const email = this.auth.currentUser()?.email ?? '';
    return email.charAt(0).toUpperCase();
  });
  readonly roleBadge = computed(() => {
    const role = this.auth.userRole();
    const map: Record<string, string> = {
      ROLE_PACIENTE: 'Paciente',
      ROLE_MEDICO: 'Médico',
      ROLE_ADMIN: 'Admin',
    };
    return role ? map[role] ?? role : '';
  });

  logout() {
    this.auth.logout();
  }
}
