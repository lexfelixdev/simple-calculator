import { Component, OnInit } from '@angular/core';
import { CalculationService} from '../calculation.service';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-simple-calculator',
  templateUrl: './simple-calculator.component.html',
  styleUrls: ['./simple-calculator.component.scss']
})
export class SimpleCalculatorComponent implements OnInit {
  calculatorForm: any;

  constructor(private calculationService: CalculationService,
    private formBuilder: FormBuilder) {
      this.calculatorForm = this.formBuilder.group({
        calculation: ['', [Validators.pattern('(?:((?:[-1-9][0-9]*)|0)([-+*\/])((?:[-1-9][0-9]*)|0))(?:([-+*\/])((?:[-1-9][0-9]*)|0))*'),Validators.required]]
      })
     }

  ngOnInit(): void {
  }

  sendCalculation(){
    this.calculationService.calculate(this.calculatorForm.value.calculation)
    .subscribe(
      response => {
        this.calculationService.getCalculationResult(response.headers.get("Location"));
        this.calculatorForm.reset();
    },
    error => {console.log(error)}
    );
  }


}
