import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Review} from '../../shared/model/review/review.model';

@Injectable()
export class ReviewService {
  constructor(private http: HttpClient) {
  }

  getReview(reviewId: number): Observable<Review> {
    return this.http.get<Review>('/backend/api/reviews/' + reviewId);
  }

  createReview(documentId: number, review: Review) {
    return this.http.post('/backend/api/documents/' + documentId + '/reviews', review);
  }


  updateReview(review: Review): Observable<Review> {
    return this.http.put<Review>('/backend/api/reviews', review);
  }

  submitReview(review: Review): Observable<Review> {
    return this.http.post<Review>('/backend/api/reviews/' + review.id + '/submit', review);
  }

  deleteReview(reviewId: number) {
    return this.http.delete('/backend/api/reviews/' + reviewId);
  }
}
