import { Component, OnInit } from '@angular/core';
import { environment } from "../environments/environment";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { TokenService } from './services/token.service';
import { NavigationStart, Router } from '@angular/router';
import { User } from './models/model';
import { UserService } from './services/user.service';
import { VersionService } from './services/version.service';
import { Constants } from './constants';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  token: string = "";
  message:any = null;
  user: string = "";
  clientVersion: string = Constants.VERSION
  serverVersion: string = ""

  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private versionSvc: VersionService,
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
    this.versionSvc.getVersion()
      .then(result => {
        this.serverVersion = result
      })
      .catch(error => {
        console.error(error)
      })

  }

  requestPermission() {
    const messaging = getMessaging();
    getToken(messaging,
     { vapidKey: environment.firebase.vapidKey}).then(
       (currentToken) => {
         if (currentToken) {
           console.log("Firebase notification token received: ", currentToken);
           this.tokenService.firebaseToken=currentToken;
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
      console.log('Notification received. ', payload);
      this.message=payload;
      alert(JSON.stringify(payload.notification?.body))
    });
  }

  logout() {
    this.tokenService.clearJwtTokenAndUsername()
    this.user = ""
    this.router.navigate(['/'])
  }

  async updateToken() {
    let userObj = {} as User
    userObj.notificationToken = this.tokenService.firebaseToken
    userObj.username = this.tokenService.username
    userObj.password = ""
    await this.userService.updateToken(userObj)
      .then(result => {
        console.info(result)
        alert("Token successfully updated!")
      })
      .catch(err => {
        console.error(JSON.stringify(err))
      })

  }
}
