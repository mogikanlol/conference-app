import {Injectable} from '@angular/core';

@Injectable()
export class SubmissionStatusService {
  private displayableStatuses = ['Being reviewed', 'Declined', 'Accepted'];

  public getStatus(statusNumber: number): string {
    switch (statusNumber) {
      case 0:
        return 'Being reviewed';
      case 1:
        return 'Declined';
      case 2:
        return 'Accepted';
      default:
        return "";
    }
  }

  public getStatusNumber(status: string): number {
    switch (status) {
      case this.getDicplayableStatuses()[0]:
        return 0;
      case this.getDicplayableStatuses()[1]:
        return 1;
      case this.getDicplayableStatuses()[2]:
        return 2;
      default:
        return -1;
    }
  }

  public getPendingNumber(): number {
    return 0;
  }

  public getDeclinedNumber(): number {
    return 1;
  }

  public getAcceptedNumber(): number {
    return 2;
  }

  public getDicplayableStatuses(): string[] {
    return this.displayableStatuses;
  }
}
