import { Injectable, OnInit } from '@angular/core';
import { ApiService } from '../api/api.service';
import { Case, Cpu, Fan, Gpu, Mainboard, PSU, Pair } from '../part-types';

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

  uploadPart() {
    var part: any = {};
    this.list.forEach((pair: Pair) => {
      part[pair.key] = pair.value;
    });
    console.log(part);
    if (!this.partType) {
      return;
    }
    this.api.postPart(part, this.partType);
  }

  changePartType(event: any) {
    var assignment: PartType = event.target.value;
    if (assignment) {
      this.partType = assignment;
      this.list = this.selectedType();
      console.log(this.list);
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
}
