import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Review} from '../../shared/model/review/review.model';

@Injectable()
export class ReviewService {
  constructor(private http: HttpClient) {
  }

  getReview(reviewId: number): Observable<Review> {
    return this.http.get<Review>('http://localhost:8081/api/reviews/' + reviewId);
  }

  createReview(documentId: number, review: Review) {
    return this.http.post('http://localhost:8081/api/documents/' + documentId + '/reviews', review);
  }


  updateReview(review: Review): Observable<Review> {
    return this.http.put<Review>('http://localhost:8081/api/reviews', review);
  }

  submitReview(review: Review): Observable<Review> {
    return this.http.post<Review>('http://localhost:8081/api/reviews/' + review.id + '/submit', review);
  }

  deleteReview(reviewId: number) {
    return this.http.delete('http://localhost:8081/api/reviews/' + reviewId);
  }
}
