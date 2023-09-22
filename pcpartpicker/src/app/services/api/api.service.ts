import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment.prod';
import { Observable, of, tap } from 'rxjs';
import { Pair } from '../part-types';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private loggedIn: Boolean = false;
  private username?: string;
  public privateConfigurations: any[] = [];

  public cases: any[] = [];
  public cpus: any[] = [];
  public gpus: any[] = [];
  public fans: any[] = [];
  public motherboards: any[] = [];
  public psus: any[] = [];

  constructor(private http: HttpClient) {}

  getBasicAuthHeader() {
    const header = {
      'Content-Type': 'application/json',
      Authorization: 'Basic ' + btoa('admin:admin'),
    };
    return header;
  }

  getAllParts() {
    const allPartsUrl = environment.api.baseUrl + '/pc-parts/all-parts';
    console.log('getting parts from ', allPartsUrl);

    if (this.cases.length > 0) {
      return;
    }

    this.http.get(allPartsUrl).subscribe((data: any) => {
      this.cases = [];
      this.cpus = [];
      this.gpus = [];
      this.fans = [];
      this.motherboards = [];
      this.psus = [];
      console.log('all parts:', data);

      data.forEach((element: any) => {
        switch (element.data.type) {
          case 'CASE':
            this.cases?.push(element);
            break;
          case 'CPU':
            this.cpus?.push(element);
            break;
          case 'GPU':
            this.gpus?.push(element);
            break;
          case 'FAN':
            this.fans?.push(element);
            break;
          case 'PSU':
            this.psus?.push(element);
            break;
          default:
            break;
        }
      });
    });
  }

  allParts(): any[] {
    let parts: any[] = [];
    parts = this.cases.concat(
      this.cpus,
      this.gpus,
      this.fans,
      this.motherboards,
      this.psus
    );
    return parts;
  }

  createUser(name: string, password: string) {
    const userCreateUrl = environment.api.baseUrl + '/users/register';
    const body = {
      name: name,
      password: password,
    };

    this.http
      .post(userCreateUrl, body, {
        headers: this.getBasicAuthHeader(),
        responseType: 'text',
      })
      .subscribe(
        (data: any) => {
          console.log('created user', data);
        },
        (error: any) => {
          console.log('err while creating user:', error.error);
        }
      );
  }

  getUserMe(name: String, password: String): Observable<any> {
    const url = environment.api.baseUrl + '/users/me';
    let observable: Observable<any>;
    return this.http
      .get(url, { headers: this.getBasicAuthHeader(), responseType: 'text' })
      .pipe(
        tap((data: any) => {
          console.log(data);
          this.loggedIn = true;
        })
      );
  }

  isModerator() {}

  getPartImageLink(id: string): string {
    return environment.api.baseUrl + '/pc-parts/' + id + '/image';
  }

  uploadPartImage(id: string, file: File) {
    const partImageUrl = environment.api.baseUrl + '/pc-parts/' + id + '/image';

    let formData: FormData = new FormData();
    formData.append('image-file', file, file.name);
    console.log(file.name, file.type);

    let httpHeader = new HttpHeaders();
    httpHeader = httpHeader.set('enctype', 'multipart/form-data');
    httpHeader = httpHeader.set(
      'Authorization',
      'Basic ' + btoa('admin:admin')
    );

    this.http
      .post(partImageUrl, formData, {
        headers: httpHeader,
        responseType: 'text',
      })
      .subscribe((data: any) => {
        console.log(data);
        this.getPartImageLink(id);
      });
  }

  getPartImageSrc(part: any) {
    let src = '../../assets/icon/favicon.png';
    // let src = '../../assets/img/poke-ball.png';
    if (part.imgSrc) {
      return part.imgSrc;
    }
    if (part.data.hasImage) {
      src = this.getPartImageLink(part.data.id);
    }
    part.imgSrc = src;
    return src;
  }

  getPartValuePairs(part: any): Pair[] {
    if (!part) {
      return [];
    }
    let pairs: Pair[] = [];
    Object.keys(part).forEach((key, index) => {
      let pair = { key: key, value: part[key] };
      pairs.push(pair);
    });
    return pairs;
  }

  postPartComment(part: any, text: any, rating: any): void {
    const partCommentUrl =
      environment.api.baseUrl + '/pc-parts/' + part.data.id + '/comment';
    const headers = this.getBasicAuthHeader();
    const body = {
      commentary: text,
      userRating: rating,
    };
    console.log(body);
    console.log('publishing comment: ', text, rating);
    this.http
      .post(partCommentUrl, body, { headers: headers })
      .subscribe((data: any) => {
        console.log(data);
        part.comments.push(data);
      });
  }

  getPartComments(part: any): any[] {
    if (part.comments) {
      return part.comments;
    }
    this.refreshPartComments(part);
    return part.comments;
  }

  /*
   * assigns comments[] attribute to part
   */
  refreshPartComments(part: any): void {
    const url =
      environment.api.baseUrl + '/pc-parts/' + part.data.id + '/comments';
    this.http.get(url).subscribe((data: any) => {
      // TODO dangerous if parts not gotten correctly
      part.comments = data;
    });
  }

  deletePartComment(part: any, comment: any): void {
    const url =
      environment.api.baseUrl +
      '/pc-parts/' +
      comment.parentId +
      '/comments/' +
      comment.id;
    console.log(url);
    this.http.delete(url, { headers: this.getBasicAuthHeader() }).subscribe(
      (data: any) => {
        console.log(data);
        this.refreshPartComments(part);
      },
      (error: any) => {
        console.log('error while deleting comment', error.status);
      }
    );
  }

  createConfiguration(name: string) {
    const url = environment.api.baseUrl + '/computers/private/create-new-pc';
    this.http
      .put(url, name, { headers: this.getBasicAuthHeader() })
      .subscribe((data: any) => {
        console.log(data);
        this.getPrivateConfigurations();
      });
  }

  getPrivateConfigurations() {
    if (!this.loggedIn) {
      console.log('user not logged in');
      return;
    }
    const url = environment.api.baseUrl + '/computers/private';
    this.http
      .get(url, { headers: this.getBasicAuthHeader() })
      .subscribe((data: any) => {
        this.privateConfigurations = data;
        console.log(data);
      });
  }

  postPart(part: any, type: string) {
    const url = environment.api.baseUrl + '/pc-parts/' + type.toLowerCase();
    const headers = this.getBasicAuthHeader();
    this.http
      .put(url, part, { headers: headers, responseType: 'text' })
      .subscribe((data: any) => {
        this.getAllParts();
      });
  }

  deletePart(part: any) {
    const url = environment.api.baseUrl + '/pc-parts/' + part.data.id;
    const headers = this.getBasicAuthHeader();
    console.log('deleting at', url);
    this.http
      .delete(url, { headers: this.getBasicAuthHeader() })
      .subscribe((data: any) => {
        part = null;
        this.getAllParts();
      });
  }
}