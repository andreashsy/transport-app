import { Time } from "@angular/common";

export interface BusStop {
    BusStopCode: string
    RoadName: string
    Description: string
    Latitude: string
    Longitude: string
}

export interface Bus {
    OriginCode: string
    DestinationCode: string
    EstimatedArrival: string
    Latitude: string
    Longitude: string
    VisitNumber: string
    Load: string
    Feature: string
    Type: string
    EstArr?: Date

}

export interface BusService {
    ServiceNo: string
    Operator: string
    NextBus?: Bus
    NextBus2?: Bus
    NextBus3?: Bus
}

export interface User {
  username: string
  password: string
  notificationToken?: string
}

export interface NotificationForm {
  busStopCode: string
  time: Time
  dayOfWeek: string
  username: string
}
