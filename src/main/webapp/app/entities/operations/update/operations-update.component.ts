import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OperationsFormService, OperationsFormGroup } from './operations-form.service';
import { IOperations } from '../operations.model';
import { OperationsService } from '../service/operations.service';

@Component({
  selector: 'jhi-operations-update',
  templateUrl: './operations-update.component.html',
})
export class OperationsUpdateComponent implements OnInit {
  isSaving = false;
  operations: IOperations | null = null;

  editForm: OperationsFormGroup = this.operationsFormService.createOperationsFormGroup();

  constructor(
    protected operationsService: OperationsService,
    protected operationsFormService: OperationsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operations }) => {
      this.operations = operations;
      if (operations) {
        this.updateForm(operations);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operations = this.operationsFormService.getOperations(this.editForm);
    if (operations.id !== null) {
      this.subscribeToSaveResponse(this.operationsService.update(operations));
    } else {
      this.subscribeToSaveResponse(this.operationsService.create(operations));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperations>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(operations: IOperations): void {
    this.operations = operations;
    this.operationsFormService.resetForm(this.editForm, operations);
  }
}
