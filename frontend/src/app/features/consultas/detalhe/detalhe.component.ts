import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { ConsultaService } from '../../../core/services/consulta.service';
import { ConsultaDTO } from '../../../core/models/consulta.model';

@Component({
  selector: 'app-detalhe',
  standalone: true,
  imports: [RouterLink, DatePipe, ReactiveFormsModule],
  template: `
    <div class="p-8 max-w-3xl animate-fade-in">

      <!-- Back -->
      <a routerLink="/consultas" class="inline-flex items-center gap-2 text-text-muted text-xs font-mono hover:text-text-secondary mb-6 transition-colors">
        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5M12 19l-7-7 7-7"/></svg>
        Voltar às consultas
      </a>

      @if (loading()) {
        <div class="space-y-4">
          <div class="h-10 w-48 rounded-lg bg-bg-elevated animate-pulse"></div>
          <div class="card h-64 animate-pulse bg-bg-elevated"></div>
        </div>
      }

      @else if (consulta()) {
        <!-- Header -->
        <div class="flex flex-col sm:flex-row sm:items-start justify-between gap-4 mb-8">
          <div>
            <p class="font-mono text-xs text-accent uppercase tracking-widest mb-2">Detalhes da consulta</p>
            <h1 class="font-display text-4xl font-bold text-text-primary">{{ consulta()!.pacienteNome }}</h1>
            <div class="flex items-center gap-3 mt-2">
              <code class="font-mono text-xs text-text-muted bg-bg-elevated px-2 py-1 rounded">{{ consulta()!.identificador }}</code>
              <span [class]="consulta()!.tipo === 'PRESENCIAL' ? 'badge-accent' : 'badge-info'">
                {{ consulta()!.tipo === 'PRESENCIAL' ? 'Presencial' : 'Teleconsulta' }}
              </span>
            </div>
          </div>

          <!-- Admin actions -->
          @if (isAdmin()) {
            <button
              (click)="confirmarCancelamento()"
              [disabled]="cancelando()"
              class="btn-danger flex-shrink-0"
            >
              @if (cancelando()) {
                <svg class="animate-spin" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
                Cancelando...
              } @else {
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/></svg>
                Cancelar consulta
              }
            </button>
          }
        </div>

        @if (cancelConfirm()) {
          <div class="card border-status-danger/30 p-5 mb-6 bg-status-danger/5">
            <p class="font-body text-sm font-semibold text-status-danger mb-3">Confirmar cancelamento?</p>
            <p class="font-body text-sm text-text-secondary mb-4">Esta ação não pode ser desfeita. A consulta será removida do sistema.</p>
            <div class="flex gap-2">
              <button (click)="cancelar()" class="btn-danger text-xs" [disabled]="cancelando()">Sim, cancelar</button>
              <button (click)="cancelConfirm.set(false)" class="btn-ghost text-xs">Não, manter</button>
            </div>
          </div>
        }

        @if (successMsg()) {
          <div class="flex items-center gap-2 px-4 py-3 rounded-lg bg-accent/10 border border-accent/20 text-accent text-sm mb-6">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
            {{ successMsg() }}
          </div>
        }

        @if (errorMsg()) {
          <div class="flex items-center gap-2 px-4 py-3 rounded-lg bg-status-danger/10 border border-status-danger/20 text-status-danger text-sm mb-6">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/></svg>
            {{ errorMsg() }}
          </div>
        }

        <!-- Details card -->
        <div class="card p-6 mb-6">
          <h2 class="font-display text-base font-semibold text-text-primary mb-5 glow-line pb-3">Informações da consulta</h2>
          <dl class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <dt class="input-label">Paciente</dt>
              <dd class="font-body text-sm text-text-primary">{{ consulta()!.pacienteNome }}</dd>
            </div>
            <div>
              <dt class="input-label">E-mail</dt>
              <dd class="font-body text-sm text-text-primary">{{ consulta()!.pacienteEmail }}</dd>
            </div>
            <div>
              <dt class="input-label">Início</dt>
              <dd class="font-mono text-sm text-text-primary">{{ consulta()!.dataInicio | date:'dd/MM/yyyy HH:mm' }}</dd>
            </div>
            <div>
              <dt class="input-label">Fim</dt>
              <dd class="font-mono text-sm text-text-primary">{{ consulta()!.dataFim | date:'dd/MM/yyyy HH:mm' }}</dd>
            </div>
            <div>
              <dt class="input-label">Consultório</dt>
              <dd class="font-body text-sm text-text-primary">{{ consulta()!.consultorio }}</dd>
            </div>
            <div>
              <dt class="input-label">CRM Médico</dt>
              <dd class="font-mono text-sm text-text-primary">{{ consulta()!.crmMedico }}</dd>
            </div>
            @if (consulta()!.descricaoSintomas) {
              <div class="sm:col-span-2">
                <dt class="input-label">Sintomas</dt>
                <dd class="font-body text-sm text-text-secondary leading-relaxed">{{ consulta()!.descricaoSintomas }}</dd>
              </div>
            }
          </dl>
        </div>

        <!-- Medico edit form -->
        @if (isMedico()) {
          <div class="card p-6">
            <h2 class="font-display text-base font-semibold text-text-primary mb-5">Editar consulta</h2>
            <form [formGroup]="editForm" (ngSubmit)="salvar()" class="space-y-4">
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="input-label">Data / hora início</label>
                  <input type="datetime-local" formControlName="dataInicio" class="input-field" />
                </div>
                <div>
                  <label class="input-label">Data / hora fim</label>
                  <input type="datetime-local" formControlName="dataFim" class="input-field" />
                </div>
                <div>
                  <label class="input-label">Consultório</label>
                  <input type="text" formControlName="consultorio" class="input-field" />
                </div>
                <div>
                  <label class="input-label">CRM Médico</label>
                  <input type="text" formControlName="crmMedico" class="input-field" />
                </div>
                <div class="sm:col-span-2">
                  <label class="input-label">Tipo</label>
                  <select formControlName="tipo" class="input-field">
                    <option value="PRESENCIAL">Presencial</option>
                    <option value="TELECONSULTA">Teleconsulta</option>
                  </select>
                </div>
              </div>
              <div class="flex gap-3">
                <button type="submit" class="btn-primary" [disabled]="salvando()">
                  @if (salvando()) {
                    <svg class="animate-spin" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
                    Salvando...
                  } @else {
                    Salvar alterações
                  }
                </button>
              </div>
            </form>
          </div>
        }
      }

      @else if (!loading()) {
        <div class="card flex flex-col items-center justify-center py-20 text-center">
          <p class="font-display text-2xl font-bold text-text-primary mb-2">Consulta não encontrada</p>
          <p class="font-body text-sm text-text-secondary mb-6">O identificador informado não corresponde a nenhum registro.</p>
          <a routerLink="/consultas" class="btn-primary">Voltar à lista</a>
        </div>
      }
    </div>
  `,
})
export class DetalheComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private consultaService = inject(ConsultaService);

  readonly loading = signal(true);
  readonly consulta = signal<ConsultaDTO | null>(null);
  readonly cancelConfirm = signal(false);
  readonly cancelando = signal(false);
  readonly salvando = signal(false);
  readonly successMsg = signal<string | null>(null);
  readonly errorMsg = signal<string | null>(null);

  readonly isAdmin = computed(() => this.auth.hasRole('ROLE_ADMIN'));
  readonly isMedico = computed(() => this.auth.hasRole('ROLE_MEDICO'));

  readonly editForm = this.fb.group({
    dataInicio: ['', Validators.required],
    dataFim: ['', Validators.required],
    consultorio: ['', Validators.required],
    crmMedico: ['', Validators.required],
    tipo: ['PRESENCIAL', Validators.required],
  });

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.consultaService.buscarPorId(id).subscribe({
      next: (c) => {
        this.consulta.set(c);
        this.editForm.patchValue({
          dataInicio: this.toDatetimeLocal(c.dataInicio),
          dataFim: this.toDatetimeLocal(c.dataFim),
          consultorio: c.consultorio,
          crmMedico: c.crmMedico,
          tipo: c.tipo,
        });
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  confirmarCancelamento() {
    this.cancelConfirm.set(true);
  }

  cancelar() {
    const id = this.consulta()!.identificador!;
    this.cancelando.set(true);
    this.consultaService.cancelar(id).subscribe({
      next: () => {
        this.successMsg.set('Consulta cancelada com sucesso.');
        setTimeout(() => this.router.navigate(['/consultas']), 1500);
      },
      error: (err) => {
        this.errorMsg.set(err.error?.detail ?? 'Erro ao cancelar.');
        this.cancelando.set(false);
        this.cancelConfirm.set(false);
      },
    });
  }

  salvar() {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }
    const id = this.consulta()!.identificador!;
    this.salvando.set(true);
    this.errorMsg.set(null);
    const v = this.editForm.getRawValue();
    this.consultaService.alterar(id, {
      dataInicio: new Date(v.dataInicio!).toISOString(),
      dataFim: new Date(v.dataFim!).toISOString(),
      consultorio: v.consultorio!,
      crmMedico: v.crmMedico!,
      tipo: v.tipo as 'PRESENCIAL' | 'TELECONSULTA',
    }).subscribe({
      next: (updated) => {
        this.consulta.set(updated);
        this.successMsg.set('Consulta atualizada com sucesso.');
        this.salvando.set(false);
        setTimeout(() => this.successMsg.set(null), 3000);
      },
      error: (err) => {
        this.errorMsg.set(err.error?.detail ?? 'Erro ao salvar.');
        this.salvando.set(false);
      },
    });
  }

  private toDatetimeLocal(iso: string): string {
    if (!iso) return '';
    const d = new Date(iso);
    return d.toISOString().slice(0, 16);
  }
}
