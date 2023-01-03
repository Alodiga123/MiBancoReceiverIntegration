import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOperations } from '../operations.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../operations.test-samples';

import { OperationsService } from './operations.service';

const requireRestSample: IOperations = {
  ...sampleWithRequiredData,
};

describe('Operations Service', () => {
  let service: OperationsService;
  let httpMock: HttpTestingController;
  let expectedResult: IOperations | IOperations[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperationsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Operations', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const operations = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(operations).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Operations', () => {
      const operations = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(operations).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Operations', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Operations', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Operations', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOperationsToCollectionIfMissing', () => {
      it('should add a Operations to an empty array', () => {
        const operations: IOperations = sampleWithRequiredData;
        expectedResult = service.addOperationsToCollectionIfMissing([], operations);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operations);
      });

      it('should not add a Operations to an array that contains it', () => {
        const operations: IOperations = sampleWithRequiredData;
        const operationsCollection: IOperations[] = [
          {
            ...operations,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOperationsToCollectionIfMissing(operationsCollection, operations);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Operations to an array that doesn't contain it", () => {
        const operations: IOperations = sampleWithRequiredData;
        const operationsCollection: IOperations[] = [sampleWithPartialData];
        expectedResult = service.addOperationsToCollectionIfMissing(operationsCollection, operations);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operations);
      });

      it('should add only unique Operations to an array', () => {
        const operationsArray: IOperations[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const operationsCollection: IOperations[] = [sampleWithRequiredData];
        expectedResult = service.addOperationsToCollectionIfMissing(operationsCollection, ...operationsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operations: IOperations = sampleWithRequiredData;
        const operations2: IOperations = sampleWithPartialData;
        expectedResult = service.addOperationsToCollectionIfMissing([], operations, operations2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operations);
        expect(expectedResult).toContain(operations2);
      });

      it('should accept null and undefined values', () => {
        const operations: IOperations = sampleWithRequiredData;
        expectedResult = service.addOperationsToCollectionIfMissing([], null, operations, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operations);
      });

      it('should return initial array if no Operations is added', () => {
        const operationsCollection: IOperations[] = [sampleWithRequiredData];
        expectedResult = service.addOperationsToCollectionIfMissing(operationsCollection, undefined, null);
        expect(expectedResult).toEqual(operationsCollection);
      });
    });

    describe('compareOperations', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOperations(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOperations(entity1, entity2);
        const compareResult2 = service.compareOperations(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOperations(entity1, entity2);
        const compareResult2 = service.compareOperations(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOperations(entity1, entity2);
        const compareResult2 = service.compareOperations(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
