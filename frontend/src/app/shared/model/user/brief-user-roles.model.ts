import {BriefUser} from './brief-user.model';

export class BriefUserRoles extends BriefUser {
  constructor(override username: string,
              override email: string,
              override firstname: string,
              override lastname: string,
              override id: number,
              public roles: Set<number>) {
    super(username, firstname, lastname, id, email);
  }
}
