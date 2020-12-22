import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SimpleCalculatorComponent } from './simple-calculator/simple-calculator.component';
import { SimpleCalculatorHistoryComponent } from './simple-calculator-history/simple-calculator-history.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [SimpleCalculatorComponent, SimpleCalculatorHistoryComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [SimpleCalculatorComponent]
})
export class CalculatorModule { }
