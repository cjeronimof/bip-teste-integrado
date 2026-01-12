export interface Beneficio {
  id?: number;
  nome: string;
  descricao?: string;
  valor: number;
  ativo: boolean;
  version?: number;
}

export interface Transferencia {
  origemId: number;
  destinoId: number;
  valor: number;
}
