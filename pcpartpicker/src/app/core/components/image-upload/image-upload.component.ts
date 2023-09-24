import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ApiService } from '../../../services/api/api.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class ImageUploadComponent implements OnInit {
  @Input() imgSrc?: string;
  @Input() partId?: string;
  private selectedImageFile?: File;

  constructor(private api: ApiService,
    public user: UserService) { }

  ngOnInit() { }

  public setFileInput(fileInputEvent: any): void {
    if (!fileInputEvent.target.files) {
      console.error("Settings File didn'nt work, no valid file found");
      return;
    }
    this.selectedImageFile = fileInputEvent.target.files[0];
  }

  public uploadSelectedFile(): void {
    if (!this.selectedImageFile) {
      console.error('no file to upload');
      return;
    }
    if (!this.partId) {
      console.error('no valid partId');
      return;
    }
    this.api.uploadPartImage(this.partId, this.selectedImageFile);
  }
}
