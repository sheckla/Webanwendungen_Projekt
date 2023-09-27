import { Injectable, OnInit } from '@angular/core';
import { ApiService } from '../api/api.service';
import {
  Case,
  Cpu,
  Fan,
  FormQuestion,
  Gpu,
  Mainboard,
  PSU,
  Pair,
  PartType,
  partList,
} from '../part-types';
import { Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class PartCreatorService implements OnInit {
  public list: any[] = [];
  public partTypesArr: PartType[] = partList;
  public partType?: PartType;

  constructor(public api: ApiService) { }

  ngOnInit() {
    console.log('PartCreatorService init');
    this.partType = 'CPU';
    this.list = this.api.getPartValuePairs(new Cpu());
  }

  uploadPart(part: any, partType: string) {
    // var part: any = {};
    // this.list.forEach((pair: Pair) => {
    //   part[pair.key] = pair.value;
    // });
    // console.log(part);
    // if (!this.partType) {
    //   return;
    // }
    this.api.postPart(part, partType);
  }

  changePartType(event: any) {
    var assignment: PartType = event.target.value;
    if (assignment) {
      this.partType = assignment;
      this.list = this.selectedType();
    }
  }

  resetSelection() { }

  selectedType(): Pair[] {
    switch (this.partType) {
      case 'CASE':
        return this.api.getPartValuePairs(new Case());
      case 'GPU':
        return this.api.getPartValuePairs(new Gpu());
      case 'FAN':
        return this.api.getPartValuePairs(new Fan());
      case 'MOTHERBOARD':
        return this.api.getPartValuePairs(new Mainboard());
      case 'PSU':
        return this.api.getPartValuePairs(new PSU());
      case 'CPU':
        return this.api.getPartValuePairs(new Cpu());
      default:
        break;
    }
    return [] as Pair[];
  }

  /*************************************************************
   *  Get Form Questions according to Type
   * - must be matched with API!
   *************************************************************/
  getPartFormQuestions(): FormQuestion<any>[] {
    let questions: FormQuestion<any>[] = [];

    /*****************************
     *  Standard Part Info
     *****************************/
    // Partname - standard for all parts
    questions.push(
      new FormQuestion<any>({
        value: '',
        key: 'partName',
        label: 'Name of part',
        placeholder: 'Ryzen 9 7950X',
        controlType: 'textbox',
        validators: [Validators.required],
      })
    );
    // Manufacturer Name, standard for all parts
    questions.push(
      new FormQuestion<any>({
        value: '',
        key: 'manufacturerName',
        label: 'Name of manufacturer',
        placeholder: 'AMD',
        controlType: 'textbox',
        validators: [Validators.required],
      })
    );

    // Add fields according to specific part types
    switch (this.partType) {
      /*****************************
       *  CPU
       *****************************/
      case 'CPU':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'cpuCores',
            label: 'CPU Core Count',
            placeholder: '16',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(1)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'cpuThreads',
            label: 'CPU Thread Count',
            placeholder: '32',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(1)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'maxTurboFrequencyInGHz',
            label: 'Turbo Frequency (GHz)',
            placeholder: '5.8',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'defaultFrequencyInGHz',
            label: 'Default Frequency (GHz)',
            placeholder: '5.5',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'TDPinWattage',
            label: 'TDP (Watt)',
            placeholder: '125',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'chipGeneration',
            label: 'Generation',
            placeholder: '4',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'supportedRAMType',
            label: 'Supported RAM',
            placeholder: 'DDR5',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'socket',
            label: 'Socket Type',
            placeholder: 'AM5',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        break;

      /*****************************
       *  GPU
       *****************************/
      case 'GPU':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'clockspeedInMHz',
            label: 'Maximum clockspeed',
            placeholder: '1770',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'gpuRAMType',
            label: 'GPU Ram Type',
            placeholder: 'GDDR6X',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'gpuRAMinGB',
            label: 'Amount of integrated RAM (in GB)',
            placeholder: '12.0',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'wattage',
            label: 'Power Draw (Watt)',
            placeholder: '330',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'chipsetManufacturer',
            label: 'Chipset Manufacturer',
            placeholder: 'Nvidia',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'gpuInterface',
            label: 'GPU Connection Interface',
            placeholder: 'PCI-E 4.0 x16',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'amountCUDACores',
            label: 'Amount of CUDA Cores',
            placeholder: '6144',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        break;
      case 'MOTHERBOARD':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'sizeFactor',
            label: 'Size Factor',
            placeholder: 'ATX',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'cpuManufacturer',
            label: 'CPU Manufacturer',
            placeholder: 'Intel',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'cpuSocket',
            label: 'Socket Type',
            placeholder: 'LGA1700',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'supportedRAMType',
            label: 'Supported RAM Type(s)',
            placeholder: 'DDR4, DDR5',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'chipGeneration',
            label: 'Generation of the CPU',
            placeholder: '10th Generation',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'storageInGB',
            label: 'Internal storage-size (GB)',
            placeholder: '128',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: 'false',
            key: 'hasIntegratedWLAN',
            label: 'Wireless LAN Module',
            placeholder: 'false',
            controlType: 'checkbox',
            validators: [Validators.required],
          })
        );
        break;
      /*****************************
       * PSU
       *****************************/
      case 'PSU':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'powerInWattage',
            label: 'Maximum Power Output (Watt)',
            placeholder: '800',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'efficiencyInPercent',
            label: 'Output Efficiency in percent (%)',
            placeholder: '85',
            controlType: 'textbox',
            type: 'number',
            validators: [
              Validators.required,
              Validators.min(0.0),
              Validators.max(100.0),
            ],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'certificate',
            label: 'Certificate for energy usage',
            placeholder: '80 Plus Gold',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'connections',
            label: 'Types of supported Cables',
            placeholder:
              '1x ATX 20/24pol, 1x ATX12V 4+4pol, 2x PCI Express 6+2pol, 5x SATA, 1x IDE',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'sizeFactor',
            label: 'Size Factor',
            placeholder: 'ATX',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'isModular',
            label: 'Modular',
            placeholder: 'false',
            controlType: 'checkbox',
            validators: [Validators.required],
          })
        );
        break;
      /*****************************
       *  CASE
       *****************************/
      case 'CASE':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'internalFanAmount',
            label: 'Internal Fan Amount',
            placeholder: '3',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'frameColor',
            label: 'Case Color',
            placeholder: 'black',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'sizeFactor',
            label: 'Size Factor',
            placeholder: 'ATX',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'towerType',
            label: 'MIDI-Tower, Big Tower',
            placeholder: 'Big Tower',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'hasRGB',
            label: 'RGB Lighting',
            placeholder: 'false',
            controlType: 'checkbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'hasSeeThroughSidepanel',
            label: 'Glass/See-through Side Panel',
            placeholder: '',
            controlType: 'checkbox',
            validators: [Validators.required],
          })
        );
        break;
      /*****************************
       *  FAN
       *****************************/
      case 'FAN':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'supportedSockets',
            label: 'Supported Socket Types',
            placeholder: 'LGA1700, AM4, AM5',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'fanType',
            label: 'Type of Cooler',
            placeholder: 'AIO / Tower Cooler',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'connections',
            label: 'Cable Connection',
            placeholder: '3-pin, 4-pin PWM',
            controlType: 'textbox',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'fanSpeedInUmin',
            label: 'Fan RPM',
            placeholder: '1500',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'volumeIndB',
            label: 'Sound Volume (dB)',
            placeholder: '29.3',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'maxCoolingInWattage',
            label: 'Cooling Capacity (Watt)',
            placeholder: '250',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        break;
      case 'RAM':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'amountOfSticks',
            label: 'Amount of RAM-Modules (1-8)',
            placeholder: '2',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'sizePerStickInGB',
            label: 'Capacity per stick (GB)',
            placeholder: '16',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'ramType',
            label: 'Ram Type',
            placeholder: 'DDR5',
            controlType: 'textbox',
            type: 'text',
            validators: [Validators.required],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'totalSizeInGB',
            label: 'Total Size (GB)',
            placeholder: '32',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'ramSpeedInMHz',
            label: 'Speed of Module (MHz)',
            placeholder: '4800',
            controlType: 'textbox',
            type: 'number',
            validators: [Validators.required, Validators.min(0.0)],
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'hasRGB',
            label: 'RGB Lighting',
            placeholder: 'false',
            controlType: 'checkbox',
            validators: [Validators.required],
          })
        );
        break;
      default:
    }
    questions.forEach((question: FormQuestion<any>) => {
      question.value = question.placeholder;
    });
    return questions;
  }
}
