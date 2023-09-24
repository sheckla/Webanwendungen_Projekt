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
import {
  FilterSelectComponent,
} from '../../components/filter/filter-select/filter-select.component';
import { ImageUploadComponent } from '../../components/image-upload/image-upload.component';
import { UserService } from 'src/app/services/user/user.service';
import { PartItemComponent } from "../../components/part-item/part-item.component";

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
    PartItemComponent
  ]
})
export class DashboardPage implements OnInit {
  public partFilter: FormGroup = new FormGroup({
    cpu: new FormControl(null, null),
    mainboard: new FormControl(null, null),
    gpu: new FormControl(null, null),
    psu: new FormControl(null, null),
    case: new FormControl(null, null),
    fan: new FormControl(null, null),
    manufacturerName: new FormControl(null, null),
    partName: new FormControl(null, null)
  })

  public filteredParts: any[] = [];

  constructor(public api: ApiService,
    public partCreator: PartCreatorService,
    public user: UserService) { }

  ngOnInit() {
    this.api.getAllParts()?.subscribe((data: any) => {
      this.filteredParts = data;
      this.filteredParts = this.api.allParts();
    });

  }

  public resetFilter() {
    this.partFilter.reset();
  }

  public submitFilter() {
    let parts: any[] = [];
    let filter = this.partFilter.value;
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
    this.filteredParts = parts;
  }
}
