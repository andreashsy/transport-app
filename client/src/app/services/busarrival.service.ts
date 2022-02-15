import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Constants } from "../constants";
import { BusService } from "../models/model";

@Injectable()
export class BusArrivalService {
    constructor(private http: HttpClient) { }

    public async getBusServices(busStopCode: string): Promise<BusService[]> {
        var busServices: BusService[] = []
        let rawJson = await lastValueFrom(
            this.http.get<any>(Constants.URL_BASE + "api/BusStop/" + busStopCode)
        )
        console.log(rawJson)
        for (let jsonServiceObject of rawJson.Services) {
            var busService = jsonServiceObject as BusService
            if (busService.NextBus) busService.NextBus.EstArr = new Date(busService.NextBus.EstimatedArrival)
            if (busService.NextBus2) busService.NextBus2.EstArr = new Date(busService.NextBus2.EstimatedArrival)
            if (busService.NextBus3) busService.NextBus3.EstArr = new Date(busService.NextBus3.EstimatedArrival)
            busServices.push(busService)
            console.log("Bus Number: ", busService.ServiceNo)
            console.log("First bus time: ", busService.NextBus?.EstimatedArrival)
            console.log("Second bus time: ", busService.NextBus2?.EstimatedArrival)
            console.log("Third bus time: ", busService.NextBus3?.EstimatedArrival)
        }
        return busServices
    }
}