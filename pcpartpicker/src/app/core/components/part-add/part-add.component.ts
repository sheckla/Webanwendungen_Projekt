import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { IonicModule } from '@ionic/angular';
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
  public partGroup: FormGroup = new FormGroup({
    partType: new FormControl(null, [Validators.required]),
    partInfo: new FormGroup({}),
  });
  public questions?: FormQuestion<any>[] = [];

  constructor(
    public partCreator: PartCreatorService,
    public toast: ToastService
  ) {}

  ngOnInit() {
    // this.addGroup = this.toFormGroup([]);
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
    questions.forEach((question: any) => {
      group[question.key] = new FormControl('', null);
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
    console.log(this.partGroup.value);
    if (this.partGroup.valid) {
      let partInfo: any | null = this.partGroup.get('partInfo')?.value;
      let partType: string | null = this.partGroup.get('partType')?.value;
      if (partInfo && partType) {
        console.log(partInfo, partType);
        this.partCreator.uploadPart(partInfo, partType);
        this.toast.present('top', 'Part created!');
      }
    }
  }
}
