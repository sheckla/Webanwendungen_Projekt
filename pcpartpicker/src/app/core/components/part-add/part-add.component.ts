import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { IonModal, IonicModule } from '@ionic/angular';
import { ApiService } from 'src/app/services/api/api.service';
import { PartCreatorService } from 'src/app/services/part-creator/part-creator.service';
import { FormQuestion } from 'src/app/services/part-types';
import { ToastService } from 'src/app/services/toast/toast.service';

@Component({
  selector: 'app-part-add',
  templateUrl: './part-add.component.html',
  styleUrls: ['./part-add.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, FormsModule, ReactiveFormsModule],
})
export class PartAddComponent implements OnInit {
  @ViewChild(IonModal) modal?: IonModal;
  public partGroup: FormGroup = new FormGroup({
    partType: new FormControl(null, [Validators.required]),
    partInfo: new FormGroup({}),
  });
  public questions?: FormQuestion<any>[] = [];

  constructor(
    public partCreator: PartCreatorService,
    public toast: ToastService,
    public api: ApiService
  ) { }

  ngOnInit() { }

  cancelModal() {
    this.modal?.dismiss(null, 'cancel');
  }

  onSelectPartType(event: Event) {
    this.partCreator.changePartType(event);
    this.partGroup.setControl(
      'partInfo',
      this.toFormGroup(this.partCreator.getPartFormQuestions())
    );
    this.questions = this.partCreator.getPartFormQuestions();
  }

  toFormGroup(questions: any[]) {
    const group: any = {};
    questions.forEach((question: FormQuestion<any>) => {
      group[question.key] = new FormControl('', null);
      (group[question.key] as FormControl).addValidators(question.validators);
    });
    // if (this.addGroup) {
    //   if (this.addGroup.controls['partType']) {
    //     group['partType'] = this.addGroup.controls['partType'];
    //     console.log('foudn');
    //   } else {
    //     group['partType'] = new FormControl('', null);
    //     console.log('creating new');
    //   }
    // }
    console.log(new FormGroup(group));
    return new FormGroup(group);
  }

  public onReset() {
    this.questions = [];
    this.partGroup.controls['partInfo'] = new FormGroup({});
  }

  public onSubmit() {
    this.partGroup.updateValueAndValidity();
    this.partGroup.markAllAsTouched();
    console.log('onpartSubmit', this.partGroup.value, this.partGroup.valid);
    if (this.partGroup.valid) {
      let partInfo: any | null = this.partGroup.get('partInfo')?.value;
      let partType: string | null = this.partGroup.get('partType')?.value;
      if (partInfo && partType) {
        console.log('valid', partInfo, partType);
        this.api.postPart(partInfo, partType).subscribe((data: any) => {
          console.log('got post in part-add');
          this.toast.present('top', 'Part created!');
        });
      }
    }
  }
}
