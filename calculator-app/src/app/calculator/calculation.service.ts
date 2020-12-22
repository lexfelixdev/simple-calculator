import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Calculation } from './calculation.model';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CalculationService {
  private calculationSubject = new Subject<Calculation>();

  serverEndpoint = 'http://localhost:8080';
  calculationEndpoint = this.serverEndpoint + '/calculations';

  constructor(private http: HttpClient) {}

  calculateList(calculationList: Calculation[], resetFnc){
    this.http.post<Calculation[]>(this.calculationEndpoint, calculationList)
    .subscribe(
      calculatedList =>{
        calculatedList.forEach(calculation => {
          this.calculationSubject.next(calculation);
        })
        resetFnc();
    })
  }

  getAllCalculationResults(){
    this.http.get<Calculation[]>(this.calculationEndpoint)
    .subscribe(
      calculatedList =>{
        calculatedList.forEach(calculation => {
          this.calculationSubject.next(calculation);
        })
    });
  }

  getCalculationSubject(): Subject<Calculation>{
    return this.calculationSubject;
  }
}
