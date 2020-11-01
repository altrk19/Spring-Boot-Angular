import { environment } from './../../environments/environment';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubredditModel } from './subreddit-model';

@Injectable({
  providedIn: 'root',
})
export class SubredditService {
  baseUrl = environment.baseUrl;
  constructor(private httpClient: HttpClient) {}

  getAllSubreddits(): Observable<Array<SubredditModel>> {
    return this.httpClient.get<Array<SubredditModel>>(
      this.baseUrl +'/api/subreddit'
    );
  }

  createSubreddit(subredditModel: SubredditModel): Observable<SubredditModel> {
    return this.httpClient.post<SubredditModel>(
      this.baseUrl +'/api/subreddit',
      subredditModel
    );
  }
}
