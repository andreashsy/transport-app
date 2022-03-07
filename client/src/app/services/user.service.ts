import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Constants } from "../constants";
import { BusStop, NotificationForm, User } from "../models/model";
import { TokenService } from "./token.service";

@Injectable()
export class UserService {
  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }

  async sendRegistration(user: User) {
    return await lastValueFrom(
      this.http.post<any>(Constants.URL_BASE + "api/user", user)
    )
  }

  async login(user: User) {
    return await lastValueFrom(
      this.http.post<any>(Constants.URL_BASE + "api/authenticate", user)
    )
  }

  async saveFavouriteBusStop(username: string, busStopCode: string) {
    return await lastValueFrom(
      this.http.post<any>(Constants.URL_BASE + "api/secure/favourite", busStopCode, this.generateAuthenticationHeaders())
    )
  }

  async getFavouriteBusStops(username: String) {
    return await lastValueFrom(
      this.http.get<any>(Constants.URL_BASE + "api/secure/favourite", this.generateAuthenticationHeaders())
    )
  }

  async deleteFavouriteBusStops(username: string, busStopCode: string) {
    return await lastValueFrom(
      this.http.delete(Constants.URL_BASE + "api/secure/favourite/" + busStopCode, this.generateAuthenticationHeaders())
    )
  }

  async addNotification(notification: NotificationForm) {
    return await lastValueFrom(
      this.http.post<any>(
        Constants.URL_BASE + "api/secure/notification",
        notification,
        this.generateAuthenticationHeaders())
    )
  }

  async getNotifications(username: String) {
    return await lastValueFrom(
      this.http.get<any>(Constants.URL_BASE + "api/secure/notification", this.generateAuthenticationHeaders())
    )
  }

  async deleteNotification(username: string, busStopCode: string, cronString: string) {
    let headers = {
      headers: new HttpHeaders({
        Authorization: this.tokenService.jwtToken || "",
        Username: this.tokenService.username || "",
        cronString: cronString
      })
    }
    return await lastValueFrom(
      this.http.delete(Constants.URL_BASE + "api/secure/notification/" + busStopCode, headers)
    )
  }

  async updateToken(user: User) {
    return await lastValueFrom(
      this.http.patch<any>(Constants.URL_BASE + "api/secure/firebasetoken", user, this.generateAuthenticationHeaders())
    )
  }

  generateAuthenticationHeaders() {
    return {
      headers: new HttpHeaders({
        Authorization: this.tokenService.jwtToken || "",
        Username: this.tokenService.username || ""
      })
    }
  }
}
