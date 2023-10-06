import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from './authentication/guard/authentication.guard';


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
