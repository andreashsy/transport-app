import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Constants } from "../constants";
import { User } from "../models/model";

@Injectable()
export class UserService {
  constructor(
    private http: HttpClient
  ) { }

  async sendRegistration(user: User) {
    return await lastValueFrom(
      this.http.post<any>(Constants.URL_BASE + "api/user", user)
    )
  }
}
