import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BusStopService } from './services/busstop.service';
import { BusArrivalService } from './services/busarrival.service';
import { VersionService } from './services/version.service';
import { BusstopDetailComponent } from './components/busstop-detail.component';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main.component';
import { environment } from "../environments/environment";
import { initializeApp } from "firebase/app";
import { UserRegistrationComponent } from './components/user-registration.component';
import { TokenService } from './services/token.service';
import { UserService } from './services/user.service';
import { UserLoginComponent } from './components/user-login.component';
import { UserFavouritesComponent } from './components/user-favourites.component';
import { UserNotificationsComponent } from './components/user-notifications.component';

initializeApp(environment.firebase);
const appRoutes: Routes = [
  { path: '', component: MainComponent },
  { path: 'busStop/:busStopId', component: BusstopDetailComponent },
  { path: 'register', component: UserRegistrationComponent },
  { path: 'login', component: UserLoginComponent },
  { path: 'favourites', component: UserFavouritesComponent },
  { path: 'notifications', component: UserNotificationsComponent },
]

@NgModule({
  declarations: [
    AppComponent,
    BusstopDetailComponent,
    MainComponent,
    UserRegistrationComponent,
    UserLoginComponent,
    UserFavouritesComponent,
    UserNotificationsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    BusStopService,
    BusArrivalService,
    VersionService,
    TokenService,
    UserService
   ],
  bootstrap: [AppComponent]
})
export class AppModule { }
