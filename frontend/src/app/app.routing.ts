import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from './authentication/guard/authentication.guard';
import { WebsocketComponent } from './websocket/websocket.component';
import { WebrtcComponent } from './webrtc/webrtc.component';
import { WebrtcComponent2 } from './webrtc2/webrtc.component';
import { WebrtcComponent3 } from './webrtc2 copy/webrtc.component';


const appRoutes: Routes = [
  // {
  //   path: 'home',
  //   loadChildren: 'app/home/home.module#HomeModule'
  // },
  // {
  //   path: 'dashboard',
  //   canActivate: [AuthenticationGuard],
  //   loadChildren: 'app/dashboard/dashboard.module#DashboardModule'
  // },
  // {
  //   path: 'profile',
  //   loadChildren: 'app/account/account.module#AccountModule'
  // },
  {
    path: 'conferences',
    loadChildren: () => import('./conference/conference.module').then(m => m.ConferenceModule)
  },
  {
    path: '',
    loadChildren: () => import('./submission/submission.module').then(m => m.SubmissionModule),
    canActivate: [AuthenticationGuard]
  },
  {
    path: '',
    loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
    canActivate: [AuthenticationGuard]
  },
  {
    path: '',
    loadChildren: () => import('./conference-request/conference-request.module').then(m => m.ConferenceRequestModule),
    canActivate: [AuthenticationGuard]
  },
  {
    path: '',
    loadChildren: () => import('./user/user.module').then(m => m.UserModule),
    canActivate: [AuthenticationGuard]
  },



  // {
  //   path: 'conferences/:conferenceId',
  //   loadChildren: 'app/review/review.module#ReviewModule',
  //   canActivate: [AuthenticationGuard]
  // },



  {
    path: '',
    loadChildren: () => import('./signin/signin.module').then(m => m.SigninModule)
  },
  {
    path: '',
    loadChildren: () => import('./signup/signup.module').then(m => m.SignupModule)
  },
  {
    path: '',
    redirectTo: 'conferences',
    pathMatch: 'full'
  },
  {
    path: 'websocket-test',
    component: WebsocketComponent
  },
  {
    path: 'webrtc-test',
    component: WebrtcComponent
  },
  {
    path: 'webrtc-test-2',
    component: WebrtcComponent2
  },
  {
    path: 'webrtc-test-3',
    component: WebrtcComponent3
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
