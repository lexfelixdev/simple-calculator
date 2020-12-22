import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SimpleCalculatorHistoryComponent } from './simple-calculator-history.component';
import { CalculationService } from '../calculation.service';
import { Calculation } from '../calculation.model';
import { of, Subject } from 'rxjs';
import { VALID_OPERATORS } from '../calculation.constants';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

describe('SimpleCalculatorHistoryComponent', () => {
  let calculationServiceSpy: jasmine.SpyObj<CalculationService>;
  let component: SimpleCalculatorHistoryComponent;
  let fixture: ComponentFixture<SimpleCalculatorHistoryComponent>;
  let mockSubjectSpy = jasmine.createSpyObj(Subject, ['subscribe']);

  const mockCalculations =[
    new Calculation(1, 1, 2, 'ADD', 3),
    new Calculation(2, 4, 5, 'SUBTRACT', -1),
    new Calculation(3, 0, 1, 'DIVIDE', 0),
    new Calculation(3, 0, 1, 'MULTIPLY', 0)
  ];

  beforeEach(async () => {
    const useValueCalculationServiceSpy = jasmine.createSpyObj('CalculationService', ['getAllCalculationResults', 'getCalculationSubject']);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{provide: CalculationService, useValue: useValueCalculationServiceSpy}],
      declarations: [ SimpleCalculatorHistoryComponent ]
    })
    .compileComponents();
    calculationServiceSpy = TestBed.inject(CalculationService) as jasmine.SpyObj<CalculationService>;
  });

  beforeEach(() => {
    calculationServiceSpy.getCalculationSubject.and.returnValue(mockSubjectSpy);
    fixture = TestBed.createComponent(SimpleCalculatorHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('on init should subscribe to calculationSubject', () => {
    expect(calculationServiceSpy.getCalculationSubject).toHaveBeenCalled();
    expect(mockSubjectSpy.subscribe).toHaveBeenCalled();
  });

  it('should set and diplay all calculations correctly', () => {
    let mockSubject = new Subject<Calculation>();
    calculationServiceSpy.getCalculationSubject.and.returnValue(mockSubject);
    component.ngOnInit();

    mockCalculations.forEach(calculation => {
      mockSubject.next(calculation);
    })

    fixture.detectChanges();

    expect(component.calculations).not.toEqual([]);
    for( let calculation of component.calculations){
      const index = VALID_OPERATORS.findIndex(validOperator => { return validOperator.display === calculation.operator});
      expect(index).not.toBe(-1);
    };

    const previousCalculationElements : DebugElement[]= fixture.debugElement.queryAll(By.css('.previousCalc'));
    console.log(previousCalculationElements);
    expect(previousCalculationElements.length).toBe(mockCalculations.length);
    previousCalculationElements.forEach((previousCalculationElement, index) => {
      const calculation = mockCalculations[index];
      const calculationElement = previousCalculationElement.queryAll(By.css('span'));
      expect(calculationElement[0].nativeElement.innerHTML)
      .toBe(`${calculation.a} ${calculation.operator} ${calculation.b}`);
      expect(calculationElement[1].nativeElement.innerHTML)
      .toBe(`${calculation.result}`);
    });
  });
});
