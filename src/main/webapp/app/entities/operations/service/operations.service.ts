import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperations, NewOperations } from '../operations.model';

export type PartialUpdateOperations = Partial<IOperations> & Pick<IOperations, 'id'>;

export type EntityResponseType = HttpResponse<IOperations>;
export type EntityArrayResponseType = HttpResponse<IOperations[]>;

@Injectable({ providedIn: 'root' })
export class OperationsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operations: NewOperations): Observable<EntityResponseType> {
    return this.http.post<IOperations>(this.resourceUrl, operations, { observe: 'response' });
  }

  update(operations: IOperations): Observable<EntityResponseType> {
    return this.http.put<IOperations>(`${this.resourceUrl}/${this.getOperationsIdentifier(operations)}`, operations, {
      observe: 'response',
    });
  }

  partialUpdate(operations: PartialUpdateOperations): Observable<EntityResponseType> {
    return this.http.patch<IOperations>(`${this.resourceUrl}/${this.getOperationsIdentifier(operations)}`, operations, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperations>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperations[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperationsIdentifier(operations: Pick<IOperations, 'id'>): number {
    return operations.id;
  }

  compareOperations(o1: Pick<IOperations, 'id'> | null, o2: Pick<IOperations, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperationsIdentifier(o1) === this.getOperationsIdentifier(o2) : o1 === o2;
  }

  addOperationsToCollectionIfMissing<Type extends Pick<IOperations, 'id'>>(
    operationsCollection: Type[],
    ...operationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operations: Type[] = operationsToCheck.filter(isPresent);
    if (operations.length > 0) {
      const operationsCollectionIdentifiers = operationsCollection.map(operationsItem => this.getOperationsIdentifier(operationsItem)!);
      const operationsToAdd = operations.filter(operationsItem => {
        const operationsIdentifier = this.getOperationsIdentifier(operationsItem);
        if (operationsCollectionIdentifiers.includes(operationsIdentifier)) {
          return false;
        }
        operationsCollectionIdentifiers.push(operationsIdentifier);
        return true;
      });
      return [...operationsToAdd, ...operationsCollection];
    }
    return operationsCollection;
  }
}
