export type TipoConsulta = 'PRESENCIAL' | 'TELECONSULTA';

export interface ConsultaDTO {
  id?: number;
  pacienteNome: string;
  pacienteEmail: string;
  descricaoSintomas: string;
  identificador?: string;
  dataInicio: string;
  dataFim: string;
  consultorio: string;
  crmMedico: string;
  imgReceitaUrl?: string;
  tipo: TipoConsulta;
}

export interface PaginaResponse<T> {
  conteudo: T[];
  paginaAtual: number;
  tamanhoPagina: number;
  totalElementos: number;
  totalPaginas: number;
}
