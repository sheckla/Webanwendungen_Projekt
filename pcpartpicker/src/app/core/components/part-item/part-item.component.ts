import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ImageUploadComponent } from '../image-upload/image-upload.component';
import { ApiService } from 'src/app/services/api/api.service';
import { CommentAddComponent } from '../comment-add/comment-add.component';
import { CommentListComponent } from '../comment-list/comment-list.component';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-part-item',
  templateUrl: './part-item.component.html',
  styleUrls: ['./part-item.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    ImageUploadComponent,
    CommentAddComponent,
    CommentListComponent,
  ],
})
export class PartItemComponent implements OnInit, OnChanges {
  @Input() item: any;

  constructor(public api: ApiService, public user: UserService) {}

  ngOnInit() {}
  ngOnChanges() {}
}
