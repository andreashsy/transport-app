import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BusService, BusStop } from '../models/model';
import { BusArrivalService } from '../services/busarrival.service';
import { BusStopService } from '../services/busstop.service';
import { TokenService } from '../services/token.service';
import { UserService } from '../services/user.service';
import { VersionService } from '../services/version.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  searchForm!: FormGroup
  arrivalForm!: FormGroup
  busStops: BusStop[] = []
  busServices: BusService[] = []
  numStops!: number
  searchMsg: string = ""
  timeNow: number = Date.now()
  user: string = ""

  constructor(
    private fb: FormBuilder,
    private busStopSvc: BusStopService,
    private busArrivalSvc: BusArrivalService,
    private tokenSvc: TokenService,
    private userSvc: UserService
  ) {}

  ngOnInit(): void {
    this.searchForm = this.createSearchForm()
    this.arrivalForm = this.createArrivalForm()
    this.busStopSvc.getNumberOfBusStops()
      .then(result => {
        this.numStops = result
      })
      .catch(error => {
        console.error(error)
      })

    if (this.tokenSvc.jwtToken) {
      this.user = this.tokenSvc.username
    }
  }

  createSearchForm(): FormGroup {
    return this.fb.group({
      searchWords: this.fb.control('', [Validators.required])
    })
  }

  createArrivalForm(): FormGroup {
    return this.fb.group({
      busStopCode: this.fb.control('', [Validators.required])
    })
  }

  async search() {
    this.searchMsg = ""
    await this.busStopSvc.search(this.searchForm.value.searchWords.toLowerCase())
      .then(result => {
        console.log("Search success!")
        this.busStops = result
        if (result.length < 1) {
          this.searchMsg = "No result found for " + this.searchForm.value.searchWords
        }
      })
      .catch(error => {
        console.error(error)
        this.searchMsg = error
      })
  }

  async getArrivalData() {
    await this.busArrivalSvc.getBusServices(this.arrivalForm.value.busStopCode)
      .then(result => {
        console.log("Arrival data success! ", result)
        this.busServices = result
        this.timeNow = Date.now()
      })
      .catch(error => {
        console.error("app.component.ts getArrivalData ERROR: ", error)
      })
  }

  async reloadBusStops() {
    await this.busStopSvc.getAndSaveBusStops()
    await this.busStopSvc.getNumberOfBusStops()
      .then(result => {
        this.numStops = result
      })
      .catch(error => {
        console.error(error)
      })
  }

  async saveBusStop() {
    await this.userSvc.saveFavouriteBusStop(this.tokenSvc.username, this.arrivalForm.value.busStopCode)
      .then(result => {
        console.info(result)
      })
      .catch(err => {
        console.error(err)
      })
  }

}
