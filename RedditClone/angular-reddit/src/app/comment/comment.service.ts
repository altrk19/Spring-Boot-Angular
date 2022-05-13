import { environment } from './../../environments/environment';
import { Observable } from 'rxjs';
import { CommentPayload } from './comment-payload';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  baseUrl = environment.baseUrl;

  constructor(private httpClient: HttpClient) {}

  getAllCommentsForPost(postId: number): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>(
      this.baseUrl + '/api/comments/by-post/' + postId
    );
  }

  postComment(
    postId: number,
    commentPayload: CommentPayload
  ): Observable<CommentPayload> {
    return this.httpClient.post<CommentPayload>(
      this.baseUrl + '/api/comments/' + postId,
      commentPayload
    );
  }

  getAllCommentsByUser(name: string) {
    return this.httpClient.get<CommentPayload[]>(
      this.baseUrl + '/api/comments/by-user/' + name
    );
  }
}
