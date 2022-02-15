import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Constants } from "../constants";

@Injectable()
export class VersionService{
    constructor(private http: HttpClient) {}

    public async getVersion(): Promise<string> {
        const resp = await lastValueFrom(
            this.http.get<any>(Constants.URL_BASE + "api/version")
        )
        return resp.version
    }
}