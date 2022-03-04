import { AfterContentInit, Component, DoCheck, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BusService, BusStop } from './models/model';
import { BusArrivalService } from './services/busarrival.service';
import { BusStopService } from './services/busstop.service';
import { VersionService } from './services/version.service';
import { environment } from "../environments/environment";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { TokenService } from './services/token.service';
import { NavigationStart, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  token: string = "";
  message:any = null;
  user: string = "";
  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.requestPermission();
    this.listenForNotifications();
    this.router.events.forEach((event) => {
      if(event instanceof NavigationStart) {
        if (this.tokenService.jwtToken) {
          this.user = this.tokenService.username
        }
      }
    });

  }

  requestPermission() {
    const messaging = getMessaging();
    getToken(messaging,
     { vapidKey: environment.firebase.vapidKey}).then(
       (currentToken) => {
         if (currentToken) {
           console.log("Firebase notification token received: ", currentToken);
           this.tokenService.token=currentToken;
         } else {
           console.log('No registration token available. Request permission to generate one.');
         }
     }).catch((err) => {
        console.log('An error occurred while retrieving token. ', err);
    });
  }
  listenForNotifications() {
    const messaging = getMessaging();
    onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
      this.message=payload;
    });
  }

  logout() {
    this.tokenService.clearJwtTokenAndUsername()
    this.user = ""
    this.router.navigate(['/'])
  }
}
