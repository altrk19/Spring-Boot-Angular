import { SignupRequestPayload } from './../signup/signup-request-payload';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient:HttpClient){ }

  signup(signupRequestPayload:SignupRequestPayload): Observable<any> {
    return this.httpClient.post('http://localhost:8080/api/user/signUp', signupRequestPayload, {responseType:'text'});
  }
}
