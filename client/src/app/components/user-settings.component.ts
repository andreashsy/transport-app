import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { User } from '../models/model';
import { TokenService } from '../services/token.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css']
})
export class UserSettingsComponent implements OnInit {

  form!: FormGroup

  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      telegramUsername: this.fb.control('')
    })
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

  async updateTelegramUsername() {
    let telegramUsername = this.form.value.telegramUsername
    this.userService.updateTelegramUsername(telegramUsername)
      .then(result => {
        console.info(result)
        alert("Update success!")
        this.form.reset('')
      })
      .catch(err => {
        console.error(err)
      })
  }

}
