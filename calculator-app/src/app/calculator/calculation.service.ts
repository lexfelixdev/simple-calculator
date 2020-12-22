import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Calculation } from './calculation.model';
import { Observable, throwError, ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CalculationService {
  newCalculationSubject: ReplaySubject<Calculation> = new ReplaySubject<Calculation>(1);;

  serverEndpoint = 'http://localhost:8080';
  calculationEndpoint = this.serverEndpoint + '/calculations';

  constructor(private http: HttpClient) {}

  calculate(calculationString: string): Observable<HttpResponse<Object>> {
    const calculation: Calculation = new Calculation(null, calculationString, null);

    return this.http.post(this.calculationEndpoint, calculation, {
      observe: 'response',
    });
  }

  getCalculationResult(location: string) {
    this.http.get<Calculation>(this.serverEndpoint + location).subscribe(
      (calculation) => {
        this.newCalculationSubject.next(calculation);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  getAllCalculationResults(): Observable<Calculation[]> {
    return this.http.get<Calculation[]>(this.calculationEndpoint);
  }
}
