import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import Dexie from "dexie";
import { last, lastValueFrom } from "rxjs";
import { Constants } from "../constants";
import { BusStop } from "../models/model";

@Injectable()
export class BusStopService extends Dexie {
    busStop: Dexie.Table<BusStop, string>

    constructor(
        private http: HttpClient
    ) {
        super('BusStopDB')

        this.version(1).stores({
            busStopList: 'BusStopCode'
        })

        this.busStop = this.table('busStopList')
    }

    public async getAndSaveBusStops() {
        let jsonStringData = await lastValueFrom(
            this.http.get<any>(Constants.URL_BASE + "api/BusStops")
        )
        console.log(jsonStringData)
        for (let jsonObject of jsonStringData) {
            const bs = jsonObject as BusStop
            this.addBusStop(bs)
        }
    }

    public addBusStop(busStop: BusStop) {
        return this.busStop.put(busStop)
    }

    public async getNumberOfBusStops() {
        return await this.busStop.count()
    }

    public async search(queryTerm: string): Promise<BusStop[]> {
        return await this.busStop
            .filter((bs: BusStop) => bs.Description.toLowerCase().includes(queryTerm) || bs.RoadName.toLowerCase().includes(queryTerm))
            .toArray()
    }

}
