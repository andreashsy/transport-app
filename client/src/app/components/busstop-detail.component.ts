import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BusService } from '../models/model';
import { BusArrivalService } from '../services/busarrival.service';
import { BusStopService } from '../services/busstop.service';

@Component({
  selector: 'app-busstop-detail',
  templateUrl: './busstop-detail.component.html',
  styleUrls: ['./busstop-detail.component.css']
})
export class BusstopDetailComponent implements OnInit {

  busStopId!: string
  busServices: BusService[] = []
  timeNow: number = Date.now()

  constructor(
    private activatedRoute: ActivatedRoute,
    private busArrivalSvc: BusArrivalService
  ) { }

  ngOnInit(): void {
    this.busStopId = this.activatedRoute.snapshot.params['busStopId']
    this.getArrivalData()
  }

  async getArrivalData() {
    await this.busArrivalSvc.getBusServices(this.busStopId)
      .then(result => {
        console.log("Arrival data success! ", result)
        this.busServices = result
        this.timeNow = Date.now()
      })
      .catch(error => {
        console.error("app.component.ts getArrivalData ERROR: ", error)
      })
  }

}
