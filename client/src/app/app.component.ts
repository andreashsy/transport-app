import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BusService, BusStop } from './models/model';
import { BusArrivalService } from './services/busarrival.service';
import { BusStopService } from './services/busstop.service';
import { VersionService } from './services/version.service';
import { environment } from "../environments/environment";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { TokenService } from './services/token.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit,OnDestroy {
  token: string = "";
  message:any = null;
  constructor(
    private tokenService: TokenService
  ) {}
  ngOnInit(): void {
    this.requestPermission();
    this.listen();
  }

  ngOnDestroy(): void {
  }
  requestPermission() {
    const messaging = getMessaging();
    getToken(messaging,
     { vapidKey: environment.firebase.vapidKey}).then(
       (currentToken) => {
         if (currentToken) {
           console.log("We got the token...");
           console.log(currentToken);
           this.tokenService.token=currentToken;
         } else {
           console.log('No registration token available. Request permission to generate one.');
         }
     }).catch((err) => {
        console.log('An error occurred while retrieving token. ', err);
    });
  }
  listen() {
    const messaging = getMessaging();
    onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
      this.message=payload;
    });
  }
}
