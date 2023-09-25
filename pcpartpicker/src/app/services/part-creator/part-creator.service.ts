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
} from '../part-types';

type PartType = 'CASE' | 'CPU' | 'GPU' | 'FAN' | 'MOTHERBOARD' | 'PSU';

@Injectable({
  providedIn: 'root',
})
export class PartCreatorService implements OnInit {
  public partTypesArr = ['CASE', 'CPU', 'GPU', 'FAN', 'MOTHERBOARD', 'PSU'];
  public partType?: PartType;
  public list: any[] = [];

  constructor(public api: ApiService) {}

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

  resetSelection() {}

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

  getPartFormQuestions(): FormQuestion<any>[] {
    let questions: FormQuestion<any>[] = [];

    // Partname - standard for all parts
    questions.push(
      new FormQuestion<any>({
        value: '',
        key: 'partName',
        label: 'Name of part',
        placeholder: 'Super Mega 3000',
        controlType: 'textbox',
      })
    );
    // Manufacturer Name, standard for all parts
    questions.push(
      new FormQuestion<any>({
        value: '',
        key: 'manufacturerName',
        label: 'Name of manufacturer',
        placeholder: 'Umbrella Corporation',
        controlType: 'textbox',
      })
    );

    // Add fields according to specific part types
    switch (this.partType) {
      case 'CPU':
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'cpuCores',
            label: 'CPU Core Count',
            placeholder: '16',
            controlType: 'textbox',
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'cpuThreads',
            label: 'CPU Thread Count',
            placeholder: '32',
            controlType: 'textbox',
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'maxTurboFrequencyInGHz',
            label: 'Turbo Frequency (GHz)',
            placeholder: '5.8',
            controlType: 'textbox',
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'defaultFrequencyInGHz',
            label: 'Default Frequency (GHz)',
            placeholder: '5.5',
            controlType: 'textbox',
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'TDPinWattage',
            label: 'TDP (Watt)',
            placeholder: '125',
            controlType: 'textbox',
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'chipGeneration',
            label: 'Generation',
            placeholder: '4',
            controlType: 'textbox',
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'supportedRAMType',
            label: 'Supported RAM',
            placeholder: 'DDR5',
            controlType: 'textbox',
          })
        );
        questions.push(
          new FormQuestion<any>({
            value: '',
            key: 'socket',
            label: 'Socket Type',
            placeholder: 'AM5',
            controlType: 'textbox',
          })
        );
        break;
      case 'GPU':
      case 'MOTHERBOARD':
      case 'PSU':
      case 'CASE':
      case 'FAN':
      default:
    }
    questions.forEach((question: FormQuestion<any>) => {
      question.value = question.placeholder;
    })
    return questions;
  }
}
