import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { map, filter, switchMap } from 'rxjs/operators';


@Injectable()
export class AuthenticationService {

  url = '/backend/api/auth/';

  constructor(private http: HttpClient) {
  }

  doSignin(data: any) {
    return this.http.post(this.url + 'signin', data)
      .pipe(map((res:any) => {
        localStorage.setItem('token', res['token']);
        localStorage.setItem('user', JSON.stringify(res['user']));
      }));
  }

  doSignup(data: any) {
    console.log(data);
    return this.http.post(this.url + 'signup', data).pipe(map((res:any) => localStorage.setItem('token', res['token'])));
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  doLogout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }
}
