export interface IOperations {
  id: number;
  cedulaBeneficiario?: string | null;
  telefonoEmisor?: string | null;
  telefonoBeneficiario?: string | null;
  monto?: string | null;
  bancoEmisor?: string | null;
  concepto?: string | null;
  referencia?: string | null;
  fechaHora?: string | null;
}

export type NewOperations = Omit<IOperations, 'id'> & { id: null };
