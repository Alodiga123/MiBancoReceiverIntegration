import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperations } from '../operations.model';

@Component({
  selector: 'jhi-operations-detail',
  templateUrl: './operations-detail.component.html',
})
export class OperationsDetailComponent implements OnInit {
  operations: IOperations | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operations }) => {
      this.operations = operations;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
