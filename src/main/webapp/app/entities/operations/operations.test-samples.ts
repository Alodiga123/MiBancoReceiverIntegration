import { IOperations, NewOperations } from './operations.model';

export const sampleWithRequiredData: IOperations = {
  id: 99528,
  cedulaBeneficiario: 'Apartamento',
  telefonoEmisor: 'Salchichas Gri',
  telefonoBeneficiario: 'redundant AGP',
  monto: 'Plástico Jefe',
  bancoEmisor: 'with',
  concepto: 'Artesanal',
  referencia: 'Investment bypa',
  fechaHora: 'Costa India',
};

export const sampleWithPartialData: IOperations = {
  id: 68409,
  cedulaBeneficiario: 'users Quinta',
  telefonoEmisor: 'Cuba implement',
  telefonoBeneficiario: 'Pakistan',
  monto: 'Hormigon Rampa Agent',
  bancoEmisor: 'Músi',
  concepto: 'Verde Corporativo',
  referencia: 'Dinánmico Centr',
  fechaHora: 'viral Tajikistan withdrawal',
};

export const sampleWithFullData: IOperations = {
  id: 23581,
  cedulaBeneficiario: 'Plástico Corpora',
  telefonoEmisor: 'bypass a',
  telefonoBeneficiario: 'Planificador m',
  monto: 'Arroyo',
  bancoEmisor: 'Rioj',
  concepto: 'Muelle',
  referencia: 'mobile',
  fechaHora: 'compelling',
};

export const sampleWithNewData: NewOperations = {
  cedulaBeneficiario: 'SMTP index optic',
  telefonoEmisor: 'recíproca Acer',
  telefonoBeneficiario: 'Negro Extremad',
  monto: 'Madrid',
  bancoEmisor: 'Vía ',
  concepto: 'efficient',
  referencia: 'IB',
  fechaHora: 'transmit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
