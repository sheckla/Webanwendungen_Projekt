import { Injectable, OnInit } from '@angular/core';
import { ApiService } from '../api/api.service';
import { environment } from 'src/environments/environment.prod';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

interface UserData {
  id: number,
  username: string,
  role: 'user' | 'admin' | 'moderator'
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public isLoggedIn: boolean = false;
  private keepLoggedIn?: boolean;
  public username?: string;
  public password?: string;
  public role?: string;

  constructor(
    private http: HttpClient) {
    let keepLoggedInStr: string | null = localStorage.getItem('keepLoggedIn');
    if (keepLoggedInStr) {
      this.keepLoggedIn = (keepLoggedInStr == 'true');
    }
  }

  public getAuthHeader(): { Authorization: string; } {
    const header = {
      Authorization: 'Basic ' + btoa(this.username + ':' + this.password)
    }
    return header;
  }

  public setKeepLoggedIn(keep: string): void {
    // this.keepLoggedIn = keep;
    console.log(keep + '')
    localStorage.setItem('keepLoggedIn', '' + keep);
  }

  login(username: string, password: string): void {
    const url = environment.api.baseUrl + '/users/me';

    // build header manually only for login
    const header = {
      Authorization: 'Basic ' + btoa(username + ':' + password)
    }

    this.http
      .get(url, { headers: header })
      .subscribe(
        (data: any) => {
          const userData: UserData = {
            id: data.id,
            username: data.name,
            role: data.role
          }
          console.log(data);
          this.username = username;
          this.password = password;
          this.role = userData.role;
          this.isLoggedIn = true;
          if (this.keepLoggedIn) {
            localStorage.setItem('username', this.username);
            localStorage.setItem('password', this.password);
          }
        },
        (error: any) => {
          // anything besides successfull indicates error
          this.isLoggedIn = false;
          this.username = '';
          this.password = '';
        })
  }

  public logout(): void {
    this.isLoggedIn = false;
    this.username = undefined;
    this.password = undefined;
    this.role = undefined;
  }

  public register(username: string, password: string) {
    const url = environment.api.baseUrl + '/users/register';
    const body = {
      name: username,
      password: password
    }
    this.http.post(url, body, { responseType: 'text' })
      .subscribe((data: any) => {
        
      },
        (error: any) => {
          // anything besides 200 indicates error while registering
        })
  }
}
