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
import { tap } from 'rxjs';
import { ApiService } from 'src/app/services/api/api.service';
import { PartCreatorService } from 'src/app/services/part-creator/part-creator.service';
import {
  FilterSelectComponent,
  SelectOption,
} from '../../components/filter/filter-select/filter-select.component';
import { ImageUploadComponent } from '../../components/image-upload/image-upload.component';

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
  ],
})
export class DashboardPage implements OnInit {
  selectedImageFile?: File;
  fileMap: Map<String, File> = new Map();
  public partId = '64fec3535cdd7636718eb9ce';
  myInputList: string[] = ['', 'Hello World', 'Bye World'];
  public options: SelectOption<string>[] = [
    { value: 'CPU', label: 'Cpu' },
    { value: 'MAINBOARD', label: 'Mainboard' },
  ];
  selectedCar: any;
  filterForm: FormGroup = new FormGroup({
    partType: new FormControl<string | null>(null),
    hasImage: new FormControl<boolean | null>(null),
  });

  constructor(public api: ApiService, public partCreator: PartCreatorService) {}

  ngOnInit() {
    // console.log(this.options);
    this.api.getAllParts();
    // let name = "daniel" + (Math.random() * 1000).toFixed(0);
    // this.api.createUser("peter", "peter");
    // this.api.getUserMe('admin', 'admin');
    this.filterForm.valueChanges.pipe(
      tap((value: any) => {
        // console.log('pipe, tap', value);
      })
    );
    this.filterForm.valueChanges.subscribe((value: any) => {
      // console.log('subscribe', value);
    });
  }

  test() {
    console.log('outline');
  }
}
