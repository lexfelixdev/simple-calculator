import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CalculationService } from './calculation.service';

describe('CalculationService', () => {
  let service: CalculationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
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

  it('getCalculationResult should set Subject ', () => {
    const mockCalculation = [
      {id: 1, calculationString: "1+1", result: 2.0}
    ]

    service.getCalculationResult("/calculations/1");

    const req = httpMock.expectOne('http://localhost:8080/calculations/1');
    expect(req.request.method).toBe("GET");

    req.flush(mockCalculation);
  })
});
