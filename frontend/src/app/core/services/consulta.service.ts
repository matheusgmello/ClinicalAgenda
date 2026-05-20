import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ConsultaDTO, PaginaResponse } from '../models/consulta.model';

const API = 'http://localhost:8080/api/v1/consultas';

@Injectable({ providedIn: 'root' })
export class ConsultaService {
  constructor(private http: HttpClient) {}

  agendar(dto: ConsultaDTO) {
    return this.http.post<ConsultaDTO>(`${API}/agendar`, dto);
  }

  listar(pagina = 0, tamanho = 10) {
    const params = new HttpParams().set('pagina', pagina).set('tamanho', tamanho);
    return this.http.get<PaginaResponse<ConsultaDTO>>(`${API}/listar`, { params });
  }

  buscarPorId(identificador: string) {
    return this.http.get<ConsultaDTO>(`${API}/listar/${identificador}`);
  }

  alterar(identificador: string, dto: Partial<ConsultaDTO>) {
    return this.http.put<ConsultaDTO>(`${API}/alterar/${identificador}`, dto);
  }

  cancelar(identificador: string) {
    return this.http.delete<void>(`${API}/cancelar/${identificador}`);
  }
}
