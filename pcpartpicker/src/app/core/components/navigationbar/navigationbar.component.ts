import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonModal, IonicModule } from '@ionic/angular';
import { LoginComponent } from "../login/login.component";

@Component({
    selector: 'app-navigationbar',
    templateUrl: './navigationbar.component.html',
    styleUrls: ['./navigationbar.component.scss'],
    standalone: true,
    imports: [IonicModule, NavigationbarComponent, FormsModule, LoginComponent]
})
export class NavigationbarComponent implements OnInit {
  @ViewChild(IonModal) modal?: IonModal;

  public name: string = '';

  constructor() {}

  ngOnInit() {}

  onWillDismiss(event: Event) {
    console.log(event);
  }

  cancel() {
    this.modal?.dismiss(null, 'cancel');
  }

  confirm() {
    this.modal?.dismiss(this.name, 'confirm');
  }
}
