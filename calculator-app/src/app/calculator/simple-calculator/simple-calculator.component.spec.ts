import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { SimpleCalculatorComponent } from './simple-calculator.component';
import { By } from '@angular/platform-browser';
import { CalculationService } from '../calculation.service';

describe('SimpleCalculatorComponent', () => {
  let calculationServiceSpy: jasmine.SpyObj<CalculationService>;
  let component: SimpleCalculatorComponent;
  let fixture: ComponentFixture<SimpleCalculatorComponent>;

  beforeEach(async () => {
    const useValueCalculationServiceSpy = jasmine.createSpyObj('CalculationService', ['calculateList']);

    await TestBed.configureTestingModule({
      imports: [],
      providers: [FormBuilder, {provide: CalculationService, useValue: useValueCalculationServiceSpy}],
      declarations: [SimpleCalculatorComponent],
    }).compileComponents();

    calculationServiceSpy = TestBed.inject(CalculationService) as jasmine.SpyObj<CalculationService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SimpleCalculatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('on init should set initial form', () => {
    expect(component.calculatorForm.controls.length).toBe(1);
    expect(component.calculatorForm.controls[0].value).toEqual({
      leftHand: '',
      operator: '',
      rightHand: '',
    });
    expect(component.calculatorForm.controls[0].valid).toBeFalse();
  });

  it('entering text in leftHand field should error', () => {
    const leftHandFormControl: FormControl = (component.calculatorForm
      .controls[0] as FormGroup).controls['leftHand'] as FormControl;
    expect(leftHandFormControl.errors['required']).toBeTruthy();
    leftHandFormControl.setValue('asdb');
    expect(leftHandFormControl.errors['pattern']).toBeTruthy();
  });

  it('entering text in rightHand field should error', () => {
    const rightHandFormControl: FormControl = (component.calculatorForm
      .controls[0] as FormGroup).controls['rightHand'] as FormControl;
    expect(rightHandFormControl.errors['required']).toBeTruthy();
    rightHandFormControl.setValue('asdb');
    expect(rightHandFormControl.errors['pattern']).toBeTruthy();
  });

  it('entering nothing in operator field should error', () => {
    const operatorFormControl: FormControl = (component.calculatorForm
      .controls[0] as FormGroup).controls['operator'] as FormControl;
    expect(operatorFormControl.errors['required']).toBeTruthy();
  });

  it('add button click should set a new formgroup', () => {
    const addNewCalculationBtn = fixture.debugElement
      .query(By.css('#addNewCalculationBtn'))
      .nativeElement;
      addNewCalculationBtn.click()

    expect(component.calculatorForm.controls.length).toBe(2);
    expect(component.calculatorForm.controls[1].value).toEqual({
      leftHand: '',
      operator: '',
      rightHand: '',
    });
    expect(component.calculatorForm.controls[1].valid).toBeFalse();

    fixture.detectChanges();

    const calculationFieldDebugElements = fixture.debugElement.queryAll(
      By.css('.calculationField')
    );
    console.log(calculationFieldDebugElements);
    expect(calculationFieldDebugElements.length).toBe(2);
  });

  it('submit button click should send all calculations in form group and reset', () => {
    calculationServiceSpy.calculateList.and.callFake((calculateList, resetFnc) => {
      resetFnc();
    });
    component.calculatorForm.push(
      new FormGroup({
        leftHand: new FormControl('', [Validators.required, Validators.pattern('^[0-9]*$')]),
        operator: new FormControl('', Validators.required),
        rightHand: new FormControl('', [Validators.required, Validators.pattern('^[0-9]*$')]),
      })
    );

    component.calculatorForm.setValue([
      {
        leftHand: '1',
        operator: 'ADD',
        rightHand: '3',
      },
      {
        leftHand: '2',
        operator: 'MULTIPLY',
        rightHand: '6',
      }
    ]);
    fixture.detectChanges();

    const submitBtn = fixture.debugElement.query(
      By.css('#submitCalculatorFormBtn')
    ).nativeElement;
    submitBtn.click();

    fixture.detectChanges();

    expect(calculationServiceSpy.calculateList).toHaveBeenCalled();

    expect(component.calculatorForm.controls.length).toBe(1);
    expect(component.calculatorForm.controls[0].value).toEqual({
      leftHand: '',
      operator: '',
      rightHand: '',
    });
    expect(component.calculatorForm.controls[0].valid).toBeFalse();
  });

  it('submit button should be disabled if form is invalid', () =>{
    component.calculatorForm.setValue([
      {
        leftHand: '',
        operator: 'ADD',
        rightHand: '3',
      },
    ]);
    fixture.detectChanges();

    const submitBtn = fixture.debugElement.query(
      By.css('#submitCalculatorFormBtn')
    ).nativeElement;
    submitBtn.click();

    fixture.detectChanges();

    expect(calculationServiceSpy.calculateList).not.toHaveBeenCalled();
  });
  it('sendCalculation should not call service if form is invalid', () =>{
    component.calculatorForm.setValue([
      {
        leftHand: '',
        operator: 'ADD',
        rightHand: '3',
      },
    ]);

    component.sendCalculation();

    expect(calculationServiceSpy.calculateList).not.toHaveBeenCalled();
  });
});
