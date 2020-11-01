import { environment } from './../../environments/environment';
import { Observable } from 'rxjs';
import { VotePayload } from './vote-button/vote-payload';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class VoteService {
  baseUrl = environment.baseUrl;
  constructor(private http: HttpClient) {}

  vote(postIdentifier: number, votePayload: VotePayload): Observable<any> {
    return this.http.post(
      this.baseUrl +'/api/votes/' + postIdentifier,
      votePayload
    );
  }
}
