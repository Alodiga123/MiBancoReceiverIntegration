import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OperationsDetailComponent } from './operations-detail.component';

describe('Operations Management Detail Component', () => {
  let comp: OperationsDetailComponent;
  let fixture: ComponentFixture<OperationsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperationsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ operations: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OperationsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OperationsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load operations on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.operations).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
