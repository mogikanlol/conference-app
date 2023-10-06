import {Injectable} from '@angular/core';

@Injectable()
export class ReviewStatusService {

  private displayableStatuses = ['Accept', 'Accept, edit required', 'Can not decide', 'Decline, edit required', 'Decline'];

  public getStatus(statusNumber: number): string {
    switch (statusNumber) {
      case 0:
        return 'Decline';
      case 1:
        return 'Decline, edit required';
      case 2:
        return 'Can not decide';
      case 3:
        return 'Accept, edit required';
      case 4:
        return 'Accept';
      default:
        return "";
    }
  }

  public getStatusNumber(status: string): number {
    switch (status) {
      case 'Decline':
        return 0;
      case 'Decline, edit required':
        return 1;
      case 'Can not decide':
        return 2;
      case 'Accept, edit required':
        return 3;
      case 'Accept':
        return 4;
      default:
        return -1;
    }
  }

  public getDicplayableStatuses(): string[] {
    return this.displayableStatuses;
  }
}
