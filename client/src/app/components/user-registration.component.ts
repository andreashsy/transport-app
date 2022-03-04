import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../models/model';
import { TokenService } from '../services/token.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  newUserForm!: FormGroup
  token!: string

  constructor(
    private tokenService: TokenService,
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.newUserForm = this.createForm();
    this.token = this.tokenService.firebaseToken
  }

  createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.minLength(4)]),
      password: this.fb.control('', [Validators.required, Validators.minLength(8)])
    })
  }

  async submitRegistration() {
    let user = this.newUserForm.value as User
    user.notificationToken = this.token
    await this.userService.sendRegistration(user)
      .then(result => {
        console.info(result)
        alert(`Registration for ${user.username} successful! Please log in.`)
        this.router.navigate(['/'])
      })
      .catch(err => {
        console.error(err)
      })
  }
}
