import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Constants } from "../constants";
import { BusStop, User } from "../models/model";
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
    const httpOptions = this.generateHeader()

    return await lastValueFrom(
      this.http.post<any>(Constants.URL_BASE + "secure/favourite/" + username, busStopCode, httpOptions)
    )
  }

  async getFavouriteBusStops(username: String) {
    const httpOptions = this.generateHeader()

    return await lastValueFrom(
      this.http.get<any>(Constants.URL_BASE + "secure/favourite/" + username, httpOptions)
    )
  }

  async deleteFavouriteBusStops(username: string, busStopCode: string) {
    const httpOptions = this.generateHeader()

    return await lastValueFrom(
      this.http.delete(Constants.URL_BASE + "secure/favourite/" + username + "/" + busStopCode, httpOptions)
    )
  }

  generateHeader() {
    return {
      headers: new HttpHeaders({
        Authorization: this.tokenService.jwtToken || ""
      })
    }
  }
}
