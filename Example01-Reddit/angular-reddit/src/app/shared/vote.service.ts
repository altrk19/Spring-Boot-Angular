import { Observable } from 'rxjs';
import { VotePayload } from './vote-button/vote-payload';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class VoteService {
  constructor(private http: HttpClient) {}

  vote(postIdentifier: number, votePayload: VotePayload): Observable<any> {
    return this.http.post(
      'http://localhost:8080/api/votes/' + postIdentifier,
      votePayload
    );
  }
}
