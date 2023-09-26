import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validator,
  Validators,
} from '@angular/forms';
import { IonModal, IonicModule, ToastController } from '@ionic/angular';
import { ToastService } from 'src/app/services/toast/toast.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, ReactiveFormsModule],
})
export class LoginComponent implements OnInit {
  @ViewChild(IonModal) modal?: IonModal;
  public loginForm: FormGroup = new FormGroup({
    loginName: new FormControl(null, [Validators.required]),
    loginPassword: new FormControl(null, [Validators.required]),
    keepLoggedIn: new FormControl(null),
  });
  public registerForm: FormGroup = new FormGroup({
    registerName: new FormControl(null, [Validators.required]),
    registerPassword: new FormControl(null, [Validators.required]),
  });
  public registerFormActive: boolean = false;

  public name: string = '';

  constructor(public user: UserService, private toastService: ToastService) {}

  ngOnInit() {}

  onWillDismiss(event: Event) {
    console.log('dismissed');
  }

  public toggleRegister() {
    this.registerFormActive = !this.registerFormActive;
  }

  cancel() {
    this.modal?.dismiss(null, 'cancel');
    this.loginForm.reset();
    this.registerForm.reset();
    this.registerFormActive = false;
  }

  confirm() {
    this.modal?.dismiss(this.name, 'confirm');
  }

  login() {
    this.loginForm.updateValueAndValidity();
    this.loginForm.markAllAsTouched();
    if (this.loginForm.valid) {
      let username: string = this.loginForm.controls['loginName'].value;
      let password: string = this.loginForm.controls['loginPassword'].value;
      if (this.loginForm.controls['keepLoggedIn'].value) {
        this.user.setKeepLoggedIn(
          this.loginForm.controls['keepLoggedIn'].value
        );
      } else {
        this.user.setKeepLoggedIn('false');
      }
      this.user.login(username, password).subscribe((data: any) => {
        this.toastService.present('top', 'Login Successful!');
      },
      (error: any) => {
        this.toastService.present('top', 'Login failed!');
      });
    }
  }

  register() {
    this.registerForm.updateValueAndValidity();
    this.registerForm.markAllAsTouched();
    if (this.registerForm.valid) {
      let username: string = this.registerForm.controls['registerName'].value;
      let password: string =
        this.registerForm.controls['registerPassword'].value;
      this.user.register(username, password);
      this.toastService.present('top', 'User Registered!');
    }
  }

  logout() {
    this.user.logout();
    this.toastService.present('top', 'You have been logged out');
  }
}
