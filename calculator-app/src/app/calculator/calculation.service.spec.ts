import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { CalculationService } from './calculation.service';
import { Calculation } from './calculation.model';
import { environment } from './../../environments/environment';

describe('CalculationService', () => {
  let service: CalculationService;
  let httpMock: HttpTestingController;
  const ADD_ENDPOINT_PATH = '/calculations';

  const dummyResponse = [
    new Calculation(1, 1, 2, 'ADD', 3),
    new Calculation(2, 4, 5, 'SUBTRACT', -1),
    new Calculation(3, 0, 1, 'DIVIDE', 0),
  ];
  const calculations = [
    new Calculation(null, 1, 2, 'ADD', null),
    new Calculation(null, 4, 5, 'SUBTRACT', null),
    new Calculation(null, 0, 1, 'DIVIDE', null),
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(CalculationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('calculateList should call backend function', () => {
    service.calculateList(calculations, () => {});

    const req = httpMock.expectOne(environment.serverUrl + ADD_ENDPOINT_PATH);
    expect(req.request.method).toBe('POST');
    req.flush(dummyResponse);
  });

  it('calculateList should call set all new calculations in subject', () => {
    let responseCalculations: Calculation[] = [];

    service.getCalculationSubject().subscribe((calculation) => {
      responseCalculations.push(calculation);
    });

    service.calculateList(calculations, () => {});

    const req = httpMock.expectOne(environment.serverUrl + ADD_ENDPOINT_PATH);
    req.flush(dummyResponse);

    expect(responseCalculations.length).toBe(3);
    responseCalculations.forEach((calculation, index) =>{
        const exceptedCalculation = calculations[index];
        expect(calculation.a).toBe(exceptedCalculation.a);
        expect(calculation.b).toBe(exceptedCalculation.b);
        expect(calculation.operator).toBe(exceptedCalculation.operator);
        expect(calculation.result).not.toBeNull();
    });
  });

  it('calculateList should display alert on error', ()=>{
    spyOn(window, 'alert');
    const dummyError = {status: 400, statusText: 'Bad Request'};
    let resetIsCalled = false;
    service.calculateList(calculations, () => {resetIsCalled = true;});

    const req = httpMock.expectOne(environment.serverUrl + ADD_ENDPOINT_PATH);
    req.flush('test error message', dummyError);

    expect(resetIsCalled).not.toBeTrue();
    expect(window.alert).toHaveBeenCalledWith('test error message');
  });

  it('calculateList should call reset function', ()=>{
    let resetIsCalled = false;
    service.calculateList(calculations, () => {resetIsCalled = true;});

    const req = httpMock.expectOne(environment.serverUrl + ADD_ENDPOINT_PATH);
    req.flush(dummyResponse);
    expect(resetIsCalled).toBeTrue();
  });

  it('getAllCalculationResults should call the backend', () =>{
    service.getAllCalculationResults();

    const req = httpMock.expectOne(environment.serverUrl + ADD_ENDPOINT_PATH);
    expect(req.request.method).toBe('GET');
    req.flush(dummyResponse);
  });

  it('getAllCalculationResults should set all calculations in subject', () =>{
    let responseCalculations: Calculation[] = [];

    service.getCalculationSubject().subscribe((calculation) => {
      responseCalculations.push(calculation);
    });

    service.getAllCalculationResults();

    const req = httpMock.expectOne(environment.serverUrl + ADD_ENDPOINT_PATH);
    req.flush(dummyResponse);

    expect(responseCalculations.length).toBe(3);
    responseCalculations.forEach((calculation, index) =>{
        const exceptedCalculation = calculations[index];
        expect(calculation.a).toBe(exceptedCalculation.a);
        expect(calculation.b).toBe(exceptedCalculation.b);
        expect(calculation.operator).toBe(exceptedCalculation.operator);
        expect(calculation.result).not.toBeNull();
    });
  });

});
