import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperations } from '../operations.model';
import { OperationsService } from '../service/operations.service';

@Injectable({ providedIn: 'root' })
export class OperationsRoutingResolveService implements Resolve<IOperations | null> {
  constructor(protected service: OperationsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperations | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operations: HttpResponse<IOperations>) => {
          if (operations.body) {
            return of(operations.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
