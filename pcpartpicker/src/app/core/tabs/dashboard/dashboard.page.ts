import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { ApiService } from 'src/app/services/api/api.service';
import { PartCreatorService } from 'src/app/services/part-creator/part-creator.service';
import { FilterSelectComponent } from '../../components/filter/filter-select/filter-select.component';
import { ImageUploadComponent } from '../../components/image-upload/image-upload.component';
import { UserService } from 'src/app/services/user/user.service';
import { PartItemComponent } from '../../components/part-item/part-item.component';
import { PartAddComponent } from '../../components/part-add/part-add.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'dashboard.page.html',
  styleUrls: ['dashboard.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    FormsModule,
    ReactiveFormsModule,
    FilterSelectComponent,
    NgSelectModule,
    ImageUploadComponent,
    PartItemComponent,
    PartAddComponent,
  ],
})
export class DashboardPage implements OnInit {
  public partFilter: FormGroup = new FormGroup({
    cpu: new FormControl(null, null),
    mainboard: new FormControl(null, null),
    gpu: new FormControl(null, null),
    psu: new FormControl(null, null),
    case: new FormControl(null, null),
    fan: new FormControl(null, null),
    ram: new FormControl(null, null),
    manufacturerName: new FormControl(null, null),
    partName: new FormControl(null, null),
  });

  public filteredParts: any[] = [];

  constructor(
    public api: ApiService,
    public partCreator: PartCreatorService,
    public user: UserService
  ) {}

  ngOnInit() {
    this.api.getAllParts()?.subscribe((data: any) => {
      this.filteredParts = data;
      this.filteredParts = this.api.allParts();
      this.submitFilter();
    });

    this.partFilter.valueChanges.subscribe((value: any) => {
      this.submitFilter();
    });
  }

  public resetFilter() {
    this.partFilter.reset();
  }

  public submitFilter() {
    let parts: any[] = [];
    let filter = this.partFilter.value;

    // Fill parts with selected types
    if (filter.cpu) {
      parts = parts.concat(this.api.cpus);
    }
    if (filter.mainboard) {
      parts = parts.concat(this.api.motherboards);
    }
    if (filter.psu) {
      parts = parts.concat(this.api.psus);
    }
    if (filter.gpu) {
      parts = parts.concat(this.api.gpus);
    }
    if (filter.case) {
      parts = parts.concat(this.api.cases);
    }
    if (filter.fan) {
      parts = parts.concat(this.api.fans);
    }
    if (filter.ram) {
      parts = parts.concat(this.api.rams);
    }

    // Filter for part name
    let partName: string = this.partFilter.get('partName')?.value;
    if (partName) {
      this.filterPartNameForKeyword(parts, partName);
    }

    // Filter for manufacturer name
    let manufacturerName: string =
      this.partFilter.get('manufacturerName')?.value;
    if (manufacturerName) {
      this.filterManufacturerNameForKeyword(parts, manufacturerName);
    }

    this.filteredParts = parts;
  }

  private filterPartNameForKeyword(parts: any[], keyword: string): any[] {
    parts.forEach((part: any, index: number) => {
      let partName_lowercase: string = (
        part.data.partName as string
      ).toLowerCase();
      let keyword_lowercase: string = keyword.toLowerCase();
      if (!partName_lowercase.includes(keyword_lowercase)) {
        parts.splice(index, 1);
      }
    });
    return parts;
  }

  private filterManufacturerNameForKeyword(
    parts: any[],
    keyword: string
  ): any[] {
    parts.forEach((part: any, index: number) => {
      let partName_lowercase: string = (
        part.data.manufacturerName as string
      ).toLowerCase();
      let keyword_lowercase: string = keyword.toLowerCase();
      if (!partName_lowercase.includes(keyword_lowercase)) {
        parts.splice(index, 1);
      }
    });
    return parts;
  }
}
