import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Submission} from '../../shared/model/submission/submission.model';
import {BriefUser} from '../../shared/model/user/brief-user.model';
import {Page} from '../../shared/model/paging/page.model';

@Injectable()
export class SubmissionService {


  constructor(private http: HttpClient) {
  }

  getSubmissions(conferenceId: number) {
    return this.http.get('http://localhost:8081/api/submissions');
  }

  getReviewableSubmisisons(conferenceId: number): Observable<Array<Submission>> {
    const params = new HttpParams().set('reviewable', true.toString());
    return this.http.get<Array<Submission>>('http://localhost:8081/api/conferences/' + conferenceId + '/submissions', {params: params});
  }

  getSubmission(submissionId: number): Observable<Submission> {
    return this.http.get<Submission>('http://localhost:8081/api/submissions/' + submissionId);
  }

  setReviewable(submissionId: number, reviewable: boolean): Observable<Submission> {
    return this.http
      .put<Submission>('http://localhost:8081/api/submissions/' + submissionId + '/reviewable', reviewable.toString());
  }

  uploadDocument(submissionId: number, file: File): Observable<Submission> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return this.http.post<Submission>('http://localhost:8081/api/submissions/' + submissionId + '/documents', formdata);
  }

  getReviewers(submissionId: number, page: number = 0): Observable<Page<BriefUser>> {
    return this.http.get<Page<BriefUser>>('http://localhost:8081/api/submissions/' + submissionId + '/reviewers?page=' + page);
  }

  addReviewers(submissionId: number, reviewers: Array<number>) {
    return this.http.post('http://localhost:8081/api/submissions/' + submissionId + '/reviewers', reviewers);
  }

  deleteReviewers(submissionId: number, reviewers: Array<number>) {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }), body: reviewers
    };
    return this.http.delete('http://localhost:8081/api/submissions/' + submissionId + '/reviewers', httpOptions);
  }


}
