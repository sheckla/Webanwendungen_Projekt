import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ApiService } from 'src/app/services/api/api.service';
import { ToastService } from 'src/app/services/toast/toast.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class CommentListComponent implements OnInit {
  @Input() part?: any;
  @Input() type?: 'part' | 'configuration' = 'part';
  public comments: any[] = [];

  constructor(
    public api: ApiService,
    public toast: ToastService,
    public user: UserService
  ) {}

  ngOnInit() {
    if (this.type === 'part') {
      this.comments = this.api.getPartComments(this.part);
    } else {
      this.api
        .getPublicConfigurationComments(this.part)
        .subscribe((data: any) => {
          this.comments = data;
        });
      // this.comments = this.api.getPublicConfigurationComments(this.part);
    }
    // this.comments = this.part.comments;
  }

  deleteComment(comment: any): void {
    if (this.type == 'part') {
      this.api.deletePartComment(this.part, comment).subscribe((data: any) => {
        this.toast.present('top', 'Comment removed!');
      });
    } else {
      this.api.deleteConfigurationComment(this.part, comment).subscribe((data: any) => {});
    }
  }

  public convertTimeString(time: string): string {
    let date: Date = new Date(time);
    return date.toISOString().substring(0, 10);
  }
}
