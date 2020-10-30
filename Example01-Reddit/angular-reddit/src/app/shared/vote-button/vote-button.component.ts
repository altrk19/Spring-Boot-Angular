import { throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { PostService } from './../post.service';
import { AuthService } from './../../auth/shared/auth.service';
import { VoteType } from './vote-type';
import { VotePayload } from './vote-payload';
import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { PostModel } from './../post-model';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css'],
})
export class VoteButtonComponent implements OnInit {
  @Input() post: PostModel;
  votePayload: VotePayload;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  upvoteColor: string;
  downvoteColor: string;
  isLoggedIn: boolean;

  constructor(
    private authService: AuthService,
    private postService: PostService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {}

  upvotePost() {
    this.votePayload.voteType = VoteType.UPVOTE;
    this.vote();
    this.downvoteColor = '';
  }

  downvotePost() {
    this.votePayload.voteType = VoteType.DOWNVOTE;
    this.vote();
    this.upvoteColor = '';
  }

  private vote() {
    this.votePayload.postId = this.post.identifier;
  }
}
