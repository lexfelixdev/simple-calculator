export class Calculation {
  id:number;
  a: number;
  b: number;
  operator: string;
  result: number;

  constructor(id:number, a:number,b:number, operator: string, result: number) {
    this.id = id;
    this.a = a;
    this.b = b;
    this.operator = operator;
    this.result = result;
  }
}
