import { Component, OnInit } from '@angular/core';
import { environment } from "../environments/environment";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { TokenService } from './services/token.service';
import { NavigationStart, Router } from '@angular/router';
import { UserFavouritesComponent } from './components/user-favourites.component';
import { User } from './models/model';
import { UserService } from './services/user.service';

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
    private userService: UserService,
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
      console.log('Message received. ', payload);
      this.message=payload;
      alert(JSON.stringify(payload))
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
