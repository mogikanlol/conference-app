import {ActivatedRouteSnapshot, Resolve, ResolveFn, RouterStateSnapshot} from '@angular/router';
import {inject} from '@angular/core';
import {Observable} from 'rxjs';
import {Conference} from '../../../shared/model/conference/conference.model';
import {ConferenceService} from '../../service/conference.service';

export const ConferenceResolve: ResolveFn<Conference> =

  (route: ActivatedRouteSnapshot,
          state: RouterStateSnapshot) => {
         console.log("in conference resolve");   
    const conferenceId = route.paramMap.get('conferenceId');
    return inject(ConferenceService).getConference(conferenceId!);
  }