import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ConsultaService } from '../../../core/services/consulta.service';

@Component({
  selector: 'app-agendar',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="p-8 max-w-3xl animate-fade-in">

      <!-- Header -->
      <div class="mb-8">
        <a routerLink="/dashboard" class="inline-flex items-center gap-2 text-text-muted text-xs font-mono hover:text-text-secondary mb-4 transition-colors">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5M12 19l-7-7 7-7"/></svg>
          Dashboard
        </a>
        <p class="font-mono text-xs text-accent uppercase tracking-widest mb-2">Nova consulta</p>
        <h1 class="font-display text-4xl font-bold text-text-primary">Agendar consulta</h1>
        <p class="font-body text-text-secondary mt-2">Preencha os dados para registrar uma nova consulta.</p>
      </div>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="space-y-6">

        <!-- Tipo -->
        <div class="card p-6">
          <h2 class="font-display text-base font-semibold text-text-primary mb-4">Tipo de atendimento</h2>
          <div class="grid grid-cols-2 gap-3">
            @for (tipo of tipos; track tipo.value) {
              <button
                type="button"
                (click)="form.get('tipo')!.setValue(tipo.value)"
                class="flex items-center gap-3 p-4 rounded-xl border transition-all duration-200 text-left"
                [class]="form.get('tipo')!.value === tipo.value
                  ? 'border-accent bg-accent/10 text-accent'
                  : 'border-border bg-bg-secondary text-text-secondary hover:border-border-strong hover:text-text-primary'"
              >
                <span [innerHTML]="tipo.icon"></span>
                <div>
                  <p class="font-body text-sm font-semibold">{{ tipo.label }}</p>
                  <p class="text-xs opacity-70 mt-0.5">{{ tipo.desc }}</p>
                </div>
              </button>
            }
          </div>
        </div>

        <!-- Paciente info -->
        <div class="card p-6">
          <h2 class="font-display text-base font-semibold text-text-primary mb-4">Dados do paciente</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div class="sm:col-span-2">
              <label class="input-label">Nome completo</label>
              <input type="text" formControlName="pacienteNome" class="input-field" placeholder="Ana Lima" />
              @if (f('pacienteNome')?.touched && f('pacienteNome')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">Nome obrigatório.</p>
              }
            </div>
            <div class="sm:col-span-2">
              <label class="input-label">E-mail do paciente</label>
              <input type="email" formControlName="pacienteEmail" class="input-field" placeholder="paciente@email.com" />
              @if (f('pacienteEmail')?.touched && f('pacienteEmail')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">E-mail válido obrigatório.</p>
              }
            </div>
            <div class="sm:col-span-2">
              <label class="input-label">Descrição dos sintomas</label>
              <textarea
                formControlName="descricaoSintomas"
                class="input-field min-h-[80px] resize-none"
                placeholder="Descreva os sintomas do paciente..."
              ></textarea>
            </div>
          </div>
        </div>

        <!-- Agendamento -->
        <div class="card p-6">
          <h2 class="font-display text-base font-semibold text-text-primary mb-4">Agendamento</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label class="input-label">Data / hora início</label>
              <input type="datetime-local" formControlName="dataInicio" class="input-field" />
              @if (f('dataInicio')?.touched && f('dataInicio')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">Data de início obrigatória.</p>
              }
            </div>
            <div>
              <label class="input-label">Data / hora fim</label>
              <input type="datetime-local" formControlName="dataFim" class="input-field" />
              @if (f('dataFim')?.touched && f('dataFim')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">Data de fim obrigatória.</p>
              }
            </div>
            <div>
              <label class="input-label">Consultório</label>
              <input type="text" formControlName="consultorio" class="input-field" placeholder="Sala 101" />
              @if (f('consultorio')?.touched && f('consultorio')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">Consultório obrigatório.</p>
              }
            </div>
            <div>
              <label class="input-label">CRM do médico</label>
              <input type="text" formControlName="crmMedico" class="input-field" placeholder="CRM-SP-001" />
              @if (f('crmMedico')?.touched && f('crmMedico')?.invalid) {
                <p class="mt-1 text-xs text-status-danger">CRM obrigatório.</p>
              }
            </div>
          </div>
        </div>

        @if (error()) {
          <div class="flex items-center gap-2 px-4 py-3 rounded-lg bg-status-danger/10 border border-status-danger/20 text-status-danger text-sm">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
            {{ error() }}
          </div>
        }

        @if (success()) {
          <div class="flex items-center gap-2 px-4 py-3 rounded-lg bg-accent/10 border border-accent/20 text-accent text-sm">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
            Consulta agendada com sucesso! Redirecionando...
          </div>
        }

        <div class="flex items-center gap-3">
          <button type="submit" class="btn-primary" [disabled]="loading()">
            @if (loading()) {
              <svg class="animate-spin" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
              Agendando...
            } @else {
              Confirmar agendamento
            }
          </button>
          <a routerLink="/dashboard" class="btn-ghost">Cancelar</a>
        </div>
      </form>
    </div>
  `,
})
export class AgendarComponent {
  private fb = inject(FormBuilder);
  private consultaService = inject(ConsultaService);
  private router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly success = signal(false);

  readonly tipos = [
    {
      value: 'PRESENCIAL',
      label: 'Presencial',
      desc: 'Atendimento no consultório',
      icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>',
    },
    {
      value: 'TELECONSULTA',
      label: 'Teleconsulta',
      desc: 'Atendimento online',
      icon: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><polygon points="23 7 16 12 23 17 23 7"/><rect x="1" y="5" width="15" height="14" rx="2"/></svg>',
    },
  ];

  readonly form = this.fb.group({
    pacienteNome: ['', Validators.required],
    pacienteEmail: ['', [Validators.required, Validators.email]],
    descricaoSintomas: [''],
    dataInicio: ['', Validators.required],
    dataFim: ['', Validators.required],
    consultorio: ['', Validators.required],
    crmMedico: ['', Validators.required],
    tipo: ['PRESENCIAL', Validators.required],
  });

  f(name: string) {
    return this.form.get(name);
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    const v = this.form.getRawValue();
    this.consultaService.agendar({
      pacienteNome: v.pacienteNome!,
      pacienteEmail: v.pacienteEmail!,
      descricaoSintomas: v.descricaoSintomas ?? '',
      dataInicio: new Date(v.dataInicio!).toISOString(),
      dataFim: new Date(v.dataFim!).toISOString(),
      consultorio: v.consultorio!,
      crmMedico: v.crmMedico!,
      tipo: v.tipo as 'PRESENCIAL' | 'TELECONSULTA',
    }).subscribe({
      next: () => {
        this.success.set(true);
        setTimeout(() => this.router.navigate(['/dashboard']), 1500);
      },
      error: (err) => {
        const msg = err.error?.detail ?? 'Erro ao agendar. Verifique os dados.';
        this.error.set(msg);
        this.loading.set(false);
      },
    });
  }
}
