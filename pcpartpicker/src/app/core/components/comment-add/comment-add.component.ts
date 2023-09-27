import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { ApiService } from 'src/app/services/api/api.service';
import { ToastService } from 'src/app/services/toast/toast.service';

@Component({
  selector: 'app-comment-add',
  templateUrl: './comment-add.component.html',
  styleUrls: ['./comment-add.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule, ReactiveFormsModule, FormsModule],
})
export class CommentAddComponent implements OnInit {
  @Input() item: any;
  @Input() type?: 'configuration' | 'part' = 'part';
  public commentGroup: FormGroup = new FormGroup({
    commentText: new FormControl(null, [Validators.required]),
    commentRating: new FormControl(null, [
      Validators.required,
      Validators.max(5),
      Validators.min(1),
    ]),
  });

  constructor(public api: ApiService, public toast: ToastService) {}

  ngOnInit() {
    this.commentGroup.controls['commentRating'].setValue(3);
  }
  tapLowerRating() {
    this.commentGroup.controls['commentRating'].setValue(
      this.commentGroup.controls['commentRating'].value - 1
    );
    if (this.commentGroup.controls['commentRating'].value < 1) {
      this.commentGroup.controls['commentRating'].setValue(1);
    }
  }

  tapHigherRating() {
    this.commentGroup.controls['commentRating'].setValue(
      this.commentGroup.controls['commentRating'].value + 1
    );
    if (this.commentGroup.controls['commentRating'].value > 5) {
      this.commentGroup.controls['commentRating'].setValue(5);
    }
  }

  public submitComment(): void {
    this.commentGroup.updateValueAndValidity();
    this.commentGroup.markAllAsTouched();
    let text: string = this.commentGroup.controls['commentText'].value;
    let rating: number = this.commentGroup.controls['commentRating'].value;
    if (this.commentGroup.valid) {
      if (this.type === 'part') {

        this.api
          .postPartComment(this.item, text, rating)
          .subscribe((data: any) => {
            this.toast.present('top', 'Comment added!');
          });
      } else {
        this.api.postConfigurationComment(this.item, text, rating)
          .subscribe((data: any) => {
            this.toast.present('top', 'Comment added!');
          })
      }
    }
  }

  public convertTimeString(time: string): string {
    let date: Date = new Date(time);
    return date.toISOString();
  }
}
