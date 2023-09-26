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
  Validators,
} from '@angular/forms';
import { ToastService } from 'src/app/services/toast/toast.service';
import { PartType, partList } from 'src/app/services/part-types';

@Component({
  selector: 'app-configuration',
  templateUrl: 'configuration.page.html',
  styleUrls: ['configuration.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, ReactiveFormsModule],
})
export class ConfigurationPage implements OnInit {
  public configuration?: any;
  public createConfigurationGroup: FormGroup = new FormGroup({
    name: new FormControl(null, [Validators.required]),
  });
  public configurationGroup: FormGroup = new FormGroup({
    id: new FormControl(null, [Validators.required]),
    partGroup: new FormGroup({
      cpuId: new FormControl(null, null),
      motherboardId: new FormControl(null, null),
      caseId: new FormControl(null, null),
      gpuId: new FormControl(null, null),
      fanId: new FormControl(null, null),
      psuId: new FormControl(null, null),
      public: new FormControl(null, null),
    }),
  });

  constructor(
    public api: ApiService,
    public user: UserService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.user.login('admin', 'admin').subscribe((data: any) => {
      console.log('login happened');
      this.api.getPrivateConfigurations();
      this.api.getAllParts()?.subscribe((data: any) => {});
    });

    this.disablePartIdForm();

    // When configuration is selected
    this.configurationGroup.controls['id'].valueChanges.subscribe(
      (data: any) => {
        if (!data) {
          return;
        }
        this.api.getPrivateConfiguration(data).subscribe((data: any) => {
          this.configuration = data;
          this.configurationGroup.get('partGroup')?.reset();
          /*
           *  Prefill reactive Form-Select with respective configuration ID's
           */
          // IsPublic Status
          const partGroup: FormGroup = this.configurationGroup.get(
            'partGroup'
          ) as FormGroup;
          partGroup.controls['public'].setValue(this.configuration.isPublic);

          // Cpu ID
          if (this.configuration.cpu) {
            partGroup.controls['cpuId'].setValue(this.configuration.cpu.id);
          }
          // Motherboard ID
          if (this.configuration.motherboard) {
            partGroup.controls['motherboardId'].setValue(
              this.configuration.motherboard.id
            );
          }
          // Case ID
          if (this.configuration.pc_case) {
            partGroup.controls['caseId'].setValue(
              this.configuration.pc_case.id
            );
          }
          // Gpu ID
          if (this.configuration.gpu) {
            partGroup.controls['gpuId'].setValue(this.configuration.gpu.id);
          }
          // Cooler/Fan ID
          if (this.configuration.fan) {
            partGroup.controls['fanId'].setValue(this.configuration.fan.id);
          }
          // Psu ID
          if (this.configuration.psu) {
            partGroup.controls['psuId'].setValue(this.configuration.psu.id);
          }
          this.enablePartIdForm();
        });
      }
    );
  }

  onResetSelection(): void {
    this.configurationGroup.get('partGroup')?.reset();
    this.configurationGroup.get('partGroup')?.disable();
    this.configurationGroup.controls['id'].reset();
    this.configuration = undefined;
  }

  onSubmitParts(): void {
    let form = this.configurationGroup.value;
    console.log('submit parts', form);

    // set public/private
    if (form.partGroup.public != undefined) {
      this.api
        .setPrivateConfigurationVisibility(
          this.configuration.id,
          form.partGroup.public
        )
        .subscribe((data: any) => {});
    }

    // Set part-id for each part type
    partList.forEach((type: PartType) => {
      let typeKey: string = (type as string).toLowerCase() + 'Id';
      if (form.partGroup[typeKey]) {
        this.api.setPrivateConfigurationPart(
          this.configuration.id,
          type,
          form.partGroup[typeKey]
        );
      }
    });
    this.toast.present('top', 'Configuration saved!');
  }

  public onCreateConfiguration() {
    const form = this.createConfigurationGroup.value;
    this.createConfigurationGroup.markAllAsTouched();
    if (this.createConfigurationGroup.valid) {
      this.api.createConfiguration(form.name);
    }
  }

  public onDeleteConfiguration() {
    console.log('delete');
    this.api
      .deletePrivateConfiguration(this.configuration.id)
      // .subscribe((data: any) => {
      //   this.toast.present('top', 'Configuration has been deleted');
      //   // this.onResetSelection();
      // });
  }

  private disablePartIdForm() {
    this.configurationGroup.get('partGroup')?.disable();
  }

  private enablePartIdForm() {
    this.configurationGroup.get('partGroup')?.enable();
  }
}
