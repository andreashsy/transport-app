import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../services/token.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-favourites',
  templateUrl: './user-favourites.component.html',
  styleUrls: ['./user-favourites.component.css']
})
export class UserFavouritesComponent implements OnInit {

  favStops: string = ""
  stops: string[] = []

  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.initialiseFavourites()
  }

  initialiseFavourites() {
    if (this.tokenService.jwtToken) {
      this.userService.getFavouriteBusStops(this.tokenService.username)
        .then(result => {
          this.favStops = JSON.stringify(result)
          this.stops = result.favourites
        })
        .catch(err => {
          let error = JSON.stringify(err)
          alert(error)
          console.error(error)
        })
      } else {
        alert("Please login first!")
        this.router.navigate([''])
      }
  }

  async deleteFavourite(index: number) {
    console.log("deleting stop number: ", this.stops[index])
    await this.userService.deleteFavouriteBusStops(this.tokenService.username, this.stops[index])
      .then(result => {
        console.info(result)
        this.stops.splice(index, 1)
      })
      .catch(err => {
        console.error(err)
      })
  }



}
