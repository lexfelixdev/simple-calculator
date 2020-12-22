export class Calculation {
  id: number;
  calculationString: string;
  result: number;

  constructor(id:number, calculationString: string, result: number) {
    this.id = id;
    this.calculationString = calculationString;
    this.result = result;
  }
}
