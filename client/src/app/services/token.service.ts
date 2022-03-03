import { Injectable } from "@angular/core";

@Injectable()
export class TokenService {
  public token!: string;
  public jwtToken!: string;
  public username!: string;

  setJwtToken(jwtToken: string) {
    this.jwtToken = "Bearer " + jwtToken
  }
}
