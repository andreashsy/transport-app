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
      this.http.post<any>(Constants.URL_BASE + "secure/favourite", busStopCode, this.generateHeaders())
    )
  }

  async getFavouriteBusStops(username: String) {
    return await lastValueFrom(
      this.http.get<any>(Constants.URL_BASE + "secure/favourite", this.generateHeaders())
    )
  }

  async deleteFavouriteBusStops(username: string, busStopCode: string) {
    return await lastValueFrom(
      this.http.delete(Constants.URL_BASE + "secure/favourite/" + busStopCode, this.generateHeaders())
    )
  }

  async addNotification(notification: NotificationForm) {
    return await lastValueFrom(
      this.http.post<any>(
        Constants.URL_BASE + "secure/notification",
        notification,
        this.generateHeaders())
    )
  }

  async getNotifications(username: String) {
    return await lastValueFrom(
      this.http.get<any>(Constants.URL_BASE + "secure/notification", this.generateHeaders())
    )
  }

  async deleteNotification(username: string, busStopCode: string, cronString: string) {
    return await lastValueFrom(
      this.http.delete(Constants.URL_BASE + "secure/notification/" + busStopCode + "/" + cronString, this.generateHeaders())
    )
  }

  generateHeaders() {
    return {
      headers: new HttpHeaders({
        Authorization: this.tokenService.jwtToken || "",
        Username: this.tokenService.username || ""
      })
    }
  }
}
