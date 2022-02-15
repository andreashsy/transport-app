import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BusStopService } from './services/busstop.service';
import { BusArrivalService } from './services/busarrival.service';
import { VersionService } from './services/version.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [ 
    BusStopService,
    BusArrivalService,
    VersionService
   ],
  bootstrap: [AppComponent]
})
export class AppModule { }
