import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonModal, IonicModule } from '@ionic/angular';
import { LoginComponent } from '../login/login.component';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-navigationbar',
  templateUrl: './navigationbar.component.html',
  styleUrls: ['./navigationbar.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    IonicModule,
    NavigationbarComponent,
    FormsModule,
    LoginComponent,
  ],
})
export class NavigationbarComponent implements OnInit {
  @ViewChild(IonModal) modal?: IonModal;

  constructor(public user: UserService) {}

  ngOnInit() {}

  onWillDismiss(event: Event) {}

  cancel() {
    this.modal?.dismiss(null, 'cancel');
  }

  confirm() {
    this.modal?.dismiss(null, 'confirm');
  }
}
