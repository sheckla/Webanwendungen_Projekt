import { ValidatorFn, Validators } from "@angular/forms";

export class FormQuestion<T> {
  value: T | undefined;
  key: string;
  label: string;
  controlType: string | 'textbox' | 'checkbox';
  type: string | 'text' | 'number';
  placeholder: string;
  validators: ValidatorFn[];

  constructor(options: {
    value?: T;
    key?: string;
    label?: string;
    controlType?: string;
    type?: string;
    placeholder?: string;
    validators?: ValidatorFn[]
  }) {
    this.value = options.value;
    this.key = options.key || '';
    this.label = options.label || '';
    this.controlType = options.controlType || 'textbox';
    this.type = options.type || 'text'
    this.placeholder = options.placeholder || '';
    this.validators = options.validators || [];
  }
}

export class Cpu {
  partName: string = '';
  manufacturerName: string = '';
  cpuCores: number = 0;
  cpuThreads: number = 0;
  maxTurboFrequencyInGHz: number = 0;
  defaultFrequencyInGHz: number = 0;
  TDPinWattage: number = 0;
  chipGeneration: number = 0;
  supportedRAMType: string = '';
  socket: string = '';
}

export class Gpu {
  partName: string = '';
  manufacturerName: string = '';
  clockspeedInMHz: number = 0;
  gpuRAMtype: string = '';
  gpuRAMinGB: number = 0;
  wattage: number = 0;
  chipsetManufacturer: string = '';
  gpuInterface: string = '';
  amountCUDACores: number = 0;
}

export class Mainboard {
  partName: string = '';
  manufacturerName: string = '';
  sizeFactor: string = '';
  cpuManufacturer: string = '';
  cpuSocket: string = '';
  supportedRAMType: string = '';
  chipGeneration: string = '';
  storageI: string = '';
  hasIntegratedW: string = '';
}

export class PSU {
  partName: string = '';
  manufacturerName: string = '';
  powerInWattage: string = '';
  efficiencyInPercent: string = '';
  certificate: string = '';
  connections: string = '';
  sizeFactor: string = '';
  isModular: string = '';
}

export class Case {
  partName: string = '';
  manufacturerName: string = 'Nvidia';
  internalFanAmount: number = 3;
  frameColor: string = '';
  sizeFactor: string = '';
  towerType: string = '';
  hasRGB: boolean = false;
  hasSeeThroughSidepanel: boolean = false;
}

export class Fan {
  partName: string = '';
  manufacturerName: string = '';
  supportedSockets: string = '';
  fanType: string = '';
  connections: string = '';
  fanSpeedInUmin: number = 0;
  volumeIndB: number = 0;
  maxCoolingInWattage: number = 0;
}

export interface Pair {
  key?: any;
  value?: any;
}

// TODO RAM
export type PartType = 'CASE' | 'CPU' | 'GPU' | 'FAN' | 'MOTHERBOARD' | 'PSU' | 'RAM';
export const partList: PartType[] = ['CASE', 'CPU', 'GPU', 'FAN', 'MOTHERBOARD', 'PSU', 'RAM'];
