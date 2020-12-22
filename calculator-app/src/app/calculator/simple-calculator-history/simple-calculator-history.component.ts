import { Component, OnInit } from '@angular/core';
import { CalculationService } from '../calculation.service';
import { Calculation } from '../calculation.model';
import { Subject } from 'rxjs';
import { VALID_OPERATORS} from '../calculation.constants';

@Component({
  selector: 'app-simple-calculator-history',
  templateUrl: './simple-calculator-history.component.html',
  styleUrls: ['./simple-calculator-history.component.scss']
})
export class SimpleCalculatorHistoryComponent implements OnInit {
  validOperators = VALID_OPERATORS;
  calculations : Calculation[] = [];

  constructor(private calculationService: CalculationService) { }

  ngOnInit(): void {
    this.subscribeToCalculations();
    this.calculationService.getAllCalculationResults()
  }

  subscribeToCalculations(){
    this.calculationService.getCalculationSubject().subscribe(calculation =>{
      console.log(calculation);
      calculation.operator = VALID_OPERATORS.find(validOperator => {return validOperator.value == calculation.operator}).display;
      this.calculations.push(calculation);
    })
  }

}
