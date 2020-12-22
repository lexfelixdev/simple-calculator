import { Component, OnInit } from '@angular/core';
import { CalculationService } from '../calculation.service';
import { Calculation } from '../calculation.model';

@Component({
  selector: 'app-simple-calculator-history',
  templateUrl: './simple-calculator-history.component.html',
  styleUrls: ['./simple-calculator-history.component.scss']
})
export class SimpleCalculatorHistoryComponent implements OnInit {
  calculations : Calculation[] = [];

  constructor(private calculationService: CalculationService) { }

  ngOnInit(): void {
    this.subscribeToNewCalculations();
    this.calculationService.getAllCalculationResults().subscribe(
      (calculations : Calculation[]) => {
        this.calculations.push(...calculations);
      },
      error => {
        console.log(error);
      }
    );
  }

  subscribeToNewCalculations(){
    this.calculationService.newCalculationSubject.subscribe(newCalculation =>{
      this.calculations.push(newCalculation);
    })
  }

}
