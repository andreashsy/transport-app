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

const appRoutes: Routes = [
  { path: 'busStop/:busStopId', component: BusstopDetailComponent},
  { path: '', component: MainComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    BusstopDetailComponent,
    MainComponent
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
    VersionService
   ],
  bootstrap: [AppComponent]
})
export class AppModule { }
