import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../operations.test-samples';

import { OperationsFormService } from './operations-form.service';

describe('Operations Form Service', () => {
  let service: OperationsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OperationsFormService);
  });

  describe('Service methods', () => {
    describe('createOperationsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOperationsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cedulaBeneficiario: expect.any(Object),
            telefonoEmisor: expect.any(Object),
            telefonoBeneficiario: expect.any(Object),
            monto: expect.any(Object),
            bancoEmisor: expect.any(Object),
            concepto: expect.any(Object),
            referencia: expect.any(Object),
            fechaHora: expect.any(Object),
          })
        );
      });

      it('passing IOperations should create a new form with FormGroup', () => {
        const formGroup = service.createOperationsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cedulaBeneficiario: expect.any(Object),
            telefonoEmisor: expect.any(Object),
            telefonoBeneficiario: expect.any(Object),
            monto: expect.any(Object),
            bancoEmisor: expect.any(Object),
            concepto: expect.any(Object),
            referencia: expect.any(Object),
            fechaHora: expect.any(Object),
          })
        );
      });
    });

    describe('getOperations', () => {
      it('should return NewOperations for default Operations initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOperationsFormGroup(sampleWithNewData);

        const operations = service.getOperations(formGroup) as any;

        expect(operations).toMatchObject(sampleWithNewData);
      });

      it('should return NewOperations for empty Operations initial value', () => {
        const formGroup = service.createOperationsFormGroup();

        const operations = service.getOperations(formGroup) as any;

        expect(operations).toMatchObject({});
      });

      it('should return IOperations', () => {
        const formGroup = service.createOperationsFormGroup(sampleWithRequiredData);

        const operations = service.getOperations(formGroup) as any;

        expect(operations).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOperations should not enable id FormControl', () => {
        const formGroup = service.createOperationsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOperations should disable id FormControl', () => {
        const formGroup = service.createOperationsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
