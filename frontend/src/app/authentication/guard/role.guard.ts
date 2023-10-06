import {ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ConferenceService} from '../../core/service/conference.service';

import {BriefUserRoles} from '../../shared/model/user/brief-user-roles.model'
import { map, filter, switchMap } from 'rxjs/operators';

@Injectable()
export class RoleGuard {
  constructor(private conferenceService: ConferenceService) {
  }

  canActivate(route: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean> {
    const minRole: number = route.data['roles'];
    if (route.params['conferenceId']) {
      return this.conferenceService.getUserRoles(route.params['conferenceId'])
        .pipe(map((data:BriefUserRoles) => {
          const roles = new Set(data.roles);
          roles.forEach(value => {
            if (value >= minRole) {
              return true;
            } else {
              return false;
            }
          });
          return false;
        }));
    } else {
       return new Observable();
    }

  }

}
