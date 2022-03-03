import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BusService } from '../models/model';
import { BusArrivalService } from '../services/busarrival.service';
import { BusStopService } from '../services/busstop.service';
import { TokenService } from '../services/token.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-busstop-detail',
  templateUrl: './busstop-detail.component.html',
  styleUrls: ['./busstop-detail.component.css']
})
export class BusstopDetailComponent implements OnInit {

  busStopId!: string
  busServices: BusService[] = []
  timeNow: number = Date.now()
  user: string = ""

  constructor(
    private activatedRoute: ActivatedRoute,
    private busArrivalService: BusArrivalService,
    private tokenService: TokenService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.busStopId = this.activatedRoute.snapshot.params['busStopId']
    this.getArrivalData()
    if (this.tokenService.jwtToken) {
      this.user = this.tokenService.username
    }

  }

  async getArrivalData() {
    await this.busArrivalService.getBusServices(this.busStopId)
      .then(result => {
        console.log("Arrival data success! ", result)
        this.busServices = result
        this.timeNow = Date.now()
      })
      .catch(error => {
        console.error("app.component.ts getArrivalData ERROR: ", error)
      })
  }

  async saveToFavourites() {
    await this.userService.saveFavouriteBusStop(this.user, this.busStopId)
      .then(result => {
        console.info(result)
        alert("Save successful!")
      })
      .catch(err => {
        console.error(err)
        alert(err)
      })
  }

}
