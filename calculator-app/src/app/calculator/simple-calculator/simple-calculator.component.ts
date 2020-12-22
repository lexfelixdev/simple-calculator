import { Component, OnInit } from '@angular/core';
import { CalculationService } from '../calculation.service';
import { Form, FormArray, FormBuilder, Validators } from '@angular/forms';
import { Calculation } from '../calculation.model';
import { VALID_OPERATORS } from '../calculation.constants';

@Component({
  selector: 'app-simple-calculator',
  templateUrl: './simple-calculator.component.html',
  styleUrls: ['./simple-calculator.component.scss'],
})
export class SimpleCalculatorComponent implements OnInit {
  calculatorForm: FormArray;
  validOperators = VALID_OPERATORS;

  constructor(
    private calculationService: CalculationService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.calculatorForm = this.getInitialForm();
  }

  addNewCalculationField() {
    console.log('Add new calculation field');
    this.calculatorForm.push(
      this.formBuilder.group({
        leftHand: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
        operator: ['', Validators.required],
        rightHand: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
      })
    );
  }

  getInitialForm(): FormArray {
    return this.formBuilder.array([
      this.formBuilder.group({
        leftHand: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
        operator: ['', Validators.required],
        rightHand: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
      }),
    ]);
  }

  sendCalculation() {
    if (this.calculatorForm.valid) {
      let calculationList = [];

      this.calculatorForm.controls.forEach((calculationInput) => {
        calculationList.push(
          new Calculation(
            null,
            calculationInput.get('leftHand').value,
            calculationInput.get('rightHand').value,
            calculationInput.get('operator').value,
            null
          )
        );
      });
      this.calculationService.calculateList(calculationList, () => {
        this.calculatorForm = this.getInitialForm();
      });
    }
  }
}
