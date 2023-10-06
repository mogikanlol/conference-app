import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ConferenceRequest} from '../../shared/model/conference-request/request.model';
import {UserService} from './user.service';
import {BriefConferenceRequest} from '../../shared/model/conference-request/brief-request.model';
import {ConferenceRequestComment} from '../../shared/model/conference-request/request-comment.model';
import {Page} from '../../shared/model/paging/page.model';

@Injectable()
export class RequestService {
  constructor(private http: HttpClient, private userService: UserService) {
  }

  createRequest(request: ConferenceRequest) {
    console.log(request);
    return this.http.post('http://localhost:8081/api/requests', request);
  }

  getAll(page: number = 0, statusNumber?: number): Observable<Page<BriefConferenceRequest>> {
    let params;
    if (statusNumber !== undefined) {
      params = new HttpParams().set('status', statusNumber.toString());
    }
    return this.http.get<Page<BriefConferenceRequest>>('http://localhost:8081/api/requests?page=' + page, {params: params});
  }

  getRequest(id: number | string): Observable<ConferenceRequest> {
    return this.http.get<ConferenceRequest>('http://localhost:8081/api/requests/' + id);
  }

  update(request: ConferenceRequest) {
    return this.http.put('http://localhost:8081/api/requests', request);
  }

  createComment(requestId: number, comment: ConferenceRequestComment): Observable<ConferenceRequest> {
    return this.http.post<ConferenceRequest>('http://localhost:8081/api/requests/' + requestId + '/comments', comment);
  }
}
