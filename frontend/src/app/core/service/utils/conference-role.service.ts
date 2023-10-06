import {Injectable} from '@angular/core';

@Injectable()
export class ConferenceRoleService {

  private roles = ['Submitter', 'Reviewer', 'Organizer', 'Conference admin'];
  private changeableRoles = ['Submitter', 'Reviewer', 'Conference admin'];

  public getSubmitterNumber(): number {
    return 0;
  }

  public getReviewerNumber(): number {
    return 1;
  }

  public getCreatorNumber(): number {
    return 2;
  }

  public getAdminNumber(): number {
    return 3;
  }

  public getRole(roleNumber: number): string {
    switch (roleNumber) {
      case 0:
        return 'Submitter';
      case 1:
        return 'Reviewer';
      case 2:
        return 'Organizer';
      case 3:
        return 'Conference admin';
      default:
        return "";
    }
  }

  public getRoleNumber(role: string): number {
    switch (role) {
      case 'Submitter':
        return 0;
      case 'Reviewer':
        return 1;
      case 'Organizer':
        return 2;
      case 'Conference admin':
        return 3;
      default :
        return -1;
    }
  }

  getChangeableRoles(): string[] {
    return this.changeableRoles;
  }
}
