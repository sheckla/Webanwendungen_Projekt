import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { ApiService } from 'src/app/services/api/api.service';
import { UserService } from 'src/app/services/user/user.service';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';

@Component({
  selector: 'app-configuration',
  templateUrl: 'configuration.page.html',
  styleUrls: ['configuration.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, ReactiveFormsModule],
})
export class ConfigurationPage implements OnInit {
  public configuration?: any;
  public selectGroup: FormGroup = new FormGroup({
    configurationName: new FormControl(null, null),
  });
  public partGroup: FormGroup = new FormGroup({
    cpu: new FormControl(null, null),
  });

  constructor(public api: ApiService, public user: UserService) {}

  ngOnInit(): void {
    this.user.login('admin', 'admin').subscribe((data: any) => {
      this.api.getPrivateConfigurations();
    });

    this.selectGroup.valueChanges.subscribe((data: any) => {
      console.log(data);
      this.api.getAllParts();
      this.api
        .getPrivateConfiguration(data.configurationName)
        .subscribe((data: any) => {
          this.configuration = data;
          console.log(data);
        });
    });
  }
}
