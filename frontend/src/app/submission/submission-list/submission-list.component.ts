import {Component, OnInit, ViewChild} from '@angular/core';
import {Submission} from '../../shared/model/submission/submission.model';
import {ActivatedRoute} from '@angular/router';
import {ConferenceService} from '../../core/service/conference.service';
import {TokenService} from '../../core/service/token.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {BriefSubmission} from '../../shared/model/submission/brief-submission.model';
import {Page} from '../../shared/model/paging/page.model';
import {BriefUserRoles} from '../../shared/model/user/brief-user-roles.model';
import {ConferenceRoleService} from '../../core/service/utils/conference-role.service';
import {SubmissionStatusService} from '../../core/service/utils/submission-status.service';
import { map, filter, pairwise } from 'rxjs/operators';

@Component({
  selector: 'app-submissions',
  templateUrl: './submission-list.component.html',
  styleUrls: ['./submission-list.component.css']
})
export class SubmissionsComponent implements OnInit {

  form!: FormGroup;
  submissionType = ['All submissions', 'My submissions', 'Submissions that I must review'];
  page!: Page<BriefSubmission>;
  conferenceId!: number;
  userRoles!: BriefUserRoles;

  status!: number;

  loading: boolean = false;

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private conferenceService: ConferenceService,
              private conferenceRoleService: ConferenceRoleService,
              public tokenService: TokenService,
              public submissionStatusService: SubmissionStatusService) {
    this.createForm();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['conferenceId']) {
        this.conferenceId = params['conferenceId'];
        this.getUserRoles();
        this.updateSubmissionsData();
      }
    });
  }


  pageChange(page: number) {
    this.page.number = page;
    this.updateSubmissionsData();
  }

  updateSubmissionsData() {
    this.conferenceService
      .getSubmissions(this.conferenceId, this.status, this.page ? this.page.number : 0).subscribe((data => this.page = data));
  }

  submit(value: any) {
    console.log(value);
    this.status = this.submissionStatusService.getStatusNumber(value.status);
    switch (value.type) {
      case 'My submissions':
        this.conferenceService.getUserSubmissions(this.conferenceId, this.status).subscribe(data => this.page = data);
        break;
      case 'All submissions':
        this.conferenceService.getSubmissions(this.conferenceId, this.status).subscribe(data => this.page = data);
        break;
      case 'Submissions that I must review':
        this.conferenceService.getReviewerSubmissions(this.conferenceId, this.status).subscribe(data => this.page = data);
        break;
    }
  }

  createForm() {
    this.form = this.fb.group({
      type: ['Все доклады', Validators.required],
      status: ['', Validators.required],
    });
  }

  private getUserRoles() {
    this.conferenceService.getUserRoles(this.conferenceId)
      .subscribe(data => this.userRoles = data);
  }

  isReviewer() {
    return new Set(this.userRoles.roles).has(this.conferenceRoleService.getReviewerNumber());
  }

  getDisplayableSubmissionType() {
    if (this.userRoles !== undefined) {
      if (this.isReviewer()) {
        return this.submissionType;
      }
    }
    return ['All submissions', 'My submissions'];
  }

}



