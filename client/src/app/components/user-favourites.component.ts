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



}
