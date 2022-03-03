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

initializeApp(environment.firebase);
const appRoutes: Routes = [
  { path: 'busStop/:busStopId', component: BusstopDetailComponent},
  { path: '', component: MainComponent},
  { path: 'register', component: UserRegistrationComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    BusstopDetailComponent,
    MainComponent,
    UserRegistrationComponent
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
