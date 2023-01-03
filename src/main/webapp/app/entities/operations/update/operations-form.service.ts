import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOperations, NewOperations } from '../operations.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperations for edit and NewOperationsFormGroupInput for create.
 */
type OperationsFormGroupInput = IOperations | PartialWithRequiredKeyOf<NewOperations>;

type OperationsFormDefaults = Pick<NewOperations, 'id'>;

type OperationsFormGroupContent = {
  id: FormControl<IOperations['id'] | NewOperations['id']>;
  cedulaBeneficiario: FormControl<IOperations['cedulaBeneficiario']>;
  telefonoEmisor: FormControl<IOperations['telefonoEmisor']>;
  telefonoBeneficiario: FormControl<IOperations['telefonoBeneficiario']>;
  monto: FormControl<IOperations['monto']>;
  bancoEmisor: FormControl<IOperations['bancoEmisor']>;
  concepto: FormControl<IOperations['concepto']>;
  referencia: FormControl<IOperations['referencia']>;
  fechaHora: FormControl<IOperations['fechaHora']>;
};

export type OperationsFormGroup = FormGroup<OperationsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperationsFormService {
  createOperationsFormGroup(operations: OperationsFormGroupInput = { id: null }): OperationsFormGroup {
    const operationsRawValue = {
      ...this.getFormDefaults(),
      ...operations,
    };
    return new FormGroup<OperationsFormGroupContent>({
      id: new FormControl(
        { value: operationsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cedulaBeneficiario: new FormControl(operationsRawValue.cedulaBeneficiario, {
        validators: [Validators.required, Validators.maxLength(16)],
      }),
      telefonoEmisor: new FormControl(operationsRawValue.telefonoEmisor, {
        validators: [Validators.required, Validators.maxLength(14)],
      }),
      telefonoBeneficiario: new FormControl(operationsRawValue.telefonoBeneficiario, {
        validators: [Validators.required, Validators.maxLength(14)],
      }),
      monto: new FormControl(operationsRawValue.monto, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      bancoEmisor: new FormControl(operationsRawValue.bancoEmisor, {
        validators: [Validators.required, Validators.maxLength(4)],
      }),
      concepto: new FormControl(operationsRawValue.concepto, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      referencia: new FormControl(operationsRawValue.referencia, {
        validators: [Validators.required, Validators.maxLength(15)],
      }),
      fechaHora: new FormControl(operationsRawValue.fechaHora, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
    });
  }

  getOperations(form: OperationsFormGroup): IOperations | NewOperations {
    return form.getRawValue() as IOperations | NewOperations;
  }

  resetForm(form: OperationsFormGroup, operations: OperationsFormGroupInput): void {
    const operationsRawValue = { ...this.getFormDefaults(), ...operations };
    form.reset(
      {
        ...operationsRawValue,
        id: { value: operationsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OperationsFormDefaults {
    return {
      id: null,
    };
  }
}
