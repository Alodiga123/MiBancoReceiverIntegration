import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperationsFormService } from './operations-form.service';
import { OperationsService } from '../service/operations.service';
import { IOperations } from '../operations.model';

import { OperationsUpdateComponent } from './operations-update.component';

describe('Operations Management Update Component', () => {
  let comp: OperationsUpdateComponent;
  let fixture: ComponentFixture<OperationsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operationsFormService: OperationsFormService;
  let operationsService: OperationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperationsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OperationsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operationsFormService = TestBed.inject(OperationsFormService);
    operationsService = TestBed.inject(OperationsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const operations: IOperations = { id: 456 };

      activatedRoute.data = of({ operations });
      comp.ngOnInit();

      expect(comp.operations).toEqual(operations);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperations>>();
      const operations = { id: 123 };
      jest.spyOn(operationsFormService, 'getOperations').mockReturnValue(operations);
      jest.spyOn(operationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operations }));
      saveSubject.complete();

      // THEN
      expect(operationsFormService.getOperations).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(operationsService.update).toHaveBeenCalledWith(expect.objectContaining(operations));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperations>>();
      const operations = { id: 123 };
      jest.spyOn(operationsFormService, 'getOperations').mockReturnValue({ id: null });
      jest.spyOn(operationsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operations: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operations }));
      saveSubject.complete();

      // THEN
      expect(operationsFormService.getOperations).toHaveBeenCalled();
      expect(operationsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperations>>();
      const operations = { id: 123 };
      jest.spyOn(operationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operationsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
