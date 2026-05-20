import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ConsultaService } from '../../../core/services/consulta.service';
import { ConsultaDTO } from '../../../core/models/consulta.model';

@Component({
  selector: 'app-listar',
  standalone: true,
  imports: [RouterLink, DatePipe],
  template: `
    <div class="p-8 animate-fade-in">

      <!-- Header -->
      <div class="flex flex-col sm:flex-row sm:items-end justify-between gap-4 mb-8">
        <div>
          <p class="font-mono text-xs text-accent uppercase tracking-widest mb-2">Gerenciamento</p>
          <h1 class="font-display text-4xl font-bold text-text-primary">Consultas</h1>
          <p class="font-body text-text-secondary mt-1">
            {{ total() }} consulta{{ total() !== 1 ? 's' : '' }} registrada{{ total() !== 1 ? 's' : '' }}
          </p>
        </div>
        <div class="flex items-center gap-2">
          <span class="badge-accent font-mono">Página {{ pagina() + 1 }} de {{ totalPaginas() }}</span>
        </div>
      </div>

      <!-- Loading -->
      @if (loading()) {
        <div class="space-y-3">
          @for (i of [1,2,3,4,5]; track i) {
            <div class="card h-20 animate-pulse bg-bg-elevated"></div>
          }
        </div>
      }

      <!-- Empty -->
      @else if (consultas().length === 0) {
        <div class="card flex flex-col items-center justify-center py-20 text-center">
          <div class="w-16 h-16 rounded-2xl bg-bg-elevated flex items-center justify-center mb-4">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" class="text-text-muted"><path d="M9 5H7a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-2"/><rect x="9" y="3" width="6" height="4" rx="1"/></svg>
          </div>
          <p class="font-display text-xl font-semibold text-text-primary mb-1">Nenhuma consulta</p>
          <p class="font-body text-sm text-text-secondary">Não há consultas registradas no sistema.</p>
        </div>
      }

      <!-- Table -->
      @else {
        <div class="card overflow-hidden">
          <!-- Table header -->
          <div class="grid grid-cols-[1fr_160px_140px_120px_44px] gap-4 px-4 py-3 border-b border-border bg-bg-secondary">
            <span class="font-mono text-[11px] uppercase tracking-wider text-text-muted">Paciente</span>
            <span class="font-mono text-[11px] uppercase tracking-wider text-text-muted">Data início</span>
            <span class="font-mono text-[11px] uppercase tracking-wider text-text-muted">Consultório</span>
            <span class="font-mono text-[11px] uppercase tracking-wider text-text-muted">Tipo</span>
            <span></span>
          </div>

          <!-- Rows -->
          <div class="divide-y divide-border">
            @for (c of consultas(); track c.identificador) {
              <a
                [routerLink]="['/consultas', c.identificador]"
                class="grid grid-cols-[1fr_160px_140px_120px_44px] gap-4 px-4 py-4 items-center
                  hover:bg-bg-elevated transition-colors duration-150 group"
              >
                <div class="min-w-0">
                  <p class="font-body text-sm font-semibold text-text-primary truncate">{{ c.pacienteNome }}</p>
                  <p class="font-mono text-xs text-text-muted truncate">{{ c.identificador }}</p>
                </div>
                <span class="font-mono text-xs text-text-secondary">
                  {{ c.dataInicio | date:'dd/MM/yy HH:mm' }}
                </span>
                <span class="font-body text-sm text-text-secondary truncate">{{ c.consultorio }}</span>
                <span [class]="c.tipo === 'PRESENCIAL' ? 'badge-accent' : 'badge-info'">
                  {{ c.tipo === 'PRESENCIAL' ? 'Presencial' : 'Telec.' }}
                </span>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-text-muted opacity-0 group-hover:opacity-100 transition-opacity justify-self-end"><path d="M9 18l6-6-6-6"/></svg>
              </a>
            }
          </div>
        </div>

        <!-- Pagination -->
        @if (totalPaginas() > 1) {
          <div class="flex items-center justify-center gap-2 mt-6">
            <button
              (click)="goToPage(pagina() - 1)"
              [disabled]="pagina() === 0"
              class="btn-ghost px-3 py-2 disabled:opacity-30"
            >
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M15 18l-6-6 6-6"/></svg>
            </button>

            @for (p of pages(); track p) {
              <button
                (click)="goToPage(p)"
                class="w-9 h-9 rounded-lg text-sm font-mono font-medium transition-all duration-200"
                [class]="p === pagina()
                  ? 'bg-accent text-text-inverse'
                  : 'text-text-secondary hover:bg-bg-elevated hover:text-text-primary border border-border'"
              >
                {{ p + 1 }}
              </button>
            }

            <button
              (click)="goToPage(pagina() + 1)"
              [disabled]="pagina() >= totalPaginas() - 1"
              class="btn-ghost px-3 py-2 disabled:opacity-30"
            >
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 18l6-6-6-6"/></svg>
            </button>
          </div>
        }
      }
    </div>
  `,
})
export class ListarComponent implements OnInit {
  private consultaService = inject(ConsultaService);

  readonly loading = signal(true);
  readonly consultas = signal<ConsultaDTO[]>([]);
  readonly pagina = signal(0);
  readonly total = signal(0);
  readonly totalPaginas = signal(0);
  readonly tamanho = 10;

  get pages(): () => number[] {
    return () => {
      const count = this.totalPaginas();
      return Array.from({ length: Math.min(count, 7) }, (_, i) => i);
    };
  }

  ngOnInit() {
    this.load();
  }

  goToPage(p: number) {
    if (p < 0 || p >= this.totalPaginas()) return;
    this.pagina.set(p);
    this.load();
  }

  private load() {
    this.loading.set(true);
    this.consultaService.listar(this.pagina(), this.tamanho).subscribe({
      next: (res) => {
        this.consultas.set(res.conteudo);
        this.total.set(res.totalElementos);
        this.totalPaginas.set(res.totalPaginas);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
